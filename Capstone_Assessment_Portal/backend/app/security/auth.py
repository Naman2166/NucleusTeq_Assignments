"""
Provides methods for validating tokens and checking user access
"""

from fastapi import Depends
from fastapi.security import HTTPAuthorizationCredentials, HTTPBearer
from app.security.jwt_handler import verify_token
from app.utils.logger import logger
from app.exceptions.custom_exceptions import ForbiddenException
from app.exceptions.custom_exceptions import UnauthorizedException
from app.utils.constants import (Role, ExceptionMessage)


# Extract token from request header
security = HTTPBearer()


def get_current_user(credentials: HTTPAuthorizationCredentials = Depends(security)):
    """
    Verify token and return current authenticated user
    """
    try:
        logger.info("Token verification started")

        token = credentials.credentials
        payload = verify_token(token)
        
        logger.info(f"Token verified successfully for {payload['email']}")

        return payload
    
    except Exception:
        logger.warning("Invalid or expired token received")
        raise UnauthorizedException(ExceptionMessage.INVALID_TOKEN)



def require_admin(user = Depends(get_current_user)):
    """
    Allow access to users with ADMIN role
    """
    logger.info(f"Checking admin access for {user['email']}")

    if user.get("role") != Role.ADMIN:
        raise ForbiddenException(ExceptionMessage.ADMIN_ACCESS_REQUIRED)
    
    logger.info(f"Admin access granted for {user['email']}")
    
    return user



def require_student(user = Depends(get_current_user)):
    """
    Allow access to users with STUDENT role
    """
    logger.info(f"Checking student access for {user['email']}")
    
    if user.get("role") != Role.STUDENT:
        raise ForbiddenException(ExceptionMessage.STUDENT_ACCESS_REQUIRED)
    
    logger.info(f"Student access granted for {user['email']}")
    
    return user