"""
Test cases for quiz routes
"""

from unittest.mock import AsyncMock
from bson import ObjectId
from main import app
from app.security.auth import require_admin, get_current_user
from app.utils.constants import QuizMessage



def test_create_quiz_route(client, mocker):
    """
    Test create quiz endpoint
    """

    category_id = str(ObjectId())

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    mocker.patch(
        "app.routes.quiz_routes.QuizService.create_quiz",
        new=AsyncMock(
            return_value={
                "id": str(ObjectId()),
                "title": "Python Quiz",
                "description": "Basic Python Quiz",
                "category_id": category_id,
                "duration": 30,
                "total_marks": 100,
                "passing_marks": 40,
                "max_attempts": 2,
            }
        ),
    )

    response = client.post(
        "/quizzes/",
        json={
            "title": "Python Quiz",
            "description": "Basic Python Quiz",
            "category_id": category_id,
            "duration": 30,
            "total_marks": 100,
            "passing_marks": 40,
            "max_attempts": 2,
        },
    )

    assert response.status_code == 201
    assert response.json()["title"] == "Python Quiz"

    app.dependency_overrides.clear()



def test_get_all_quizzes_route(client, mocker):
    """
    Test get all quizzes endpoint
    """

    app.dependency_overrides[get_current_user] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
    }

    mocker.patch(
        "app.routes.quiz_routes.QuizService.get_all_quizzes",
        new=AsyncMock(
            return_value=[
                {
                    "id": str(ObjectId()),
                    "title": "Python Quiz",
                    "description": "Basic Python Quiz",
                    "category_id": str(ObjectId()),
                    "duration": 30,
                    "total_marks": 100,
                    "passing_marks": 40,
                    "max_attempts": 2,
                }
            ]
        ),
    )

    response = client.get("/quizzes/")

    assert response.status_code == 200
    assert len(response.json()) == 1

    app.dependency_overrides.clear()



def test_get_quiz_by_id_route(client, mocker):
    """
    Test get quiz by id endpoint
    """

    quiz_id = str(ObjectId())

    app.dependency_overrides[get_current_user] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
    }

    mocker.patch(
        "app.routes.quiz_routes.QuizService.get_quiz_by_id",
        new=AsyncMock(
            return_value={
                "id": quiz_id,
                "title": "Python Quiz",
                "description": "Basic Python Quiz",
                "category_id": str(ObjectId()),
                "duration": 30,
                "total_marks": 100,
                "passing_marks": 40,
                "max_attempts": 2,
            }
        ),
    )

    response = client.get(f"/quizzes/{quiz_id}")

    assert response.status_code == 200
    assert response.json()["title"] == "Python Quiz"

    app.dependency_overrides.clear()



def test_get_quizzes_by_category_route(client, mocker):
    """
    Test get quizzes by category endpoint
    """

    category_id = str(ObjectId())

    app.dependency_overrides[get_current_user] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
    }

    mocker.patch(
        "app.routes.quiz_routes.QuizService.get_quizzes_by_category",
        new=AsyncMock(
            return_value=[
                {
                    "id": str(ObjectId()),
                    "title": "Python Quiz",
                    "description": "Basic Python Quiz",
                    "category_id": category_id,
                    "duration": 30,
                    "total_marks": 100,
                    "passing_marks": 40,
                    "max_attempts": 2,
                }
            ]
        ),
    )

    response = client.get(f"/quizzes/category/{category_id}")

    assert response.status_code == 200
    assert len(response.json()) == 1

    app.dependency_overrides.clear()



def test_update_quiz_route(client, mocker):
    """
    Test update quiz endpoint
    """

    quiz_id = str(ObjectId())

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    mocker.patch(
        "app.routes.quiz_routes.QuizService.update_quiz",
        new=AsyncMock(
            return_value={
                "id": quiz_id,
                "title": "Updated Quiz",
                "description": "Updated Description",
                "category_id": str(ObjectId()),
                "duration": 30,
                "total_marks": 100,
                "passing_marks": 40,
                "max_attempts": 2,
            }
        ),
    )

    response = client.put(
        f"/quizzes/{quiz_id}",
        json={
            "title": "Updated Quiz",
        },
    )

    assert response.status_code == 200
    assert response.json()["title"] == "Updated Quiz"

    app.dependency_overrides.clear()



def test_delete_quiz_route(client, mocker):
    """
    Test delete quiz endpoint
    """

    quiz_id = str(ObjectId())

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    mocker.patch(
        "app.routes.quiz_routes.QuizService.delete_quiz",
        new=AsyncMock(
            return_value={
                "message": QuizMessage.DELETED
            }
        ),
    )

    response = client.delete(f"/quizzes/{quiz_id}")

    assert response.status_code == 200
    assert response.json()["message"] == QuizMessage.DELETED

    app.dependency_overrides.clear()