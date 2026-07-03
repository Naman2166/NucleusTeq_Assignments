"""
Test cases for authentication security functions
"""

import pytest
from unittest.mock import MagicMock
from app.security.password import (
    hash_password,
    verify_password,
    validate_password,
)
from app.security.jwt_handler import (
    create_access_token,
    create_refresh_token,
    verify_token,
)
from app.security.decryption import decrypt_password
from app.security.auth import (
    get_current_user,
    require_admin,
    require_student,
)
from app.exceptions.custom_exceptions import (
    BadRequestException,
    ForbiddenException,
    UnauthorizedException,
)


def test_hash_and_verify_password():
    """
    Test password hashing and verification
    """

    password = "Password@123"

    hashed_password = hash_password(password)

    assert hashed_password != password
    assert verify_password(password, hashed_password)



def test_validate_password():
    """
    Test password validation
    """

    validate_password("Password@123")

    with pytest.raises(BadRequestException):
        validate_password("password")



def test_create_and_verify_access_token():
    """
    Test access token generation and verification
    """

    user = {
        "_id": "1",
        "email": "naman@gmail.com",
        "role": "student",
    }

    token = create_access_token(user)

    payload = verify_token(token)

    assert payload["email"] == user["email"]
    assert payload["role"] == user["role"]
    assert payload["type"] == "access"



def test_create_and_verify_refresh_token():
    """
    Test refresh token generation
    """

    user = {
        "_id": "1",
        "email": "naman@gmail.com",
        "role": "student",
    }

    token = create_refresh_token(user)

    payload = verify_token(token)

    assert payload["type"] == "refresh"



def test_verify_invalid_token():
    """
    Test invalid JWT token
    """

    with pytest.raises(UnauthorizedException):
        verify_token("invalid_token")



def test_invalid_encrypted_password():
    """
    Test invalid encrypted password
    """

    with pytest.raises(BadRequestException):
        decrypt_password("invalid_password")



def test_get_current_user_success(mocker):
    """
    Test valid JWT token
    """

    mocker.patch(
        "app.security.auth.verify_token",
        return_value={
            "email": "admin@gmail.com",
            "role": "admin",
        },
    )

    credentials = MagicMock()
    credentials.credentials = "valid_token"

    user = get_current_user(credentials)

    assert user["email"] == "admin@gmail.com"
    assert user["role"] == "admin"



def test_get_current_user_invalid_token(mocker):
    """
    Test invalid JWT token
    """

    mocker.patch(
        "app.security.auth.verify_token",
        side_effect=Exception(),
    )

    credentials = MagicMock()
    credentials.credentials = "invalid_token"

    with pytest.raises(UnauthorizedException):
        get_current_user(credentials)



def test_require_admin():
    """
    Test admin authorization
    """

    user = {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    assert require_admin(user) == user



def test_require_admin_forbidden():
    """
    Test non-admin access
    """

    user = {
        "email": "student@gmail.com",
        "role": "student",
    }

    with pytest.raises(ForbiddenException):
        require_admin(user)



def test_require_student():
    """
    Test student authorization
    """

    user = {
        "email": "student@gmail.com",
        "role": "student",
    }

    assert require_student(user) == user



def test_require_student_forbidden():
    """
    Test non-student access
    """

    user = {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    with pytest.raises(ForbiddenException):
        require_student(user)