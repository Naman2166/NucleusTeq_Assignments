"""
Question routes
"""

from fastapi import APIRouter, status, Depends
from app.schemas.question_schema import (QuestionCreate, QuestionUpdate)
from app.services.question_service import QuestionService
from app.security.auth import require_admin, require_student
from app.utils.logger import logger


router = APIRouter(
    prefix="/questions",
    tags=["Question"],
)


@router.post("/", status_code=status.HTTP_201_CREATED)
async def create_question(
    question: QuestionCreate,
    current_user: dict = Depends(require_admin)
):
    """
    Create a new question
    """
    logger.info(f"Create question requested by Admin {current_user['email']}")
    response = await QuestionService.create_question(question)
    return response


@router.get("/admin/{question_id}", status_code=status.HTTP_200_OK)
async def get_question_by_id_admin(
    question_id: str,
    current_user: dict = Depends(require_admin)
):
    """
    Get a question by its ID for admin
    """
    logger.info(f"Admin {current_user['email']} requested question {question_id}")
    response = await QuestionService.get_question_by_id_admin(question_id)
    return response


@router.get("/student/{question_id}", status_code=status.HTTP_200_OK)
async def get_question_by_id_student(
    question_id: str,
    current_user: dict = Depends(require_student),
):
    """
    Get a question by its ID for student
    """
    logger.info(f"Student {current_user['email']} requested question {question_id}")
    response = await QuestionService.get_question_by_id_student(question_id)
    return response


@router.get("/admin/quiz/{quiz_id}", status_code=status.HTTP_200_OK)
async def get_questions_by_quiz_admin(
    quiz_id: str,
    current_user: dict = Depends(require_admin)
):
    """
    Get all questions for a quiz for admin
    """
    logger.info(f"Admin {current_user['email']} requested questions for quiz {quiz_id}")
    response = await QuestionService.get_questions_by_quiz_admin(quiz_id)
    return response


@router.get("/student/quiz/{quiz_id}", status_code=status.HTTP_200_OK)
async def get_questions_by_quiz_student(
    quiz_id: str,
    current_user: dict = Depends(require_student),
):
    """
    Get all questions for a quiz for student
    """
    logger.info(f"Student {current_user['email']} requested questions for quiz {quiz_id}")
    response = await QuestionService.get_questions_by_quiz_student(quiz_id)
    return response


@router.put("/{question_id}", status_code=status.HTTP_200_OK)
async def update_question(
    question_id: str,
    question: QuestionUpdate,
    current_user: dict = Depends(require_admin)
):
    """
    Update a question
    """
    logger.info(f"Update question {question_id} requested by Admin {current_user['email']}")
    response = await QuestionService.update_question(question_id, question)
    return response


@router.delete("/{question_id}", status_code=status.HTTP_200_OK)
async def delete_question(
    question_id: str,
    current_user: dict = Depends(require_admin)    
):
    """
    Delete a question
    """
    logger.info(f"Delete question {question_id} requested by Admin {current_user['email']}")
    response = await QuestionService.delete_question(question_id)
    return response