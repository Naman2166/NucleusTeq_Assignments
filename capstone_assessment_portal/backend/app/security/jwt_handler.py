"""
Functions for creating and verifying JWT tokens
"""

from datetime import datetime, timedelta, timezone
from jose import jwt
from dotenv import load_dotenv
from utils.logger import logger
import os

load_dotenv()

SECRET_KEY = os.getenv("SECRET_KEY")
ALGORITHM = os.getenv("ALGORITHM")
ACCESS_TOKEN_EXPIRE_MINUTES = int(os.getenv("ACCESS_TOKEN_EXPIRE_MINUTES"))
REFRESH_TOKEN_EXPIRE_DAYS = int(os.getenv("REFRESH_TOKEN_EXPIRE_DAYS"))


def create_access_token(existing_user: dict) -> str:         
    """
    Create JWT Access token
    """   
    logger.info(f"Creating access token for {existing_user['email']}")

    # access token expire after 15 minutes
    expiry = datetime.now(timezone.utc) + timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)

    # creating payload
    payload = {
        "user_id": str(existing_user["_id"]),
        "email": existing_user["email"],
        "role": existing_user["role"],
        "exp": expiry,
        "type": "access"
    }     

    token = jwt.encode(payload, SECRET_KEY, algorithm=ALGORITHM)
    logger.info(f"Access token created for {existing_user['email']}")

    return token



def create_refresh_token(existing_user: dict) -> str:
    """
    Create JWT refresh token
    """
    logger.info(f"Creating refresh token for {existing_user['email']}")

    # Refresh token expires after 7 days
    expiry = datetime.now(timezone.utc) + timedelta(days=REFRESH_TOKEN_EXPIRE_DAYS)

    payload = {
        "user_id": str(existing_user["_id"]),
        "email": existing_user["email"],
        "role": existing_user["role"],
        "exp": expiry,
        "type": "refresh"
    }

    token = jwt.encode(payload, SECRET_KEY, algorithm=ALGORITHM)
    logger.info(f"Refresh token created for {existing_user['email']}")

    return token



def verify_token(token: str):
    """
    Verify token
    """
    logger.info("Verifying JWT token")
    payload  = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
    logger.info(f"JWT token verified for {payload['email']}")

    return payload