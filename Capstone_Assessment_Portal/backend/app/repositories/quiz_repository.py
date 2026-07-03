"""
Quiz database operations
"""

from app.config.database import db
from bson import ObjectId


class QuizRepository:
    """
    Repository for quiz database operations
    """

    @staticmethod
    async def get_category_by_id(category_id: ObjectId):
        """
        Get category by id
        """
        category = await db.categories.find_one({"_id": category_id})
        return category


    @staticmethod
    async def get_quiz_by_title_and_category(title: str, category_id: ObjectId):
        """
        Get quiz by title and category
        """
        quiz = await db.quizzes.find_one({
            "title": title,
            "category_id": category_id
        })
        return quiz


    @staticmethod
    async def create_quiz(quiz_data: dict):
        """
        Create a new quiz
        """
        result = await db.quizzes.insert_one(quiz_data)
        return result


    @staticmethod
    async def get_quiz_by_id(quiz_id: ObjectId):
        """
        Get quiz by id
        """
        quiz = await db.quizzes.find_one({"_id": quiz_id})
        return quiz


    @staticmethod
    async def get_all_quizzes():
        """
        Get all quizzes
        """
        quizzes = await db.quizzes.find().to_list(length=None)
        return quizzes


    @staticmethod
    async def get_quizzes_by_category(category_id: ObjectId):
        """
        Get quizzes by category
        """
        quizzes = await db.quizzes.find({
            "category_id": category_id
        }).to_list(length=None)
        return quizzes


    @staticmethod
    async def get_duplicate_quiz(
        title: str,
        category_id: ObjectId,
        quiz_id: ObjectId
    ):
        """
        Get duplicate quiz
        """
        quiz = await db.quizzes.find_one({
            "title": title,
            "category_id": category_id,
            "_id": {"$ne": quiz_id}
        })
        return quiz


    @staticmethod
    async def update_quiz(quiz_id: ObjectId, update_data: dict):
        """
        Update quiz
        """
        result = await db.quizzes.update_one(
            {"_id": quiz_id},
            {"$set": update_data}
        )
        return result


    @staticmethod
    async def delete_quiz(quiz_id: ObjectId):
        """
        Delete quiz
        """
        result = await db.quizzes.delete_one({"_id": quiz_id})
        return result