"""
Result business logic
"""

from bson import ObjectId
from app.repositories.result_repository import ResultRepository
from app.schemas.result_schema import (ResultResponse, ResultQuestionResponse, AttemptHistoryResponse)
from app.exceptions.custom_exceptions import ResourceNotFoundException
from app.utils.constants import QuizAttemptMessage
from app.utils.helper import validate_object_id
from app.utils.logger import logger


def result_helper(attempt: dict) -> ResultResponse:
    """
    Helper to convert MongoDB document to ResultResponse
    """
    total_marks = attempt["snapshot"]["total_marks"]
    score = attempt["score"]
    passing_marks = attempt["snapshot"]["passing_marks"]

    answer_map = {
        answer["question_id"]: answer["selected_option"]
        for answer in attempt["answers"]
    }

    questions = []

    for question in attempt["snapshot"]["questions"]:
        selected_option = answer_map.get(question["question_id"])

        is_correct = selected_option == question["correct_answer"]

        obtained_marks = question["marks"] if is_correct else 0

        questions.append(
            ResultQuestionResponse(
                question=question["question"],
                options=question["options"],
                selected_option=selected_option,
                correct_answer=question["correct_answer"],
                marks=question["marks"],
                obtained_marks=obtained_marks,
                is_correct=is_correct,
            )
        )

    response = ResultResponse(
        attempt_id=str(attempt["_id"]),
        quiz_id=str(attempt["quiz_id"]),
        quiz_title=attempt["snapshot"]["title"],
        attempt_number=attempt["attempt_number"],
        score=score,
        total_marks=total_marks,
        percentage=(score / total_marks) * 100,
        passing_marks=passing_marks,
        is_pass=score >= passing_marks,
        started_at=attempt["started_at"],
        submitted_at=attempt["submitted_at"],
        questions=questions,
    )

    return response


def history_helper(attempt: dict) -> AttemptHistoryResponse:
    """
    Helper to convert MongoDB document to AttemptHistoryResponse
    """
    total_marks = attempt["snapshot"]["total_marks"]
    score = attempt["score"]

    response = AttemptHistoryResponse(
        attempt_id=str(attempt["_id"]),
        quiz_id=str(attempt["quiz_id"]),
        quiz_title=attempt["snapshot"]["title"],
        attempt_number=attempt["attempt_number"],
        score=score,
        total_marks=total_marks,
        percentage=(score / total_marks) * 100,
        is_pass=score >= attempt["snapshot"]["passing_marks"],
        submitted_at=attempt["submitted_at"],
    )

    return response


async def get_attempt(attempt_id: str) -> dict:
    """
    Get a submitted quiz attempt
    """
    object_id = validate_object_id(
        attempt_id,
        QuizAttemptMessage.INVALID_ID,
    )

    attempt = await ResultRepository.get_result_by_attempt_id(object_id)

    if not attempt:
        logger.warning(QuizAttemptMessage.NOT_FOUND)
        raise ResourceNotFoundException(QuizAttemptMessage.NOT_FOUND)

    return attempt



class ResultService:
    """
    Business logic for result operations
    """

    @staticmethod
    async def get_result(attempt_id: str, current_user: dict) -> ResultResponse:
        """
        Get result of a quiz attempt
        """
        logger.info(f"Getting result for attempt: {attempt_id}")

        attempt = await get_attempt(attempt_id)

        if attempt["student_id"] != ObjectId(current_user["user_id"]):
            logger.warning(QuizAttemptMessage.NOT_FOUND)
            raise ResourceNotFoundException(QuizAttemptMessage.NOT_FOUND)

        logger.info("Result retrieved successfully")
        response = result_helper(attempt)

        return response



    @staticmethod
    async def get_student_results(current_user: dict) -> list[AttemptHistoryResponse]:
        """
        Get all results of the logged-in student
        """
        logger.info("Getting student results")

        student_object_id = ObjectId(current_user["user_id"])

        attempts = await ResultRepository.get_student_results(student_object_id)

        logger.info("Student results retrieved successfully")

        response = [history_helper(attempt) for attempt in attempts]

        return response
    


    @staticmethod
    async def get_result_admin(attempt_id: str) -> ResultResponse:
        """
        Get result of any student's quiz attempt
        """
        logger.info(f"Getting result for attempt: {attempt_id}")
    
        attempt = await get_attempt(attempt_id)
    
        logger.info("Result retrieved successfully")
        response = result_helper(attempt)
    
        return response
    

    @staticmethod
    async def get_all_results() -> list[AttemptHistoryResponse]:
        """
        Get all quiz results
        """
        logger.info("Getting all results")
    
        attempts = await ResultRepository.get_all_results()
    
        logger.info("Results retrieved successfully")
    
        response = [
            history_helper(attempt)
            for attempt in attempts
        ]
    
        return response