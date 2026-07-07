"""
Quiz Attempt database operations
"""

from bson import ObjectId
from app.config.database import db
from app.utils.constants import QuizAttemptStatus


class QuizAttemptRepository:
    """
    Repository class for quiz attempt database operations
    """

    @staticmethod
    async def create_attempt(attempt: dict):
        """
        Create a new quiz attempt
        """
        result = await db.quiz_attempts.insert_one(attempt)
        return result


    @staticmethod
    async def get_attempt_by_id(attempt_id: ObjectId):
        """
        Get a quiz attempt by its ID
        """
        attempt = await db.quiz_attempts.find_one({"_id": attempt_id})
        return attempt


    @staticmethod
    async def get_student_attempts(student_id: ObjectId, quiz_id: ObjectId):
        """
        Get all attempts of a student for a quiz
        """
        attempts_cursor = db.quiz_attempts.find({
            "student_id": student_id,
            "quiz_id": quiz_id
        })
        attempts = await attempts_cursor.to_list(length=None)
        return attempts


    @staticmethod
    async def get_in_progress_attempt(student_id: ObjectId, quiz_id: ObjectId):
        """
        Get student's in-progress attempt for a quiz
        """
        attempt = await db.quiz_attempts.find_one({
            "student_id": student_id,
            "quiz_id": quiz_id,
            "status": QuizAttemptStatus.IN_PROGRESS
        })
        return attempt


    @staticmethod
    async def update_attempt(attempt_id: ObjectId, update_data: dict):
        """
        Update a quiz attempt
        """
        result = await db.quiz_attempts.update_one(
            {"_id": attempt_id},
            {"$set": update_data}
        )
        return result