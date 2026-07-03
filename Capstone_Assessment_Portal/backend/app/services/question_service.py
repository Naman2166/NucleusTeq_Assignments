"""
Question business logic
"""

from bson import ObjectId
from bson.errors import InvalidId
from app.repositories.question_repository import QuestionRepository
from app.repositories.quiz_repository import QuizRepository
from app.schemas.question_schema import (QuestionCreate, QuestionResponseStudent, QuestionUpdate, QuestionResponseAdmin)
from app.schemas.common_schema import MessageResponse
from app.exceptions.custom_exceptions import (BadRequestException, ResourceNotFoundException)
from app.utils.constants import (QuestionMessage, QuizMessage, QuestionType)
from app.utils.logger import logger
from app.utils.helper import validate_object_id



def question_helper_admin(question: dict) -> QuestionResponseAdmin:
    """
    Helper to Convert MongoDB document to QuestionResponseAdmin
    """
    response = QuestionResponseAdmin(
        id=str(question["_id"]),
        quiz_id=str(question["quiz_id"]),
        question=question["question"],
        question_type=question["question_type"],
        options=question["options"],
        correct_answer=question["correct_answer"],
        difficulty=question["difficulty"],
        tags=question["tags"],
        marks=question["marks"],
    )

    return response


def question_helper_student(question: dict) -> QuestionResponseStudent:
    """
    Helper to convert MongoDB document to QuestionResponseStudent
    """
    response = QuestionResponseStudent(
        id=str(question["_id"]),
        question=question["question"],
        question_type=question["question_type"],
        options=question["options"],
        difficulty=question["difficulty"],
        marks=question["marks"],
    )

    return response


def validate_question(question_type: QuestionType, options: list[str], correct_answer: int) -> None:
    """
    Helper to Validate question type, options and correct answer
    """
    options = [option.strip() for option in options]

    if any(not option for option in options):
        logger.warning(QuestionMessage.EMPTY_OPTION)
        raise BadRequestException(QuestionMessage.EMPTY_OPTION)

    if question_type == QuestionType.MCQ:
        if len(options) < 2:
            logger.warning(QuestionMessage.INVALID_OPTIONS)
            raise BadRequestException(QuestionMessage.INVALID_OPTIONS)
        
        if len(set(options)) != len(options):
            logger.warning(QuestionMessage.DUPLICATE_OPTIONS)
            raise BadRequestException(QuestionMessage.DUPLICATE_OPTIONS)

        if correct_answer < 1 or correct_answer > len(options):
            logger.warning(QuestionMessage.INVALID_CORRECT_ANSWER)
            raise BadRequestException(QuestionMessage.INVALID_CORRECT_ANSWER)

    elif question_type == QuestionType.TRUE_FALSE:
        if options != ["True", "False"]:
            logger.warning(QuestionMessage.INVALID_TRUE_FALSE_OPTIONS)
            raise BadRequestException(QuestionMessage.INVALID_TRUE_FALSE_OPTIONS)
        
        if correct_answer not in [1, 2]:
            logger.warning(QuestionMessage.INVALID_CORRECT_ANSWER)
            raise BadRequestException(QuestionMessage.INVALID_CORRECT_ANSWER)



