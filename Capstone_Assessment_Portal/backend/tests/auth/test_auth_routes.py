"""
Test cases for authentication routes
"""

from unittest.mock import AsyncMock
from app.exceptions.custom_exceptions import UnauthorizedException
from main import app
from app.security.auth import require_admin, require_student
from app.utils.constants import AuthMessage



def test_register_route(client, mocker):
    """
    Test register endpoint
    """

    mocker.patch(
        "app.routes.auth_routes.AuthService.register",
        new=AsyncMock(
            return_value={"message": AuthMessage.USER_REGISTERED_SUCCESSFULLY}
        ),
    )

    response = client.post("/auth/register",
        json={
            "first_name": "Naman",
            "last_name": "Patel",
            "email": "naman@gmail.com",
            "password": "encrypted_password",
        },
    )

    assert response.status_code == 201
    assert response.json()["message"] == AuthMessage.USER_REGISTERED_SUCCESSFULLY


def test_login_route(client, mocker):
    """
    Test login endpoint
    """

    mocker.patch(
        "app.routes.auth_routes.AuthService.login",
        new=AsyncMock(
            return_value={
                "access_token": "access_token",
                "refresh_token": "refresh_token",
                "role": "student",
                "token_type": "bearer",
            }
        ),
    )

    response = client.post(
        "/auth/login",
        json={
            "email": "naman@gmail.com",
            "password": "encrypted_password",
        },
    )

    assert response.status_code == 200
    assert response.json()["access_token"] == "access_token"



def test_refresh_token_route(client, mocker):
    """
    Test refresh token endpoint
    """

    mocker.patch(
        "app.routes.auth_routes.AuthService.regenerate_access_token",
        new=AsyncMock(
            return_value={
                "access_token": "new_access_token",
                "token_type": "bearer",
            }
        ),
    )

    response = client.post(
        "/auth/refresh",
        json={"refresh_token": "refresh_token",},
    )

    assert response.status_code == 200
    assert response.json()["access_token"] == "new_access_token"



def test_public_key_route(client, mocker):
    """
    Test public key endpoint
    """

    mocker.patch(
        "app.routes.auth_routes.AuthService.get_public_key",
        new=AsyncMock(
            return_value={"publicKey": "dummy_public_key"}
        ),
    )

    response = client.get("/auth/public-key")

    assert response.status_code == 200
    assert response.json()["publicKey"] == "dummy_public_key"



def test_admin_dashboard(client):
    """
    Test admin dashboard endpoint
    """

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    response = client.get("/auth/admin/dashboard")

    assert response.status_code == 200
    assert response.json()["message"] == "Welcome Admin"

    app.dependency_overrides.clear()



def test_admin_dashboard_without_token(client):
    """
    Test protected endpoint without JWT token
    """

    response = client.get("/auth/admin/dashboard")

    assert response.status_code == 401



def test_admin_dashboard_with_expired_token(client, mocker):
    """
    Test protected endpoint with expired JWT token
    """

    mocker.patch(
        "app.security.auth.verify_token",
        side_effect=UnauthorizedException("Token has expired")
    )

    response = client.get(
        "/auth/admin/dashboard",
        headers={"Authorization": "Bearer expired_token"},
    )

    assert response.status_code == 401



def test_student_dashboard(client):
    """
    Test student dashboard endpoint
    """

    app.dependency_overrides[require_student] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
    }

    response = client.get("/auth/student/dashboard")

    assert response.status_code == 200
    assert response.json()["message"] == "Welcome Student"

    app.dependency_overrides.clear()