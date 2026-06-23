"""
Provides methods for validating tokens and checking user access
"""

from fastapi import Depends, HTTPException, status
from fastapi.security import HTTPAuthorizationCredentials, HTTPBearer
from app.security.jwt_handler import verify_token
from app.utils.constants import ADMIN, STUDENT

# Extract token from request header
security = HTTPBearer()


def get_current_user(credentials: HTTPAuthorizationCredentials = Depends(security)):
    """
    Verify token and return current authenticated user
    """
    try:
        token = credentials.credentials
        payload = verify_token(token)
        return payload
    
    except Exception:
        raise HTTPException(status.HTTP_401_UNAUTHORIZED, "Invalid or expired token")


def require_admin(user = Depends(get_current_user)):
    """
    Allow access to users with ADMIN role
    """
    if user.get("role") != ADMIN:
        raise HTTPException(status.HTTP_403_FORBIDDEN, "Admin access required")
    
    return user


def require_student(user = Depends(get_current_user)):
    """
    Allow access to users with STUDENT role
    """
    if user.get("role") != STUDENT:
        raise HTTPException(status.HTTP_403_FORBIDDEN, "Student access required")
    
    return user