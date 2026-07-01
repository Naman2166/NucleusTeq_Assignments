"""
Category database operations
"""

from app.config.database import db
from bson import ObjectId


class CategoryRepository:
    """
    Repository for category database operations
    """

    @staticmethod
    async def get_all_categories():
        """
        Get all categories
        """
        categories = await db.categories.find().to_list(length=None)
        return categories


    @staticmethod
    async def get_category_by_name(name: str):
        """
        Get category by name
        """
        category = await db.categories.find_one({"name": name})
        return category


    @staticmethod
    async def create_category(category_data: dict):
        """
        Create a new category
        """
        result = await db.categories.insert_one(category_data)
        return result


    @staticmethod
    async def get_category_by_id(category_id: ObjectId):
        """
        Get category by id
        """
        category = await db.categories.find_one({"_id": category_id})
        return category


    @staticmethod
    async def get_duplicate_category(name: str, category_id: ObjectId):
        """
        Get duplicate category
        """
        category = await db.categories.find_one({
            "name": name,
            "_id": {"$ne": category_id}
        })
        return category


    @staticmethod
    async def update_category(category_id: ObjectId, update_data: dict):
        """
        Update category
        """
        result = await db.categories.update_one(
            {"_id": category_id},
            {"$set": update_data}
        )
        return result


    @staticmethod
    async def delete_category(category_id: ObjectId):
        """
        Delete category
        """
        result = await db.categories.delete_one({"_id": category_id})
        return result