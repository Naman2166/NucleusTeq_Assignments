"""
Quiz Attempt business logic
"""

from datetime import datetime
from bson import ObjectId
from app.repositories.question_repository import QuestionRepository
from app.repositories.quiz_repository import QuizRepository
from app.repositories.quiz_attempt_repository import QuizAttemptRepository
from app.schemas.quiz_attempt_schema import (AttemptCreate, AttemptQuestionResponse, QuizSnapshot, AttemptQuestionSnapshot, AttemptResponse, StudentAnswer)
from app.schemas.common_schema import MessageResponse
from app.exceptions.custom_exceptions import (BadRequestException, ResourceNotFoundException)
from app.utils.constants import (QuestionMessage, QuizAttemptMessage, QuizAttemptStatus, QuizMessage)
from app.utils.logger import logger
from app.utils.helper import validate_object_id



def create_quiz_snapshot(quiz: dict, questions: list[dict]) -> QuizSnapshot:
    """
    Create quiz snapshot for an attempt
    """
    snapshot = QuizSnapshot(
        title=quiz["title"],
        duration=quiz["duration"],
        total_marks=quiz["total_marks"],
        passing_marks=quiz["passing_marks"],
        questions=[
            AttemptQuestionSnapshot(
                question_id=str(question["_id"]),
                question=question["question"],
                question_type=question["question_type"],
                options=question["options"],
                correct_answer=question["correct_answer"],
                difficulty=question["difficulty"],
                tags=question["tags"],
                marks=question["marks"],
            )
            for question in questions
        ]
    )

    return snapshot


def attempt_helper(attempt: dict) -> AttemptResponse:
    """
    Helper to convert MongoDB document to AttemptResponse
    """
    response = AttemptResponse(
        id=str(attempt["_id"]),
        quiz_id=str(attempt["quiz_id"]),
        attempt_number=attempt["attempt_number"],
        status=attempt["status"],
        started_at=attempt["started_at"],
        submitted_at=attempt.get("submitted_at"),
    )

    return response


def check_attempt_time_expired(attempt: dict) -> bool:
    """
    Check whether the quiz attempt has exceeded the time limit
    """
    time_spent = (datetime.now() - attempt["started_at"]).total_seconds()
    allowed_time = attempt["snapshot"]["duration"] * 60

    has_time_expired =  time_spent >= allowed_time

    return has_time_expired


def calculate_score(attempt: dict) -> int:
    """
    Calculate score for a quiz attempt
    """
    score = 0
    answer_map = {}

    for answer in attempt["answers"]:
        answer_map[answer["question_id"]] = answer["selected_option"]

    for question in attempt["snapshot"]["questions"]:
        selected_option = answer_map.get(question["question_id"])

        if selected_option == question["correct_answer"]:
            score += question["marks"]

    return score


async def auto_submit_attempt(attempt_id: ObjectId, attempt: dict) -> None:
    """
    Automatically submit an expired quiz attempt
    """
    score = calculate_score(attempt)

    update_data = {
        "status": QuizAttemptStatus.TIME_EXPIRED,
        "submitted_at": datetime.now(),
        "score": score,
    }

    await QuizAttemptRepository.update_attempt(attempt_id, update_data)



