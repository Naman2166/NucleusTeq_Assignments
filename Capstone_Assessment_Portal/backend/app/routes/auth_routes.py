"""
Authentication routes
"""

from fastapi import APIRouter, Depends, status
from app.schemas.user_schema import (UserRegister, UserLogin, RefreshTokenRequest)
from app.services.auth_service import AuthService
from app.security.auth import require_admin, require_student
from app.utils.logger import logger


router = APIRouter(
    prefix="/auth",
    tags=["Authentication"]
)


@router.post("/register", status_code=status.HTTP_201_CREATED)
async def register(user: UserRegister):
    """
    Register a new user
    """
    logger.info(f"Registration request received for {user.email}")
    response =  await AuthService.register(user)
    return response 


@router.post("/login")
async def login(user: UserLogin):
    """
    Login user
    """
    logger.info(f"Login request received for {user.email}")
    response =  await AuthService.login(user)
    return response


@router.post("/refresh")
async def refresh_access_token(data: RefreshTokenRequest):
    """
    Regenerate access token
    """
    logger.info("Refresh token request received")
    response =  await AuthService.regenerate_access_token(data.refresh_token)
    return response


@router.get("/admin/dashboard")
async def admin_dashboard(current_user=Depends(require_admin)):
    """
    Test endpoint for admin
    """
    logger.info(f"Admin dashboard accessed by {current_user['email']}")
    response =  {"message": "Welcome Admin", "user":current_user}
    return response


@router.get("/student/dashboard")
async def student_dashboard(current_user=Depends(require_student)):
    """
    Test endpoint for student
    """
    logger.info(f"Student dashboard accessed by {current_user['email']}")
    response =  {"message": "Welcome Student", "user": current_user}
    return response


@router.get("/public-key")
async def get_public_key():
    """
    Get public key
    """
    logger.info("Public key request received")
    response = await AuthService.get_public_key()
    return response
