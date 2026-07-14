"""
Question database operations
"""

from bson import ObjectId
from app.config.database import db


class QuestionRepository:
    """
    Repository class for question database operations
    """

    @staticmethod
    async def create_question(question: dict):
        """
        Create a new question
        """
        result = await db.questions.insert_one(question)
        return result


    @staticmethod
    async def get_question_by_id(question_id: ObjectId):
        """
        Get a question by its ID
        """
        question = await db.questions.find_one({"_id": question_id})
        return question


    @staticmethod
    async def get_questions_by_quiz(quiz_id: ObjectId):
        """
        Get all questions for a quiz
        """
        questions_cursor = db.questions.find({"quiz_id": quiz_id}).sort("_id", -1)
        questions = await questions_cursor.to_list(length=None)
        return questions
    

    @staticmethod
    async def delete_questions_by_quiz(quiz_id: ObjectId):
        """
        Delete all questions of a quiz
        """
        result = await db.questions.delete_many({"quiz_id": quiz_id})
        return result


    @staticmethod
    async def update_question(question_id: ObjectId, update_data: dict):
        """
        Update a question
        """
        result = await db.questions.update_one(
            {"_id": question_id},
            {"$set": update_data}
        )
        return result


    @staticmethod
    async def delete_question(question_id: ObjectId):
        """
        Delete a question
        """
        result = await db.questions.delete_one({"_id": question_id})
        return result