class QuestionService:
    """
    Business logic for question operations
    """

    @staticmethod
    async def create_question(question: QuestionCreate) -> QuestionResponseAdmin:
        """
        Create a new question
        """
        logger.info("Creating question")

        quiz_object_id = validate_object_id(
            question.quiz_id,
            QuizMessage.INVALID_ID,
        )

        quiz = await QuizRepository.get_quiz_by_id(quiz_object_id)
    
        if not quiz:
            logger.warning(QuizMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizMessage.NOT_FOUND)
    
        validate_question(question.question_type, question.options, question.correct_answer)
    
        questions = await QuestionRepository.get_questions_by_quiz(quiz_object_id)
    
        assigned_marks = sum(
            existing_question["marks"] for existing_question in questions
        )
    
        if assigned_marks + question.marks > quiz["total_marks"]:
            logger.warning(QuestionMessage.MARKS_EXCEEDED)
            raise BadRequestException(QuestionMessage.MARKS_EXCEEDED)
    
        question_data = {
            "quiz_id": quiz_object_id,
            "question": question.question.strip(),
            "question_type": question.question_type,
            "options": [option.strip() for option in question.options],
            "correct_answer": question.correct_answer,
            "difficulty": question.difficulty,
            "tags": question.tags,
            "marks": question.marks,
        }
    
        result = await QuestionRepository.create_question(question_data)
    
        created_question = await QuestionRepository.get_question_by_id(result.inserted_id)
    
        logger.info("Question created successfully")
        response = question_helper_admin(created_question)
    
        return response
    


    @staticmethod
    async def get_question_by_id_student(question_id: str) -> QuestionResponseStudent:
        """
        Get a question by its ID for student
        """
        logger.info(f"Getting question with id: {question_id}")
        
        object_id = validate_object_id(question_id, QuestionMessage.INVALID_ID)
    
        question = await QuestionRepository.get_question_by_id(object_id)
    
        if not question:
            logger.warning(QuestionMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuestionMessage.NOT_FOUND)
    
        logger.info(f"Question retrieved successfully: {question_id}")
    
        response = question_helper_student(question)
    
        return response
    


    @staticmethod
    async def get_question_by_id_admin(question_id: str) -> QuestionResponseAdmin:
        """
        Get a question by its ID for admin
        """
        logger.info(f"Getting question with id: {question_id}")
        
        object_id = validate_object_id(question_id, QuestionMessage.INVALID_ID)

        question = await QuestionRepository.get_question_by_id(object_id)
    
        if not question:
            logger.warning(QuestionMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuestionMessage.NOT_FOUND)
    
        response = question_helper_admin(question)
    
        logger.info(f"Question retrieved successfully: {question_id}")
    
        return response
    


    @staticmethod
    async def get_questions_by_quiz_student(quiz_id: str) -> list[QuestionResponseStudent]:
        """
        Get all questions for a quiz for student
        """
        logger.info(f"Getting questions for quiz: {quiz_id}")
        
        object_id = validate_object_id(quiz_id, QuizMessage.INVALID_ID)
    
        quiz = await QuizRepository.get_quiz_by_id(object_id)
    
        if not quiz:
            logger.warning(QuizMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizMessage.NOT_FOUND)
    
        questions = await QuestionRepository.get_questions_by_quiz(object_id)
    
        response = [question_helper_student(question) for question in questions]
    
        logger.info("Questions retrieved successfully")
    
        return response
    


    @staticmethod
    async def get_questions_by_quiz_admin(quiz_id: str) -> list[QuestionResponseAdmin]:
        """
        Get all questions for a quiz for admin
        """
        logger.info(f"Getting questions for quiz: {quiz_id}")
    
        object_id = validate_object_id(quiz_id, QuizMessage.INVALID_ID)
    
        quiz = await QuizRepository.get_quiz_by_id(object_id)
    
        if not quiz:
            logger.warning(QuizMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizMessage.NOT_FOUND)
    
        questions = await QuestionRepository.get_questions_by_quiz(object_id)
    
        response = [question_helper_admin(question) for question in questions]
    
        logger.info("Questions retrieved successfully")
    
        return response



    @staticmethod
    async def update_question(question_id: str, question: QuestionUpdate) -> QuestionResponseAdmin:
        """
        Update a question
        """
        logger.info(f"Updating question with id: {question_id}")
        
        object_id = validate_object_id(question_id, QuestionMessage.INVALID_ID)

        existing_question = await QuestionRepository.get_question_by_id(object_id)
    
        if not existing_question:
            logger.warning(QuestionMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuestionMessage.NOT_FOUND)
    
        update_data = question.model_dump(exclude_unset=True)

        if "options" in update_data:
            update_data["options"] = [option.strip() for option in update_data["options"]]
    
        question_type = update_data.get(
            "question_type",
            existing_question["question_type"]
        )
    
        options = update_data.get(
            "options",
            existing_question["options"]
        )
    
        correct_answer = update_data.get(
            "correct_answer",
            existing_question["correct_answer"]
        )
    
        validate_question(question_type, options, correct_answer)
    
        if "marks" in update_data:

            questions = await QuestionRepository.get_questions_by_quiz(
                existing_question["quiz_id"]
            )
    
            assigned_marks = sum(
                existing_question["marks"] for existing_question in questions
            )
    
            new_total_marks = (
                assigned_marks - existing_question["marks"] + update_data["marks"]
            )
    
            quiz = await QuizRepository.get_quiz_by_id(
                existing_question["quiz_id"]
            )
    
            if new_total_marks > quiz["total_marks"]:
                logger.warning(QuestionMessage.MARKS_EXCEEDED)
                raise BadRequestException(QuestionMessage.MARKS_EXCEEDED)
    
        if "question" in update_data:
            update_data["question"] = update_data["question"].strip()
    
        await QuestionRepository.update_question(object_id, update_data)
    
        updated_question = await QuestionRepository.get_question_by_id(object_id)
    
        logger.info(f"Question updated successfully: {question_id}")
        response = question_helper_admin(updated_question)
    
        return response
    


    @staticmethod
    async def delete_question(question_id: str) -> MessageResponse:
        """
        Delete a question
        """
        logger.info(f"Deleting question with id: {question_id}")
    
        object_id = validate_object_id(question_id, QuestionMessage.INVALID_ID)
    
        existing_question = await QuestionRepository.get_question_by_id(object_id)
    
        if not existing_question:
            logger.warning(QuestionMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuestionMessage.NOT_FOUND)
    
        await QuestionRepository.delete_question(object_id)
    
        logger.info(f"Question deleted successfully: {question_id}")
    
        response = MessageResponse(message=QuestionMessage.DELETED)
    
        return response