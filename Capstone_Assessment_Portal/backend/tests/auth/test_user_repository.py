"""
Test cases for UserRepository
"""

import pytest
from unittest.mock import AsyncMock
from app.repositories.user_repository import UserRepository


@pytest.mark.asyncio
async def test_get_user_by_email(mocker):
    """
    Test get user by email
    """

    mock_users = mocker.patch("app.repositories.user_repository.db.users")

    mock_users.find_one = AsyncMock(
        return_value={
            "_id": "1",
            "email": "naman@gmail.com",
            "password": "hashed_password",
            "role": "student",
        }
    )

    response = await UserRepository.get_user_by_email("naman@gmail.com")

    assert response["email"] == "naman@gmail.com"

    mock_users.find_one.assert_awaited_once_with({"email": "naman@gmail.com"})



@pytest.mark.asyncio
async def test_create_user(mocker):
    """
    Test create user
    """

    mock_users = mocker.patch("app.repositories.user_repository.db.users")

    mock_users.insert_one = AsyncMock()

    user_data = {
        "first_name": "Naman",
        "last_name": "Patel",
        "email": "naman@gmail.com",
        "password": "hashed_password",
        "role": "student",
    }

    await UserRepository.create_user(user_data)

    mock_users.insert_one.assert_awaited_once_with(user_data)