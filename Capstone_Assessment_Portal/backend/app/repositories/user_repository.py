"""
User database operations
"""

from app.utils.constants import Role
from app.config.database import db
from bson import ObjectId


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
    

    @staticmethod
    async def get_user_by_id(user_id: ObjectId):
       """
       Get user by ID
       """
       response = await db.users.find_one({"_id": user_id})
       return response

    
    @staticmethod
    async def get_all_students():
        """
        Get all registered students
        """
        students_cursor = db.users.find({"role": Role.STUDENT},{"_id": 0})
        students = await students_cursor.to_list(length=None)    
        return students