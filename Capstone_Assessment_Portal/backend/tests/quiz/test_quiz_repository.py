"""
Test cases for QuizRepository
"""

import pytest
from bson import ObjectId
from unittest.mock import AsyncMock, MagicMock
from app.repositories.quiz_repository import QuizRepository



@pytest.mark.asyncio
async def test_get_quiz_by_id(mocker):
    """
    Test get quiz by id
    """

    mock_quizzes = mocker.patch("app.repositories.quiz_repository.db.quizzes")

    quiz_id = ObjectId()

    quiz = {
        "_id": quiz_id,
        "title": "Python Quiz",
    }

    mock_quizzes.find_one = AsyncMock(return_value=quiz)

    response = await QuizRepository.get_quiz_by_id(quiz_id)

    assert response == quiz

    mock_quizzes.find_one.assert_awaited_once_with(
        {"_id": quiz_id}
    )



@pytest.mark.asyncio
async def test_get_category_by_id(mocker):
    """
    Test get category by id
    """

    mock_categories = mocker.patch("app.repositories.quiz_repository.db.categories")

    category = {
        "_id": ObjectId(),
        "name": "Python",
    }

    mock_categories.find_one = AsyncMock(return_value=category)

    response = await QuizRepository.get_category_by_id(category["_id"])

    assert response == category

    mock_categories.find_one.assert_awaited_once_with({"_id": category["_id"]})



@pytest.mark.asyncio
async def test_get_quizzes_by_category(mocker):
    """
    Test get quizzes by category
    """

    mock_quizzes = mocker.patch("app.repositories.quiz_repository.db.quizzes")

    category_id = ObjectId()

    quizzes = [
        {"title": "Quiz 1"},
        {"title": "Quiz 2"},
    ]

    cursor = MagicMock()
    cursor.to_list = AsyncMock(return_value=quizzes)

    mock_quizzes.find.return_value = cursor

    response = await QuizRepository.get_quizzes_by_category(category_id)

    assert response == quizzes

    mock_quizzes.find.assert_called_once_with(
        {"category_id": category_id}
    )



@pytest.mark.asyncio
async def test_create_quiz(mocker):
    """
    Test create quiz
    """

    mock_quizzes = mocker.patch("app.repositories.quiz_repository.db.quizzes")

    mock_quizzes.insert_one = AsyncMock()

    quiz_data = {
        "title": "Python Quiz",
        "category_id": ObjectId(),
    }

    await QuizRepository.create_quiz(quiz_data)

    mock_quizzes.insert_one.assert_awaited_once_with(quiz_data)



@pytest.mark.asyncio
async def test_get_all_quizzes(mocker):
    """
    Test get all quizzes
    """

    mock_quizzes = mocker.patch("app.repositories.quiz_repository.db.quizzes")

    quizzes = [
        {"title": "Quiz 1"},
        {"title": "Quiz 2"},
    ]

    cursor = MagicMock()
    cursor.to_list = AsyncMock(return_value=quizzes)

    mock_quizzes.find.return_value = cursor

    response = await QuizRepository.get_all_quizzes()

    assert response == quizzes

    mock_quizzes.find.assert_called_once_with()



@pytest.mark.asyncio
async def test_update_quiz(mocker):
    """
    Test update quiz
    """

    mock_quizzes = mocker.patch("app.repositories.quiz_repository.db.quizzes")

    mock_quizzes.update_one = AsyncMock()

    quiz_id = ObjectId()
    update_data = {"title": "Updated Quiz"}

    await QuizRepository.update_quiz(quiz_id, update_data)

    mock_quizzes.update_one.assert_awaited_once_with(
        {"_id": quiz_id},
        {"$set": update_data},
    )



@pytest.mark.asyncio
async def test_delete_quiz(mocker):
    """
    Test delete quiz
    """

    mock_quizzes = mocker.patch("app.repositories.quiz_repository.db.quizzes")

    mock_quizzes.delete_one = AsyncMock()

    quiz_id = ObjectId()

    await QuizRepository.delete_quiz(quiz_id)

    mock_quizzes.delete_one.assert_awaited_once_with({"_id": quiz_id})