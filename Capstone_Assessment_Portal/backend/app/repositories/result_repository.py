"""
Result database operations
"""

from bson import ObjectId
from app.config.database import db
from app.utils.constants import QuizAttemptStatus


class ResultRepository:
    """
    Repository class for result database operations
    """

    @staticmethod
    async def get_result_by_attempt_id(attempt_id: ObjectId):
        """
        Get a result by attempt ID
        """
        result = await db.quiz_attempts.find_one({
            "_id": attempt_id,
            "status": {
               "$in": [QuizAttemptStatus.SUBMITTED, QuizAttemptStatus.TIME_EXPIRED]
            }
        })
        return result


    @staticmethod
    async def get_student_results(student_id: ObjectId):
        """
        Get all submitted results of a student
        """
        results_cursor = db.quiz_attempts.find({
            "student_id": student_id,
            "status": {
                "$in": [QuizAttemptStatus.SUBMITTED, QuizAttemptStatus.TIME_EXPIRED]
            }
        })

        results = await results_cursor.to_list(length=None)

        return results


    @staticmethod
    async def get_all_results():
        """
        Get all submitted quiz results
        """
        results_cursor = db.quiz_attempts.find({
            "status": {
                "$in": [QuizAttemptStatus.SUBMITTED, QuizAttemptStatus.TIME_EXPIRED]
            }
        })

        results = await results_cursor.to_list(length=None)

        return results