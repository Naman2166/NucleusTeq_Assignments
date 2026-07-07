"""
Test cases for QuizAttemptRepository
"""

import pytest
from unittest.mock import AsyncMock, MagicMock
from bson import ObjectId
from app.repositories.quiz_attempt_repository import QuizAttemptRepository
from app.utils.constants import QuizAttemptStatus


@pytest.mark.asyncio
async def test_create_attempt(mocker):
    """
    Test create attempt
    """

    mock_attempts = mocker.patch(
        "app.repositories.quiz_attempt_repository.db.quiz_attempts"
    )

    mock_attempts.insert_one = AsyncMock()

    attempt_data = {
        "student_id": ObjectId(),
        "quiz_id": ObjectId(),
    }

    await QuizAttemptRepository.create_attempt(attempt_data)

    mock_attempts.insert_one.assert_awaited_once_with(attempt_data)



@pytest.mark.asyncio
async def test_get_attempt_by_id(mocker):
    """
    Test get attempt by id
    """

    mock_attempts = mocker.patch(
        "app.repositories.quiz_attempt_repository.db.quiz_attempts"
    )

    attempt_id = ObjectId()

    mock_attempts.find_one = AsyncMock(
        return_value={
            "_id": attempt_id,
            "status": QuizAttemptStatus.IN_PROGRESS,
        }
    )

    response = await QuizAttemptRepository.get_attempt_by_id(attempt_id)

    assert response["_id"] == attempt_id
    mock_attempts.find_one.assert_awaited_once_with({"_id": attempt_id})



@pytest.mark.asyncio
async def test_get_student_attempts(mocker):
    """
    Test get student attempts
    """

    mock_attempts = mocker.patch(
        "app.repositories.quiz_attempt_repository.db.quiz_attempts"
    )

    student_id = ObjectId()
    quiz_id = ObjectId()

    mock_cursor = MagicMock()
    mock_cursor.to_list = AsyncMock(
        return_value=[
            {
                "_id": ObjectId(),
                "student_id": student_id,
                "quiz_id": quiz_id,
            },
            {
                "_id": ObjectId(),
                "student_id": student_id,
                "quiz_id": quiz_id,
            },
        ]
    )

    mock_attempts.find.return_value = mock_cursor

    response = await QuizAttemptRepository.get_student_attempts(student_id, quiz_id)

    assert len(response) == 2
    mock_attempts.find.assert_called_once_with(
        {"student_id": student_id, "quiz_id": quiz_id}
    )
    mock_cursor.to_list.assert_awaited_once_with(length=None)



@pytest.mark.asyncio
async def test_get_in_progress_attempt(mocker):
    """
    Test get in-progress attempt
    """

    mock_attempts = mocker.patch(
        "app.repositories.quiz_attempt_repository.db.quiz_attempts"
    )

    student_id = ObjectId()
    quiz_id = ObjectId()

    mock_attempts.find_one = AsyncMock(
        return_value={
            "_id": ObjectId(),
            "student_id": student_id,
            "quiz_id": quiz_id,
            "status": QuizAttemptStatus.IN_PROGRESS,
        }
    )

    response = await QuizAttemptRepository.get_in_progress_attempt(
        student_id,
        quiz_id,
    )

    assert response["status"] == QuizAttemptStatus.IN_PROGRESS

    mock_attempts.find_one.assert_awaited_once_with(
        {
            "student_id": student_id,
            "quiz_id": quiz_id,
            "status": QuizAttemptStatus.IN_PROGRESS,
        }
    )



@pytest.mark.asyncio
async def test_update_attempt(mocker):
    """
    Test update attempt
    """

    mock_attempts = mocker.patch(
        "app.repositories.quiz_attempt_repository.db.quiz_attempts"
    )

    mock_attempts.update_one = AsyncMock()
    attempt_id = ObjectId()
    update_data = {"status": QuizAttemptStatus.SUBMITTED}

    await QuizAttemptRepository.update_attempt(
        attempt_id,
        update_data,
    )

    mock_attempts.update_one.assert_awaited_once_with(
        {"_id": attempt_id},
        {"$set": update_data},
    )