"""
Authentication routes
"""

from fastapi import APIRouter, Depends, HTTPException, status
from app.schemas.user_schema import (UserRegister, UserLogin)
from app.services.auth_service import AuthService
from app.security.auth import get_current_user, require_admin, require_student


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
        return await AuthService.register(user)
    
    except ValueError as error:
        raise HTTPException(status_code=400, detail=str(error))


@router.post("/login")
async def login(user: UserLogin):
    """
    Login user
    """
    try:
        return await AuthService.login(user)

    except ValueError as error:
        raise HTTPException(status_code=401, detail=str(error))
    


@router.get("/admin/dashboard")
async def admin_dashboard(current_user=Depends(require_admin)):
    """
    Test endpoint for admin
    """
    return {
        "message": "Welcome Admin",
         "user":current_user 
    }

@router.get("/student/dashboard")
async def student_dashboard(current_user=Depends(require_student)):
    """
    Test endpoint for student
    """
    return {
        "message": "Welcome Student",
        "user": current_user
    }