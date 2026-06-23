"""
Handles password hashing and verification
"""

from passlib.context import CryptContext


# Password hashing configuration using bcrypt
password_hasher = CryptContext(schemes=["bcrypt"])


def hash_password(password: str) -> str:
    """
    Generates hashed password
    """
    return password_hasher.hash(password)


def verify_password(plain_password: str, hashed_password: str) -> bool:
    """
    Verify plain password with hashed pasword 
    """
    return password_hasher.verify(plain_password, hashed_password)