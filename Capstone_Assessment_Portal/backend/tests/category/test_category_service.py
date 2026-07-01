"""
Test cases for CategoryService
"""

import pytest
from unittest.mock import AsyncMock, MagicMock
from bson import ObjectId
from app.services.category_service import CategoryService
from app.schemas.category_schema import CategoryCreate, CategoryUpdate
from app.exceptions.custom_exceptions import (ConflictException, ResourceNotFoundException)
from app.utils.constants import CategoryMessage



@pytest.mark.asyncio
async def test_get_all_categories(mocker):
    """
    Test get all categories
    """

    mocker.patch(
        "app.services.category_service.CategoryRepository.get_all_categories",
        new_callable=AsyncMock,
        return_value=[{
                "_id": ObjectId(),
                "name": "Programming",
                "description": "Programming quizzes",
            }],
    )

    response = await CategoryService.get_all_categories()

    assert len(response) == 1
    assert response[0].name == "Programming"



@pytest.mark.asyncio
async def test_get_category_by_id_success(mocker):
    """
    Test get category by id
    """

    category_id = str(ObjectId())

    mocker.patch(
        "app.services.category_service.CategoryRepository.get_category_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(category_id),
            "name": "Programming",
            "description": "Programming quizzes",
        },
    )

    response = await CategoryService.get_category_by_id(category_id)

    assert response.id == category_id
    assert response.name == "Programming"



@pytest.mark.asyncio
async def test_create_category_success(mocker):
    """
    Test successful category creation
    """

    mocker.patch(
        "app.services.category_service.CategoryRepository.get_category_by_name",
        new_callable=AsyncMock,
        return_value=None,
    )

    inserted_result = MagicMock()
    inserted_result.inserted_id = ObjectId()

    mock_create = mocker.patch(
        "app.services.category_service.CategoryRepository.create_category",
        new_callable=AsyncMock,
        return_value=inserted_result,
    )

    mocker.patch(
        "app.services.category_service.CategoryRepository.get_category_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": inserted_result.inserted_id,
            "name": "Programming",
            "description": "Programming quizzes",
        },
    )

    category = CategoryCreate(
        name="Programming",
        description="Programming quizzes",
    )

    response = await CategoryService.create_category(category)

    assert response.name == "Programming"

    mock_create.assert_awaited_once()



@pytest.mark.asyncio
async def test_create_category_duplicate(mocker):
    """
    Test duplicate category creation
    """

    mocker.patch(
        "app.services.category_service.CategoryRepository.get_category_by_name",
        new_callable=AsyncMock,
        return_value={"name": "Programming"},
    )

    category = CategoryCreate(
        name="Programming",
        description="Programming quizzes",
    )

    with pytest.raises(ConflictException):
        await CategoryService.create_category(category)



@pytest.mark.asyncio
async def test_update_category_success(mocker):
    """
    Test successful category update
    """

    category_id = str(ObjectId())

    mocker.patch(
        "app.services.category_service.CategoryRepository.get_category_by_id",
        new_callable=AsyncMock,
        side_effect=[
            {
                "_id": ObjectId(category_id),
                "name": "Programming",
                "description": "Old Description",
            },
            {
                "_id": ObjectId(category_id),
                "name": "Java",
                "description": "Java quizzes",
            },
        ],
    )

    mocker.patch(
        "app.services.category_service.CategoryRepository.get_duplicate_category",
        new_callable=AsyncMock,
        return_value=None,
    )

    mock_update = mocker.patch(
        "app.services.category_service.CategoryRepository.update_category",
        new_callable=AsyncMock,
    )

    category = CategoryUpdate(
        name="Java",
        description="Java quizzes",
    )

    response = await CategoryService.update_category(
        category_id,
        category,
    )

    assert response.name == "Java"

    mock_update.assert_awaited_once()



@pytest.mark.asyncio
async def test_update_category_not_found(mocker):
    """
    Test update category when category does not exist
    """

    mocker.patch(
        "app.services.category_service.CategoryRepository.get_category_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    category = CategoryUpdate(name="Java")

    with pytest.raises(ResourceNotFoundException):
        await CategoryService.update_category(str(ObjectId()), category)



@pytest.mark.asyncio
async def test_delete_category_success(mocker):
    """
    Test successful category deletion
    """

    mocker.patch(
        "app.services.category_service.CategoryRepository.get_category_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(),
            "name": "Programming",
        },
    )

    mock_delete = mocker.patch(
        "app.services.category_service.CategoryRepository.delete_category",
        new_callable=AsyncMock,
    )

    response = await CategoryService.delete_category(
        str(ObjectId())
    )

    assert response.message == CategoryMessage.DELETED

    mock_delete.assert_awaited_once()



@pytest.mark.asyncio
async def test_delete_category_not_found(mocker):
    """
    Test delete category when category does not exist
    """

    mocker.patch(
        "app.services.category_service.CategoryRepository.get_category_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    with pytest.raises(ResourceNotFoundException):
        await CategoryService.delete_category(
            str(ObjectId())
        )