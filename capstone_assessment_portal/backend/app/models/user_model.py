"""
User database model
"""

from pydantic import BaseModel, EmailStr
from utils.constants import STUDENT


class UserModel(BaseModel):
    """
    Represents user details for database storage
    """
    first_name: str
    last_name: str
    email: EmailStr
    password: str
    role: str = STUDENT