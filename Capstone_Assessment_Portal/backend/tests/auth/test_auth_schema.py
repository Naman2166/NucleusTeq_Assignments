"""
Test cases for authentication schemas.
"""

import pytest
from pydantic import ValidationError
from app.schemas.user_schema import (
    UserRegister,
    UserLogin,
    LoginResponse,
    UserResponse,
    RefreshTokenRequest,
    RefreshTokenResponse,
)


def test_user_register_valid():
    """
    Test valid user registration schema
    """
    user = UserRegister(
        first_name="Naman",
        last_name="Patel",
        email="naman@gmail.com",
        password="Password@123"
    )

    assert user.first_name == "Naman"
    assert user.last_name == "Patel"
    assert user.email == "naman@gmail.com"



def test_user_register_invalid_email():
    """
    Test registration with invalid email
    """
    with pytest.raises(ValidationError):
        UserRegister(
            first_name="Naman",
            last_name="Patel",
            email="invalid-email",
            password="Password@123"
        )



def test_user_register_invalid_first_name():
    """
    Test first name validation
    """

    with pytest.raises(ValidationError):
        UserRegister(
            first_name="Naman123",
            last_name="Patel",
            email="naman@gmail.com",
            password="Password@123"
        )



def test_user_login_valid():
    """
    Test valid login schema
    """

    login = UserLogin(
        email="naman@gmail.com",
        password="Password@123"
    )

    assert login.email == "naman@gmail.com"



def test_response_schemas():
    """
    Test response schemas
    """

    login_response = LoginResponse(
        access_token="access_token",
        refresh_token="refresh_token",
        role="admin",
        token_type="Bearer"
    )

    user_response = UserResponse(
        id="1",
        first_name="Naman",
        last_name="Patel",
        email="naman@gmail.com",
        role="admin"
    )

    refresh_request = RefreshTokenRequest(
        refresh_token="refresh_token"
    )

    refresh_response = RefreshTokenResponse(
        access_token="new_access_token",
        token_type="Bearer"
    )

    assert login_response.role == "admin"
    assert user_response.email == "naman@gmail.com"
    assert refresh_request.refresh_token == "refresh_token"
    assert refresh_response.token_type == "Bearer"