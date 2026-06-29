"""
Handles password hashing, verification and validation
"""

from passlib.context import CryptContext
from app.utils.logger import logger
from app.exceptions.custom_exceptions import BadRequestException
from app.utils.constants import ExceptionMessage
import re


PASSWORD_REGEX = r"^(?=.*[A-Za-z])(?=.*\d)(?=.*[@#$%]).*$"

# Password hashing configuration using bcrypt
password_hasher = CryptContext(schemes=["bcrypt"])


def hash_password(password: str) -> str:
    """
    Generates hashed password
    """
    logger.info("Hashing user password")
    return password_hasher.hash(password)


def verify_password(plain_password: str, hashed_password: str) -> bool:
    """
    Verify plain password with hashed pasword 
    """
    logger.info("Verifying user password")
    return password_hasher.verify(plain_password, hashed_password)


def validate_password(password: str) -> None:
    """
    Validate password length and format
    """
    if len(password) < 8 or len(password) > 30:
        raise BadRequestException(ExceptionMessage.PASSWORD_LENGTH_ERROR)

    if not re.match(PASSWORD_REGEX, password):
        raise BadRequestException(ExceptionMessage.PASSWORD_FORMAT_ERROR)