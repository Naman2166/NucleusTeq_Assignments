"""
Test cases for QuizService
"""

import pytest
from bson import ObjectId
from unittest.mock import AsyncMock, MagicMock
from app.services.quiz_service import QuizService, quiz_helper
from app.schemas.quiz_schema import QuizCreate, QuizUpdate
from app.exceptions.custom_exceptions import (BadRequestException, ResourceNotFoundException, ConflictException)
from app.utils.constants import QuizMessage


@pytest.mark.asyncio
async def test_create_quiz_success(mocker):
    """
    Test successful quiz creation
    """

    category_id = ObjectId()

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_category_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": category_id,
            "name": "Python",
        },
    )

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_quiz_by_title_and_category",
        new_callable=AsyncMock,
        return_value=None,
    )

    mocker.patch(
    "app.services.quiz_service.QuizRepository.create_quiz",
    new_callable=AsyncMock,
    return_value=MagicMock(inserted_id=ObjectId()),
)

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(),
            "title": "Python Quiz",
            "description": "Basic Python Quiz",
            "category_id": category_id,
            "duration": 30,
            "total_marks": 100,
            "passing_marks": 40,
            "max_attempts": 2,
        },
    )

    quiz = QuizCreate(
        title="Python Quiz",
        description="Basic Python Quiz",
        category_id=str(category_id),
        duration=30,
        total_marks=100,
        passing_marks=40,
        max_attempts=2,
    )

    response = await QuizService.create_quiz(quiz)

    assert response.title == "Python Quiz"



@pytest.mark.asyncio
async def test_create_quiz_category_not_found(mocker):
    """
    Test quiz creation with missing category
    """

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_category_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    quiz = QuizCreate(
        title="Python Quiz",
        description="Basic Python Quiz",
        category_id=str(ObjectId()),
        duration=30,
        total_marks=100,
        passing_marks=40,
        max_attempts=2,
    )

    with pytest.raises(ResourceNotFoundException):
        await QuizService.create_quiz(quiz)



@pytest.mark.asyncio
async def test_create_quiz_invalid_passing_marks(mocker):
    """
    Test quiz creation with invalid passing marks
    """

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_category_by_id",
        new_callable=AsyncMock,
        return_value={"_id": ObjectId()},
    )

    quiz = QuizCreate(
        title="Python Quiz",
        description="Basic Python Quiz",
        category_id=str(ObjectId()),
        duration=30,
        total_marks=50,
        passing_marks=60,
        max_attempts=2,
    )

    with pytest.raises(BadRequestException):
        await QuizService.create_quiz(quiz)



@pytest.mark.asyncio
async def test_get_all_quizzes(mocker):
    """
    Test get all quizzes
    """

    category_id = ObjectId()

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_all_quizzes",
        new_callable=AsyncMock,
        return_value=[
            {
                "_id": ObjectId(),
                "title": "Quiz 1",
                "description": "Description",
                "category_id": category_id,
                "duration": 30,
                "total_marks": 100,
                "passing_marks": 40,
                "max_attempts": 2,
            }
        ],
    )

    response = await QuizService.get_all_quizzes()

    assert len(response) == 1
    assert response[0].title == "Quiz 1"



@pytest.mark.asyncio
async def test_get_quiz_by_id_success(mocker):
    """
    Test get quiz by id
    """

    category_id = ObjectId()

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(),
            "title": "Python Quiz",
            "description": "Description",
            "category_id": category_id,
            "duration": 30,
            "total_marks": 100,
            "passing_marks": 40,
            "max_attempts": 2,
        },
    )

    response = await QuizService.get_quiz_by_id(str(ObjectId()))

    assert response.title == "Python Quiz"



@pytest.mark.asyncio
async def test_get_quiz_by_id_not_found(mocker):
    """
    Test get quiz by unknown id
    """

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    with pytest.raises(ResourceNotFoundException):
        await QuizService.get_quiz_by_id(str(ObjectId()))



@pytest.mark.asyncio
async def test_get_quiz_by_id_invalid_id():
    """
    Test get quiz with invalid id
    """

    with pytest.raises(BadRequestException):
        await QuizService.get_quiz_by_id("invalid")



