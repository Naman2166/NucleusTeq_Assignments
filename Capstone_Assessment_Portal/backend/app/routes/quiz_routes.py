"""
Quiz routes
"""

from fastapi import APIRouter, status, Depends
from app.schemas.quiz_schema import (QuizCreate, QuizUpdate)
from app.services.quiz_service import QuizService
from app.security.auth import get_current_user, require_admin

router = APIRouter(
    prefix="/quizzes",
    tags=["Quiz"]
)


@router.post("/", status_code=status.HTTP_201_CREATED)
async def create_quiz(
    quiz: QuizCreate,
    current_user: dict = Depends(require_admin)
):
    """
    Create a new quiz
    """
    response  = await QuizService.create_quiz(quiz)
    return response


@router.get("/", status_code=status.HTTP_200_OK)
async def get_all_quizzes(current_user: dict = Depends(get_current_user)):
    """
    Retrieve all quizzes.
    """
    response  = await QuizService.get_all_quizzes()
    return response


@router.get("/{quiz_id}", status_code=status.HTTP_200_OK)
async def get_quiz_by_id(
    quiz_id: str,
    current_user: dict = Depends(get_current_user)
):
    """
    Retrieve a quiz by its ID
    """
    response = await QuizService.get_quiz_by_id(quiz_id)
    return response


@router.get("/category/{category_id}")
async def get_quizzes_by_category(
    category_id: str,
    current_user: dict = Depends(get_current_user)
):
    """
    Retrieve all quizzes for a specific category
    """
    response = await QuizService.get_quizzes_by_category(category_id)
    return response


@router.put("/{quiz_id}", status_code=status.HTTP_200_OK,)
async def update_quiz(
    quiz_id: str,
    quiz: QuizUpdate,
    current_user: dict = Depends(require_admin)
):
    """
    Update an existing quiz
    """
    response = await QuizService.update_quiz(quiz_id, quiz)
    return response


@router.delete("/{quiz_id}", status_code=status.HTTP_200_OK,)
async def delete_quiz(
    quiz_id: str,
    current_user: dict = Depends(require_admin)
):
    """
    Delete a quiz
    """
    response = await QuizService.delete_quiz(quiz_id)
    return response