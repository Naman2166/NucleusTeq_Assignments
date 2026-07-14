"""
Quiz business logic
"""

from app.repositories.question_repository import QuestionRepository
from app.schemas.quiz_schema import QuizCreate, QuizUpdate, QuizResponse
from app.schemas.common_schema import MessageResponse
from app.exceptions.custom_exceptions import (ConflictException, ResourceNotFoundException, BadRequestException)
from app.utils.constants import CategoryMessage, QuizMessage
from app.utils.logger import logger
from app.repositories.quiz_repository import QuizRepository
from app.utils.helper import validate_object_id


def quiz_helper(quiz):
    """
    Converts MongoDB quiz document into response dictionary
    """
    quiz_response = {
        "id": str(quiz["_id"]),
        "title": quiz["title"],
        "description": quiz["description"],
        "category_id": str(quiz["category_id"]),
        "duration": quiz["duration"],
        "total_marks": quiz["total_marks"],
        "passing_marks": quiz["passing_marks"],
        "max_attempts": quiz["max_attempts"],
    }

    return quiz_response



class QuizService:

    @staticmethod
    async def create_quiz(quiz: QuizCreate) -> QuizResponse:
        """
        Create a new quiz
        """
        
        category_object_id = validate_object_id(quiz.category_id, CategoryMessage.INVALID_ID)

        category = await QuizRepository.get_category_by_id(category_object_id)

        if not category:
            logger.warning(CategoryMessage.NOT_FOUND)
            raise ResourceNotFoundException(CategoryMessage.NOT_FOUND)

        if quiz.passing_marks > quiz.total_marks:
            logger.warning(QuizMessage.INVALID_PASSING_MARKS)
            raise BadRequestException(QuizMessage.INVALID_PASSING_MARKS)

        quizzes = await QuizRepository.get_all_quizzes()

        for existing_quiz in quizzes:
            if (existing_quiz["category_id"] == category_object_id
                and existing_quiz["title"].strip().lower() == quiz.title.strip().lower()):
                logger.warning(QuizMessage.ALREADY_EXISTS)
                raise ConflictException(QuizMessage.ALREADY_EXISTS)

        quiz_data = {
            "title": quiz.title.strip(),
            "description": quiz.description.strip(),
            "category_id": category_object_id,
            "duration": quiz.duration,
            "total_marks": quiz.total_marks,
            "passing_marks": quiz.passing_marks,
            "max_attempts": quiz.max_attempts,
        }

        result = await QuizRepository.create_quiz(quiz_data)

        created_quiz = await QuizRepository.get_quiz_by_id(result.inserted_id)

        logger.info("Quiz created successfully")

        response = QuizResponse(**quiz_helper(created_quiz))

        return response
          


    @staticmethod
    async def get_all_quizzes() -> list[QuizResponse]:
        """
        Retrieve all quizzes
        """
        quizzes = []
        saved_quizzes = await QuizRepository.get_all_quizzes()

        for quiz in saved_quizzes:
            quizzes.append(QuizResponse(**quiz_helper(quiz)))

        logger.info("All quizzes retrieved successfully")

        return quizzes



    @staticmethod
    async def get_quiz_by_id(quiz_id: str) -> QuizResponse:
        """
        Retrieve a quiz by its ID
        """

        object_id = validate_object_id(quiz_id, QuizMessage.INVALID_ID)

        quiz = await QuizRepository.get_quiz_by_id(object_id)

        if not quiz:
            logger.warning(QuizMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizMessage.NOT_FOUND)

        logger.info("Quiz retrieved successfully")

        response = QuizResponse(**quiz_helper(quiz))

        return response



    @staticmethod
    async def get_quizzes_by_category(category_id: str) -> list[QuizResponse]:
        """
        Retrieve all quizzes belonging to specific category
        """

        category_object_id = validate_object_id(category_id, CategoryMessage.INVALID_ID)

        category = await QuizRepository.get_category_by_id(category_object_id)

        if not category:
            logger.warning(CategoryMessage.NOT_FOUND)
            raise ResourceNotFoundException(CategoryMessage.NOT_FOUND)

        quizzes = []

        saved_quizzes = await QuizRepository.get_quizzes_by_category(category_object_id)

        for quiz in saved_quizzes:
            quizzes.append(QuizResponse(**quiz_helper(quiz)))

        logger.info("Quizzes retrieved successfully by category")

        return quizzes



    @staticmethod
    async def update_quiz(quiz_id: str, quiz: QuizUpdate) -> QuizResponse:
        """
        Update an existing quiz
        """
    
        quiz_object_id = validate_object_id(
            quiz_id,
            QuizMessage.INVALID_ID,
        )
    
        existing_quiz = await QuizRepository.get_quiz_by_id(quiz_object_id)
    
        if not existing_quiz:
            logger.warning(QuizMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizMessage.NOT_FOUND)
    
        update_data = quiz.model_dump(exclude_unset=True)
    
        if not update_data:
            logger.warning(QuizMessage.NO_UPDATE_DATA)
            raise BadRequestException(QuizMessage.NO_UPDATE_DATA)
    
        if "category_id" in update_data:
            category_object_id = validate_object_id(
                update_data["category_id"],
                CategoryMessage.INVALID_ID,
            )
    
            category = await QuizRepository.get_category_by_id(category_object_id)
    
            if not category:
                logger.warning(CategoryMessage.NOT_FOUND)
                raise ResourceNotFoundException(CategoryMessage.NOT_FOUND)
    
            update_data["category_id"] = category_object_id
    
        total_marks = update_data.get("total_marks", existing_quiz["total_marks"])
        passing_marks = update_data.get("passing_marks", existing_quiz["passing_marks"])
    
        if passing_marks > total_marks:
            logger.warning(QuizMessage.INVALID_PASSING_MARKS)
            raise BadRequestException(QuizMessage.INVALID_PASSING_MARKS)
    
        quiz_title = update_data.get("title", existing_quiz["title"]).strip()
        category_id = update_data.get("category_id", existing_quiz["category_id"])
    
        quizzes = await QuizRepository.get_all_quizzes()
    
        for existing in quizzes:
            if (
                existing["_id"] != quiz_object_id
                and existing["category_id"] == category_id
                and existing["title"].strip().lower() == quiz_title.lower()
            ):
                logger.warning(QuizMessage.ALREADY_EXISTS)
                raise ConflictException(QuizMessage.ALREADY_EXISTS)
    
        if "title" in update_data:
            update_data["title"] = quiz_title
    
        if "description" in update_data:
            update_data["description"] = update_data["description"].strip()
    
        await QuizRepository.update_quiz(quiz_object_id, update_data)
    
        updated_quiz = await QuizRepository.get_quiz_by_id(quiz_object_id)
    
        logger.info("Quiz updated successfully")
        response = QuizResponse(**quiz_helper(updated_quiz))
    
        return response



    @staticmethod
    async def delete_quiz(quiz_id: str) -> MessageResponse:
        """
        Delete a quiz by its ID
        """
        
        object_id = validate_object_id(quiz_id, QuizMessage.INVALID_ID)

        quiz = await QuizRepository.get_quiz_by_id(object_id)

        if not quiz:
            logger.warning(QuizMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizMessage.NOT_FOUND)

        await QuestionRepository.delete_questions_by_quiz(object_id)
        await QuizRepository.delete_quiz(object_id)

        logger.info("Quiz deleted successfully")

        response = MessageResponse(message=QuizMessage.DELETED)

        return response