"""
Test cases for Result routes
"""

from datetime import datetime, UTC
from unittest.mock import AsyncMock
from bson import ObjectId
from main import app
from app.security.auth import require_student, require_admin


def test_get_student_results_route(client, mocker):
    """
    Test get student results endpoint
    """

    app.dependency_overrides[require_student] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
        "user_id": str(ObjectId()),
    }

    mocker.patch(
        "app.routes.result_routes.ResultService.get_student_results",
        new=AsyncMock(
            return_value=[
                {
                    "attempt_id": str(ObjectId()),
                    "quiz_id": str(ObjectId()),
                    "quiz_title": "Python Quiz",
                    "attempt_number": 1,
                    "score": 8,
                    "total_marks": 10,
                    "percentage": 80,
                    "is_pass": True,
                    "submitted_at": datetime.now(UTC),
                }
            ]
        ),
    )

    response = client.get("/results/history")

    assert response.status_code == 200
    assert len(response.json()) == 1
    assert response.json()[0]["quiz_title"] == "Python Quiz"

    app.dependency_overrides.clear()



def test_get_all_results_route(client, mocker):
    """
    Test get all results endpoint
    """

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
        "user_id": str(ObjectId()),
    }

    mocker.patch(
        "app.routes.result_routes.ResultService.get_all_results",
        new=AsyncMock(
            return_value=[
                {
                    "attempt_id": str(ObjectId()),
                    "quiz_id": str(ObjectId()),
                    "quiz_title": "Python Quiz",
                    "attempt_number": 1,
                    "score": 8,
                    "total_marks": 10,
                    "percentage": 80,
                    "is_pass": True,
                    "submitted_at": datetime.now(UTC),
                }
            ]
        ),
    )

    response = client.get("/results/admin")

    assert response.status_code == 200
    assert len(response.json()) == 1
    assert response.json()[0]["score"] == 8

    app.dependency_overrides.clear()



def test_get_result_route(client, mocker):
    """
    Test get result endpoint
    """

    attempt_id = str(ObjectId())

    app.dependency_overrides[require_student] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
        "user_id": str(ObjectId()),
    }

    mocker.patch(
        "app.routes.result_routes.ResultService.get_result",
        new=AsyncMock(
            return_value={
                "attempt_id": attempt_id,
                "quiz_id": str(ObjectId()),
                "quiz_title": "Python Quiz",
                "attempt_number": 1,
                "score": 8,
                "total_marks": 10,
                "percentage": 80,
                "passing_marks": 5,
                "is_pass": True,
                "started_at": datetime.now(UTC),
                "submitted_at": datetime.now(UTC),
                "questions": [],
            }
        ),
    )

    response = client.get(f"/results/{attempt_id}")

    assert response.status_code == 200
    assert response.json()["attempt_id"] == attempt_id
    assert response.json()["score"] == 8

    app.dependency_overrides.clear()



def test_get_result_admin_route(client, mocker):
    """
    Test get result admin endpoint
    """

    attempt_id = str(ObjectId())

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
        "user_id": str(ObjectId()),
    }

    mocker.patch(
        "app.routes.result_routes.ResultService.get_result_admin",
        new=AsyncMock(
            return_value={
                "attempt_id": attempt_id,
                "quiz_id": str(ObjectId()),
                "quiz_title": "Python Quiz",
                "attempt_number": 1,
                "score": 8,
                "total_marks": 10,
                "percentage": 80,
                "passing_marks": 5,
                "is_pass": True,
                "started_at": datetime.now(UTC),
                "submitted_at": datetime.now(UTC),
                "questions": [],
            }
        ),
    )

    response = client.get(f"/results/admin/{attempt_id}")

    assert response.status_code == 200
    assert response.json()["quiz_title"] == "Python Quiz"
    assert response.json()["score"] == 8

    app.dependency_overrides.clear()