"""
User database operations
"""

from app.config.database import db


class UserRepository:
    """
    Repository for user database operations
    """

    @staticmethod
    async def get_user_by_email(email: str):
        """
        Get user by email
        """
        user =  await db.users.find_one({"email": email})
        return user
    

    @staticmethod
    async def create_user(user_data: dict):
        """
        Create a new user
        """
        result =  await db.users.insert_one(user_data)
        return result