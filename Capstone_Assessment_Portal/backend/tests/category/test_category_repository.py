"""
Test cases for CategoryRepository
"""

import pytest
from unittest.mock import AsyncMock, MagicMock
from bson import ObjectId
from app.repositories.category_repository import CategoryRepository


@pytest.mark.asyncio
async def test_get_all_categories(mocker):
    """
    Test get all categories
    """

    mock_categories = mocker.patch("app.repositories.category_repository.db.categories")

    mock_category_cursor = MagicMock()
    mock_category_cursor.to_list = AsyncMock(
        return_value=[{
                "_id": ObjectId(),
                "name": "Programming",
                "description": "Programming quizzes",
            }]
    )

    mock_categories.find.return_value = mock_category_cursor

    response = await CategoryRepository.get_all_categories()

    assert len(response) == 1

    mock_categories.find.assert_called_once()
    mock_category_cursor.to_list.assert_awaited_once_with(length=None)



@pytest.mark.asyncio
async def test_get_category_by_name(mocker):
    """
    Test get category by name
    """

    mock_categories = mocker.patch("app.repositories.category_repository.db.categories")

    mock_categories.find_one = AsyncMock(
        return_value={
            "_id": ObjectId(),
            "name": "Programming",
            "description": "Programming quizzes",
        }
    )

    response = await CategoryRepository.get_category_by_name("Programming")

    assert response["name"] == "Programming"

    mock_categories.find_one.assert_awaited_once_with(
        {"name": "Programming"}
    )



@pytest.mark.asyncio
async def test_create_category(mocker):
    """
    Test create category
    """

    mock_categories = mocker.patch("app.repositories.category_repository.db.categories")

    mock_categories.insert_one = AsyncMock()

    category_data = {
        "name": "Programming",
        "description": "Programming quizzes",
    }

    await CategoryRepository.create_category(category_data)

    mock_categories.insert_one.assert_awaited_once_with(category_data)



@pytest.mark.asyncio
async def test_get_category_by_id(mocker):
    """
    Test get category by id
    """

    mock_categories = mocker.patch("app.repositories.category_repository.db.categories")

    category_id = ObjectId()

    mock_categories.find_one = AsyncMock(
        return_value={
            "_id": category_id,
            "name": "Programming",
            "description": "Programming quizzes",
        }
    )

    response = await CategoryRepository.get_category_by_id(category_id)

    assert response["_id"] == category_id

    mock_categories.find_one.assert_awaited_once_with(
        {"_id": category_id}
    )



@pytest.mark.asyncio
async def test_get_duplicate_category(mocker):
    """
    Test get duplicate category
    """

    mock_categories = mocker.patch("app.repositories.category_repository.db.categories")

    category_id = ObjectId()

    mock_categories.find_one = AsyncMock(
        return_value={
            "_id": ObjectId(),
            "name": "Programming",
        }
    )

    await CategoryRepository.get_duplicate_category(
        "Programming",
        category_id,
    )

    mock_categories.find_one.assert_awaited_once_with({
            "name": "Programming",
            "_id": {"$ne": category_id},
        })



@pytest.mark.asyncio
async def test_update_category(mocker):
    """
    Test update category
    """

    mock_categories = mocker.patch("app.repositories.category_repository.db.categories")

    mock_categories.update_one = AsyncMock()

    category_id = ObjectId()

    update_data = {"name": "Java",}

    await CategoryRepository.update_category(
        category_id,
        update_data,
    )

    mock_categories.update_one.assert_awaited_once_with(
        {"_id": category_id},
        {"$set": update_data},
    )



@pytest.mark.asyncio
async def test_delete_category(mocker):
    """
    Test delete category
    """

    mock_categories = mocker.patch("app.repositories.category_repository.db.categories")

    mock_categories.delete_one = AsyncMock()

    category_id = ObjectId()

    await CategoryRepository.delete_category(category_id)

    mock_categories.delete_one.assert_awaited_once_with({"_id": category_id})