@pytest.mark.asyncio
async def test_get_quizzes_by_category_invalid_id():
    """
    Test get quizzes with invalid category id
    """

    with pytest.raises(BadRequestException):
        await QuizService.get_quizzes_by_category("invalid")



@pytest.mark.asyncio
async def test_get_quizzes_by_category_success(mocker):
    """
    Test get quizzes by category
    """

    category_id = ObjectId()

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_category_by_id",
        new_callable=AsyncMock,
        return_value={"_id": category_id},
    )

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_quizzes_by_category",
        new_callable=AsyncMock,
        return_value=[
            {
                "_id": ObjectId(),
                "title": "Quiz 1",
                "description": "Description",
                "category_id": category_id,
                "duration": 30,
                "total_marks": 100,
                "passing_marks": 40,
                "max_attempts": 2,
            }
        ],
    )

    response = await QuizService.get_quizzes_by_category(str(category_id))

    assert len(response) == 1
    assert response[0].title == "Quiz 1"



@pytest.mark.asyncio
async def test_update_quiz_success(mocker):
    """
    Test successful quiz update
    """

    quiz_id = ObjectId()
    category_id = ObjectId()

    existing_quiz = {
        "_id": quiz_id,
        "title": "Python Quiz",
        "description": "Description",
        "category_id": category_id,
        "duration": 30,
        "total_marks": 100,
        "passing_marks": 40,
        "max_attempts": 2,
    }

    mock_get = mocker.patch(
        "app.services.quiz_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        side_effect=[existing_quiz, existing_quiz],
    )

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_duplicate_quiz",
        new_callable=AsyncMock,
        return_value=None,
    )

    mock_update = mocker.patch(
        "app.services.quiz_service.QuizRepository.update_quiz",
        new_callable=AsyncMock,
    )

    quiz = QuizUpdate(title="Updated Quiz")

    response = await QuizService.update_quiz(str(quiz_id), quiz)

    assert response.title == "Python Quiz"

    mock_get.assert_awaited()
    mock_update.assert_awaited_once()




@pytest.mark.asyncio
async def test_update_quiz_invalid_id():
    """
    Test update quiz with invalid id
    """

    with pytest.raises(BadRequestException):
        await QuizService.update_quiz(
            "invalid",
            QuizUpdate(title="Updated"),
        )



@pytest.mark.asyncio
async def test_update_quiz_not_found(mocker):
    """
    Test update unknown quiz
    """

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    with pytest.raises(ResourceNotFoundException):
        await QuizService.update_quiz(
            str(ObjectId()),
            QuizUpdate(title="Updated"),
        )



@pytest.mark.asyncio
async def test_update_quiz_duplicate(mocker):
    """
    Test update duplicate quiz
    """

    quiz_id = ObjectId()
    category_id = ObjectId()

    quiz = {
        "_id": quiz_id,
        "title": "Python Quiz",
        "description": "Description",
        "category_id": category_id,
        "duration": 30,
        "total_marks": 100,
        "passing_marks": 40,
        "max_attempts": 2,
    }

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value=quiz,
    )

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_duplicate_quiz",
        new_callable=AsyncMock,
        return_value={"_id": ObjectId()},
    )

    with pytest.raises(ConflictException):
        await QuizService.update_quiz(
            str(quiz_id),
            QuizUpdate(title="Updated"),
        )



@pytest.mark.asyncio
async def test_delete_quiz_success(mocker):
    """
    Test successful quiz deletion
    """

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value={"_id": ObjectId()},
    )

    mock_delete = mocker.patch(
        "app.services.quiz_service.QuizRepository.delete_quiz",
        new_callable=AsyncMock,
    )

    response = await QuizService.delete_quiz(str(ObjectId()))

    assert response.message == QuizMessage.DELETED

    mock_delete.assert_awaited_once()



@pytest.mark.asyncio
async def test_delete_quiz_not_found(mocker):
    """
    Test delete quiz with unknown id
    """

    mocker.patch(
        "app.services.quiz_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    with pytest.raises(ResourceNotFoundException):
        await QuizService.delete_quiz(str(ObjectId()))



@pytest.mark.asyncio
async def test_delete_quiz_invalid_id():
    """
    Test delete quiz with invalid id
    """

    with pytest.raises(BadRequestException):
        await QuizService.delete_quiz("invalid")
