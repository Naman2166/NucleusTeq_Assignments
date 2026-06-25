"""
Functions for creating and verifying JWT tokens
"""

from datetime import datetime, timedelta, timezone
from jose import jwt
from dotenv import load_dotenv
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

    return token



def create_refresh_token(existing_user: dict) -> str:
    """
    Create JWT refresh token
    """

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

    return token



def verify_token(token: str):
    """
    Verify token
    """
    payload  = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])

    return payload