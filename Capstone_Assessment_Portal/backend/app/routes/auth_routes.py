"""
Authentication routes
"""

from fastapi import APIRouter, Depends, status
from app.schemas.user_schema import (UserRegister, UserLogin, RefreshTokenRequest)
from app.services.auth_service import AuthService
from app.security.auth import require_admin, require_student
from app.utils.logger import logger
from app.exceptions.bad_request_exception import BadRequestException
from app.exceptions.unauthorized_exception import UnauthorizedException


router = APIRouter(
    prefix="/auth",
    tags=["Authentication"]
)


@router.post("/register", status_code=status.HTTP_201_CREATED)
async def register(user: UserRegister):
    """
    Register a new user
    """
    try:
        logger.info(f"Registration request received for {user.email}")
        return await AuthService.register(user)
    
    except ValueError as error:
        logger.error(f"Registration failed for {user.email}: {error}")
        raise BadRequestException(str(error))



@router.post("/login")
async def login(user: UserLogin):
    """
    Login user
    """
    try:
        logger.info(f"Login request received for {user.email}")
        return await AuthService.login(user)

    except ValueError as error:
        logger.error(f"Login failed for {user.email}: {error}")
        raise UnauthorizedException(str(error))



@router.post("/refresh")
async def refresh_access_token(data: RefreshTokenRequest):
    """
    Regenerate access token
    """
    try:
        logger.info("Refresh token request received")
        return await AuthService.regenerate_access_token(data.refresh_token)

    except ValueError as error:
        logger.error(f"Refresh token failed: {error}")
        raise UnauthorizedException(str(error))



@router.get("/admin/dashboard")
async def admin_dashboard(current_user=Depends(require_admin)):
    """
    Test endpoint for admin
    """
    logger.info(f"Admin dashboard accessed by {current_user['email']}")
    return {
        "message": "Welcome Admin",
         "user":current_user 
    }


@router.get("/student/dashboard")
async def student_dashboard(current_user=Depends(require_student)):
    """
    Test endpoint for student
    """
    logger.info(f"Student dashboard accessed by {current_user['email']}")
    return {
        "message": "Welcome Student",
        "user": current_user
    }


@router.get("/public-key")
async def get_public_key():
    """
    Get public key
    """
    logger.info("Public key request received")
    return await AuthService.get_public_key()
