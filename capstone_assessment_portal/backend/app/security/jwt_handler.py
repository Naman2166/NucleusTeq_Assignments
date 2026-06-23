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


def create_token(existing_user: dict) -> str:         
    """
    Create JWT token
    """   

    # token expire after 2 hrs
    expiry = datetime.now(timezone.utc) + timedelta(hours=2)

    # creating payload
    payload = {
        "user_id": str(existing_user["_id"]),
        "email": existing_user["email"],
        "role": existing_user["role"],
        "exp": expiry
    }     

    token = jwt.encode(payload, SECRET_KEY, algorithm=ALGORITHM)

    return token



def verify_token(token: str):
    """
    Verify JWT token
    """
    payload  = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])

    return payload