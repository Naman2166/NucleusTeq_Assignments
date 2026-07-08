"""
Quiz Attempt routes
"""

from fastapi import APIRouter, status, Depends
from app.schemas.quiz_attempt_schema import (AttemptCreate, StudentAnswer)
from app.services.quiz_attempt_service import QuizAttemptService
from app.security.auth import require_student
from app.utils.logger import logger


router = APIRouter(
    prefix="/quiz-attempts",
    tags=["Quiz Attempt"],
)


@router.post("/", status_code=status.HTTP_201_CREATED)
async def start_attempt(
    attempt: AttemptCreate,
    current_user: dict = Depends(require_student)
):
    """
    Start a new quiz attempt
    """
    logger.info(f"Quiz attempt requested by Student {current_user['email']}")
    response = await QuizAttemptService.start_attempt(attempt, current_user)
    return response


@router.get("/all", status_code=status.HTTP_200_OK)
async def get_student_all_attempts(current_user: dict = Depends(require_student)):
    """
    Get all attempts of a logged-in student across all quiz
    """
    logger.info(f"{current_user['email']} requested all attempts")

    response =  await QuizAttemptService.get_student_all_attempts(current_user)
    return response


@router.get("/{attempt_id}/questions", status_code=status.HTTP_200_OK)
async def get_attempt_questions(
    attempt_id: str,
    current_user: dict = Depends(require_student)
):
    """
    Get questions for a quiz attempt
    """
    logger.info(f"{current_user['email']} requested questions for attempt {attempt_id}")
    response = await QuizAttemptService.get_attempt_questions(attempt_id, current_user)
    return response


@router.patch("/{attempt_id}/answer", status_code=status.HTTP_200_OK)
async def save_answer(
    attempt_id: str,
    answer: StudentAnswer,
    current_user: dict = Depends(require_student)
):
    """
    Save a student's single answer
    """
    logger.info(f"{current_user['email']} saved answer for attempt {attempt_id}")
    response = await QuizAttemptService.save_answer(attempt_id, answer, current_user)
    return response


@router.post("/{attempt_id}/submit", status_code=status.HTTP_200_OK)
async def submit_attempt(
    attempt_id: str,
    current_user: dict = Depends(require_student)
):
    """
    Submit a quiz attempt
    """
    logger.info(f"{current_user['email']} submitted attempt {attempt_id}")
    response = await QuizAttemptService.submit_attempt(attempt_id, current_user)
    return response


@router.get("/quiz/{quiz_id}", status_code=status.HTTP_200_OK)
async def get_student_attempts(
    quiz_id: str,
    current_user: dict = Depends(require_student)
):
    """
    Get all attempts of a student for a quiz
    """
    logger.info(f"{current_user['email']} requested attempts for quiz {quiz_id}")
    response = await QuizAttemptService.get_student_attempts(quiz_id, current_user)
    return response
