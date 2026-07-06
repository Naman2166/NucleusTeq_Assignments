"""
Result routes
"""

from fastapi import APIRouter, status, Depends
from app.services.result_service import ResultService
from app.security.auth import require_admin, require_student
from app.utils.logger import logger


router = APIRouter(
    prefix="/results",
    tags=["Result"],
)


@router.get("/history", status_code=status.HTTP_200_OK)
async def get_student_results(current_user: dict = Depends(require_student)):
    """
    Get all results of the logged-in student
    """
    logger.info(f"{current_user['email']} requested result history")
    response = await ResultService.get_student_results(current_user)
    return response


@router.get("/admin", status_code=status.HTTP_200_OK)
async def get_all_results(
    current_user: dict = Depends(require_admin)
):
    """
    Get all quiz results
    """
    logger.info(f"Admin {current_user['email']} requested all results")
    response = await ResultService.get_all_results()
    return response


@router.get("/{attempt_id}", status_code=status.HTTP_200_OK)
async def get_result(
    attempt_id: str,
    current_user: dict = Depends(require_student)
):
    """
    Get result of a quiz attempt
    """
    logger.info(f"{current_user['email']} requested result for attempt {attempt_id}")
    response = await ResultService.get_result(attempt_id, current_user)
    return response



@router.get("/admin/{attempt_id}", status_code=status.HTTP_200_OK)
async def get_result_admin(
    attempt_id: str,
    current_user: dict = Depends(require_admin)
):
    """
    Get result of any student's attempt
    """
    logger.info(f"Admin {current_user['email']} requested result for attempt {attempt_id}")
    response = await ResultService.get_result_admin(attempt_id)
    return response
