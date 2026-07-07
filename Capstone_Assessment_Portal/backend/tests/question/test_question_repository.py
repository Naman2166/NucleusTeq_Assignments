"""
Test cases for QuestionRepository
"""

import pytest
from unittest.mock import AsyncMock, MagicMock
from bson import ObjectId
from app.repositories.question_repository import QuestionRepository


@pytest.mark.asyncio
async def test_create_question(mocker):
    """
    Test create question
    """

    mock_questions = mocker.patch(
        "app.repositories.question_repository.db.questions"
    )

    mock_questions.insert_one = AsyncMock()

    question_data = {
        "question": "What is Python?",
        "marks": 5,
    }

    await QuestionRepository.create_question(question_data)

    mock_questions.insert_one.assert_awaited_once_with(question_data)



@pytest.mark.asyncio
async def test_get_question_by_id(mocker):
    """
    Test get question by id
    """

    mock_questions = mocker.patch(
        "app.repositories.question_repository.db.questions"
    )

    question_id = ObjectId()

    mock_questions.find_one = AsyncMock(
        return_value={
            "_id": question_id,
            "question": "What is Python?",
        }
    )

    response = await QuestionRepository.get_question_by_id(question_id)

    assert response["_id"] == question_id
    mock_questions.find_one.assert_awaited_once_with({"_id": question_id})



@pytest.mark.asyncio
async def test_get_questions_by_quiz(mocker):
    """
    Test get questions by quiz
    """

    mock_questions = mocker.patch(
        "app.repositories.question_repository.db.questions"
    )

    quiz_id = ObjectId()

    mock_question_cursor = MagicMock()
    mock_question_cursor.to_list = AsyncMock(
        return_value=[
            {
                "_id": ObjectId(),
                "quiz_id": quiz_id,
                "question": "Question 1",
            },
            {
                "_id": ObjectId(),
                "quiz_id": quiz_id,
                "question": "Question 2",
            },
        ]
    )

    mock_questions.find.return_value = mock_question_cursor

    response = await QuestionRepository.get_questions_by_quiz(quiz_id)

    assert len(response) == 2
    mock_questions.find.assert_called_once_with({"quiz_id": quiz_id})
    mock_question_cursor.to_list.assert_awaited_once_with(length=None)



@pytest.mark.asyncio
async def test_delete_questions_by_quiz(mocker):
    """
    Test delete questions by quiz
    """

    mock_questions = mocker.patch(
        "app.repositories.question_repository.db.questions"
    )

    mock_questions.delete_many = AsyncMock()

    quiz_id = ObjectId()

    await QuestionRepository.delete_questions_by_quiz(quiz_id)

    mock_questions.delete_many.assert_awaited_once_with({"quiz_id": quiz_id})



@pytest.mark.asyncio
async def test_update_question(mocker):
    """
    Test update question
    """

    mock_questions = mocker.patch(
        "app.repositories.question_repository.db.questions"
    )

    mock_questions.update_one = AsyncMock()

    question_id = ObjectId()

    update_data = {"marks": 10}

    await QuestionRepository.update_question(
        question_id,
        update_data,
    )

    mock_questions.update_one.assert_awaited_once_with(
        {"_id": question_id},
        {"$set": update_data},
    )



@pytest.mark.asyncio
async def test_delete_question(mocker):
    """
    Test delete question
    """

    mock_questions = mocker.patch(
        "app.repositories.question_repository.db.questions"
    )

    mock_questions.delete_one = AsyncMock()

    question_id = ObjectId()

    await QuestionRepository.delete_question(question_id)

    mock_questions.delete_one.assert_awaited_once_with({"_id": question_id})