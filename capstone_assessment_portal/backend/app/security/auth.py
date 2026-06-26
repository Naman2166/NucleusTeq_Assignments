"""
Provides methods for validating tokens and checking user access
"""

from fastapi import Depends, HTTPException, status
from fastapi.security import HTTPAuthorizationCredentials, HTTPBearer
from app.security.jwt_handler import verify_token
from app.utils.constants import ADMIN, STUDENT
from app.utils.logger import logger

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
        raise HTTPException(status.HTTP_401_UNAUTHORIZED, "Invalid or expired token")



def require_admin(user = Depends(get_current_user)):
    """
    Allow access to users with ADMIN role
    """
    logger.info(f"Checking admin access for {user['email']}")

    if user.get("role") != ADMIN:
        raise HTTPException(status.HTTP_403_FORBIDDEN, "Admin access required")
    
    logger.info(f"Admin access granted for {user['email']}")
    
    return user



def require_student(user = Depends(get_current_user)):
    """
    Allow access to users with STUDENT role
    """
    logger.info(f"Checking student access for {user['email']}")
    
    if user.get("role") != STUDENT:
        raise HTTPException(status.HTTP_403_FORBIDDEN, "Student access required")
    
    logger.info(f"Student access granted for {user['email']}")
    
    return user