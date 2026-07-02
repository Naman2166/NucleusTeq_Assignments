"""
Test cases for AuthService
"""

import pytest
from unittest.mock import AsyncMock
from app.services.auth_service import AuthService
from app.schemas.user_schema import UserRegister, UserLogin
from app.exceptions.custom_exceptions import (ConflictException, UnauthorizedException)
from app.utils.constants import AuthMessage



@pytest.mark.asyncio
async def test_register_success(mocker):
    """
    Test successful user registration
    """

    mock_get_user = mocker.patch(
        "app.services.auth_service.UserRepository.get_user_by_email",
        new_callable=AsyncMock,
        return_value=None
    )

    mock_create_user = mocker.patch(
        "app.services.auth_service.UserRepository.create_user",
        new_callable=AsyncMock
    )

    mocker.patch(
        "app.services.auth_service.decrypt_password",
        return_value="Password@123"
    )

    mocker.patch(
        "app.services.auth_service.validate_password"
    )

    mocker.patch(
        "app.services.auth_service.hash_password",
        return_value="hashed_password"
    )

    user = UserRegister(
        first_name="Naman",
        last_name="Patel",
        email="naman@gmail.com",
        password="encrypted_password",
    )

    response = await AuthService.register(user)

    assert response.message == AuthMessage.USER_REGISTERED_SUCCESSFULLY

    mock_get_user.assert_awaited_once()
    mock_create_user.assert_awaited_once()



@pytest.mark.asyncio
async def test_login_student_success(mocker):
    """
    Test successful login
    """

    mock_get_user = mocker.patch(
        "app.services.auth_service.UserRepository.get_user_by_email",
        new_callable=AsyncMock,
        return_value={
            "_id": "1",
            "email": "naman@gmail.com",
            "password": "hashed_password",
            "role": "student",
        }
    )

    mocker.patch(
        "app.services.auth_service.decrypt_password",
        return_value="Naman@123"
    )

    mocker.patch(
        "app.services.auth_service.verify_password",
        return_value=True
    )

    mocker.patch(
        "app.services.auth_service.create_access_token",
        return_value="access_token"
    )

    mocker.patch(
        "app.services.auth_service.create_refresh_token",
        return_value="refresh_token"
    )

    user = UserLogin(
        email="naman@gmail.com",
        password="encrypted_password",
    )

    response = await AuthService.login(user)

    assert response.access_token == "access_token"
    assert response.refresh_token == "refresh_token"
    assert response.role == "student"

    mock_get_user.assert_awaited_once()



@pytest.mark.asyncio
async def test_login_admin_success(mocker):
    """
    Test successful admin login
    """

    mock_get_user = mocker.patch(
        "app.services.auth_service.UserRepository.get_user_by_email",
        new_callable=AsyncMock,
        return_value={
            "_id": "1",
            "email": "admin@gmail.com",
            "password": "hashed_password",
            "role": "admin",
        }
    )

    mocker.patch(
        "app.services.auth_service.decrypt_password",
        return_value="Admin@123"
    )

    mocker.patch(
        "app.services.auth_service.verify_password",
        return_value=True
    )

    mocker.patch(
        "app.services.auth_service.create_access_token",
        return_value="access_token"
    )

    mocker.patch(
        "app.services.auth_service.create_refresh_token",
        return_value="refresh_token"
    )

    user = UserLogin(
        email="admin@gmail.com",
        password="encrypted_password",
    )

    response = await AuthService.login(user)

    assert response.access_token == "access_token"
    assert response.refresh_token == "refresh_token"
    assert response.role == "admin"

    mock_get_user.assert_awaited_once()




@pytest.mark.asyncio
async def test_login_invalid_email(mocker):
    """
    Test login with unknown email
    """

    mocker.patch(
        "app.services.auth_service.UserRepository.get_user_by_email",
        new_callable=AsyncMock,
        return_value=None
    )

    user = UserLogin(
        email="naman@gmail.com",
        password="encrypted_password",
    )

    with pytest.raises(UnauthorizedException):
        await AuthService.login(user)



@pytest.mark.asyncio
async def test_login_invalid_password(mocker):
    """
    Test login with incorrect password
    """

    mocker.patch(
        "app.services.auth_service.UserRepository.get_user_by_email",
        new_callable=AsyncMock,
        return_value={
            "_id": "1",
            "email": "naman@gmail.com",
            "password": "hashed_password",
            "role": "student",
        }
    )

    mocker.patch(
        "app.services.auth_service.decrypt_password",
        return_value="WrongPassword@123"
    )

    mocker.patch(
        "app.services.auth_service.verify_password",
        return_value=False
    )

    user = UserLogin(
        email="naman@gmail.com",
        password="encrypted_password",
    )

    with pytest.raises(UnauthorizedException):
        await AuthService.login(user)



@pytest.mark.asyncio
async def test_refresh_token_success(mocker):
    """
    Test access token regeneration
    """

    mocker.patch(
        "app.services.auth_service.verify_token",
        return_value={
            "user_id": "1",
            "email": "naman@gmail.com",
            "role": "student",
            "type": "refresh",
        }
    )

    mocker.patch(
        "app.services.auth_service.create_access_token",
        return_value="new_access_token"
    )

    response = await AuthService.regenerate_access_token("refresh_token")

    assert response.access_token == "new_access_token"