class QuizAttemptService:
    """
    Business logic for quiz attempt operations
    """

    @staticmethod
    async def start_attempt(attempt: AttemptCreate, current_user: dict) -> AttemptResponse:
        """
        Start a new quiz attempt
        """
        logger.info(f"Starting attempt for quiz: {attempt.quiz_id}")
    
        quiz_object_id = validate_object_id(
            attempt.quiz_id,
            QuizMessage.INVALID_ID,
        )
    
        quiz = await QuizRepository.get_quiz_by_id(quiz_object_id)
    
        if not quiz:
            logger.warning(QuizMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizMessage.NOT_FOUND)
    
        questions = await QuestionRepository.get_questions_by_quiz(quiz_object_id)
    
        if not questions:
            logger.warning(QuestionMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuestionMessage.NOT_FOUND)
        
        total_question_marks = sum(question["marks"] for question in questions)

        if total_question_marks != quiz["total_marks"]:
            logger.warning(QuizAttemptMessage.QUIZ_NOT_READY)
            raise BadRequestException(QuizAttemptMessage.QUIZ_NOT_READY)
    
        student_object_id = ObjectId(current_user["user_id"])
    
        active_attempt = await QuizAttemptRepository.get_in_progress_attempt(
            student_object_id,
            quiz_object_id,
        )
    
        if active_attempt:
            logger.warning(QuizAttemptMessage.ATTEMPT_ALREADY_IN_PROGRESS)
            raise BadRequestException(QuizAttemptMessage.ATTEMPT_ALREADY_IN_PROGRESS)
    
        previous_attempts = await QuizAttemptRepository.get_student_attempts(
            student_object_id,
            quiz_object_id,
        )
    
        if len(previous_attempts) >= quiz["max_attempts"]:
            logger.warning(QuizAttemptMessage.MAX_ATTEMPTS_REACHED)
            raise BadRequestException(QuizAttemptMessage.MAX_ATTEMPTS_REACHED)
    
        snapshot = create_quiz_snapshot(quiz, questions)
    
        attempt_data = {
            "student_id": student_object_id,
            "quiz_id": quiz_object_id,
            "attempt_number": len(previous_attempts) + 1,
            "status": QuizAttemptStatus.IN_PROGRESS,
            "started_at": datetime.now(),
            "submitted_at": None,
            "answers": [],
            "snapshot": snapshot.model_dump(),
        }
    
        result = await QuizAttemptRepository.create_attempt(attempt_data)

        created_attempt = await QuizAttemptRepository.get_attempt_by_id(result.inserted_id)
    
        logger.info("Quiz attempt started successfully")
        response = attempt_helper(created_attempt)
    
        return response
    


    @staticmethod
    async def get_attempt_questions(attempt_id: str, index: int, current_user: dict) -> AttemptQuestionResponse:
        """
        Get questions for an attempt
        """
        logger.info(f"Getting questions for attempt: {attempt_id}")
    
        object_id = validate_object_id(attempt_id, QuizAttemptMessage.INVALID_ID)
    
        attempt = await QuizAttemptRepository.get_attempt_by_id(object_id)

        if not attempt:
            logger.warning(QuizAttemptMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizAttemptMessage.NOT_FOUND)
    
        if attempt["student_id"] != ObjectId(current_user["user_id"]):
            logger.warning(QuizAttemptMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizAttemptMessage.NOT_FOUND)   

        time_spend = (datetime.now() - attempt["started_at"]).total_seconds()
        time_remaining = max(attempt["snapshot"]["duration"] * 60 - int(time_spend), 0)     

        if (attempt["status"] == QuizAttemptStatus.IN_PROGRESS and check_attempt_time_expired(attempt)):
            await auto_submit_attempt(object_id, attempt)
            logger.warning(QuizAttemptMessage.TIME_EXPIRED)
            raise BadRequestException(QuizAttemptMessage.TIME_EXPIRED)
        
        answer_map = {
            answer["question_id"]: answer["selected_option"]
            for answer in attempt["answers"]
        }

        questions = attempt["snapshot"]["questions"]

        if index < 0 or index >= len(questions):
            logger.warning(QuizAttemptMessage.INVALID_QUESTION_INDEX)
            raise BadRequestException(QuizAttemptMessage.INVALID_QUESTION_INDEX)
        
        question = questions[index]
    
        response = AttemptQuestionResponse(
            question_number = index + 1,
            total_questions = len(questions),
            id = question["question_id"],
            question = question["question"],
            question_type = question["question_type"],
            options = question["options"],
            difficulty = question["difficulty"],
            marks = question["marks"],
            selected_option = answer_map.get(question["question_id"]),
            time_remaining=time_remaining,
        )
    
        logger.info("Attempt question retrieved successfully")
    
        return response
    


    @staticmethod
    async def save_answer(attempt_id: str, answer: StudentAnswer, current_user: dict) -> MessageResponse:
        """
        Save or update a student's answer
        """
        logger.info(f"Saving answer for attempt: {attempt_id}")
    
        object_id = validate_object_id(
            attempt_id,
            QuizAttemptMessage.INVALID_ID,
        )
    
        attempt = await QuizAttemptRepository.get_attempt_by_id(object_id)

        if not attempt:
            logger.warning(QuizAttemptMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizAttemptMessage.NOT_FOUND)
    
        if attempt["student_id"] != ObjectId(current_user["user_id"]):
            logger.warning(QuizAttemptMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizAttemptMessage.NOT_FOUND)

        if (attempt["status"] == QuizAttemptStatus.IN_PROGRESS 
            and check_attempt_time_expired(attempt)
            ):
            await auto_submit_attempt(object_id, attempt)
            logger.warning(QuizAttemptMessage.TIME_EXPIRED)
            raise BadRequestException(QuizAttemptMessage.TIME_EXPIRED)
    
        if attempt["status"] != QuizAttemptStatus.IN_PROGRESS:
            logger.warning(QuizAttemptMessage.ATTEMPT_ALREADY_SUBMITTED)
            raise BadRequestException(QuizAttemptMessage.ATTEMPT_ALREADY_SUBMITTED)

        question = None

        for snapshot_question in attempt["snapshot"]["questions"]:
            if snapshot_question["question_id"] == answer.question_id:
                question = snapshot_question
                break
    
        if not question:
            logger.warning(QuestionMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuestionMessage.NOT_FOUND)
    
        if answer.selected_option > len(question["options"]):
            logger.warning(QuestionMessage.INVALID_CORRECT_ANSWER)
            raise BadRequestException(QuestionMessage.INVALID_CORRECT_ANSWER)
    
        answers = attempt["answers"]
    
        existing_answer = None

        for student_answer in answers:
            if student_answer["question_id"] == answer.question_id:
                existing_answer = student_answer
                break
    
        if existing_answer:
            existing_answer["selected_option"] = answer.selected_option
        else:
            answers.append(answer.model_dump())
    
        await QuizAttemptRepository.update_attempt(object_id, {"answers": answers})
    
        logger.info("Answer saved successfully")
        response =  MessageResponse(message=QuizAttemptMessage.ANSWER_SAVED)

        return response
    


    @staticmethod
    async def submit_attempt(attempt_id: str, current_user: dict) -> MessageResponse:
        """
        Submit a quiz attempt
        """
        logger.info(f"Submitting attempt: {attempt_id}")
    
        object_id = validate_object_id(
            attempt_id,
            QuizAttemptMessage.INVALID_ID,
        )
    
        attempt = await QuizAttemptRepository.get_attempt_by_id(object_id)

        if not attempt:
            logger.warning(QuizAttemptMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizAttemptMessage.NOT_FOUND)
    
        if attempt["student_id"] != ObjectId(current_user["user_id"]):
            logger.warning(QuizAttemptMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizAttemptMessage.NOT_FOUND)
        
        if (attempt["status"] == QuizAttemptStatus.IN_PROGRESS 
            and check_attempt_time_expired(attempt)
            ):
            await auto_submit_attempt(object_id, attempt)
            logger.warning(QuizAttemptMessage.TIME_EXPIRED)
            raise BadRequestException(QuizAttemptMessage.TIME_EXPIRED)
    
        if attempt["status"] != QuizAttemptStatus.IN_PROGRESS:
            logger.warning(QuizAttemptMessage.ATTEMPT_ALREADY_SUBMITTED)
            raise BadRequestException(QuizAttemptMessage.ATTEMPT_ALREADY_SUBMITTED)
    
        score = calculate_score(attempt)
    
        update_data = {
            "status": QuizAttemptStatus.SUBMITTED,
            "submitted_at": datetime.now(),
            "score": score,
        }
    
        await QuizAttemptRepository.update_attempt(
            object_id,
            update_data,
        )
    
        logger.info("Quiz submitted successfully")
        response =  MessageResponse(message=QuizAttemptMessage.SUBMITTED)

        return response
    


    @staticmethod
    async def get_student_attempts(quiz_id: str, current_user: dict) -> list[AttemptResponse]:
        """
        Get all attempts of a student for a quiz
        """
        logger.info(f"Getting attempts for quiz: {quiz_id}")
    
        quiz_object_id = validate_object_id(
            quiz_id,
            QuizMessage.INVALID_ID,
        )
    
        quiz = await QuizRepository.get_quiz_by_id(quiz_object_id)
    
        if not quiz:
            logger.warning(QuizMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizMessage.NOT_FOUND)
    
        student_object_id = ObjectId(current_user["user_id"])
    
        attempts = await QuizAttemptRepository.get_student_attempts(
            student_object_id,
            quiz_object_id,
        )

        for attempt in attempts:
            if(attempt["status"] == QuizAttemptStatus.IN_PROGRESS
               and check_attempt_time_expired(attempt)
               ):
                await auto_submit_attempt(attempt["_id"], attempt)

        attempts = await QuizAttemptRepository.get_student_attempts(
        student_object_id,
        quiz_object_id,
        )     
    
        logger.info("Attempts retrieved successfully")
    
        response = [attempt_helper(attempt) for attempt in attempts]
    
        return response
    


    @staticmethod
    async def get_student_all_attempts(current_user: dict) -> list[AttemptResponse]:
        """
        Get all attempts of a student across all quiz
        """
        logger.info("Getting all student attempts")
        student_object_id = ObjectId(current_user["user_id"])
    
        attempts = await QuizAttemptRepository.get_student_all_attempts(student_object_id)
    
        for attempt in attempts:
            if (attempt["status"] == QuizAttemptStatus.IN_PROGRESS
                and check_attempt_time_expired(attempt)
                ):
                await auto_submit_attempt(attempt["_id"], attempt)
    
        attempts = await QuizAttemptRepository.get_student_all_attempts(student_object_id)
    
        logger.info("Student attempts retrieved successfully")
        response = [attempt_helper(attempt) for attempt in attempts]

        return response
