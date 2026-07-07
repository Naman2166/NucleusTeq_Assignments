"""
Test cases for question routes
"""

from unittest.mock import AsyncMock
from bson import ObjectId
from main import app
from app.security.auth import require_admin, get_current_user
from app.utils.constants import QuestionMessage


def test_create_question_route(client, mocker):
    """
    Test create question endpoint
    """

    quiz_id = str(ObjectId())

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    mocker.patch(
        "app.routes.question_routes.QuestionService.create_question",
        new=AsyncMock(
            return_value={
                "id": str(ObjectId()),
                "quiz_id": quiz_id,
                "question": "What is Python?",
                "question_type": "MCQ",
                "options": ["Java", "Python", "C++", "Go"],
                "correct_answer": 2,
                "difficulty": "Easy",
                "tags": [],
                "marks": 5,
            }
        ),
    )

    response = client.post(
        "/questions/",
        json={
            "quiz_id": quiz_id,
            "question": "What is Python?",
            "question_type": "MCQ",
            "options": ["Java", "Python", "C++", "Go"],
            "correct_answer": 2,
            "difficulty": "Easy",
            "tags": [],
            "marks": 5,
        },
    )

    assert response.status_code == 201
    assert response.json()["question"] == "What is Python?"

    app.dependency_overrides.clear()



def test_get_question_by_id_route(client, mocker):
    """
    Test get question by id endpoint
    """

    question_id = str(ObjectId())

    app.dependency_overrides[get_current_user] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
    }

    mocker.patch(
        "app.routes.question_routes.QuestionService.get_question_by_id",
        new=AsyncMock(
            return_value={
                "id": question_id,
                "question": "What is Python?",
                "question_type": "MCQ",
                "options": ["Java", "Python", "C++", "Go"],
                "difficulty": "Easy",
                "marks": 5,
            })
    )

    response = client.get(f"/questions/{question_id}")

    assert response.status_code == 200
    assert response.json()["question"] == "What is Python?"

    app.dependency_overrides.clear()



def test_get_questions_by_quiz_route(client, mocker):
    """
    Test get questions by quiz endpoint
    """

    quiz_id = str(ObjectId())

    app.dependency_overrides[get_current_user] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
    }

    mocker.patch(
        "app.routes.question_routes.QuestionService.get_questions_by_quiz",
        new=AsyncMock(
            return_value=[{
                    "id": str(ObjectId()),
                    "question": "What is Python?",
                    "question_type": "MCQ",
                    "options": ["Java", "Python", "C++", "Go"],
                    "difficulty": "Easy",
                    "marks": 5,
                }])
    )

    response = client.get(f"/questions/quiz/{quiz_id}")

    assert response.status_code == 200
    assert len(response.json()) == 1

    app.dependency_overrides.clear()



def test_update_question_route(client, mocker):
    """
    Test update question endpoint
    """

    question_id = str(ObjectId())

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    mocker.patch(
        "app.routes.question_routes.QuestionService.update_question",
        new=AsyncMock(
            return_value={
                "id": question_id,
                "quiz_id": str(ObjectId()),
                "question": "Updated Question",
                "question_type": "MCQ",
                "options": ["Java", "Python", "C++", "Go"],
                "correct_answer": 2,
                "difficulty": "Easy",
                "tags": [],
                "marks": 5,
            })
    )

    response = client.put(
        f"/questions/{question_id}",
        json={"question": "Updated Question"}
    )

    assert response.status_code == 200
    assert response.json()["question"] == "Updated Question"

    app.dependency_overrides.clear()



def test_delete_question_route(client, mocker):
    """
    Test delete question endpoint
    """

    question_id = str(ObjectId())

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    mocker.patch(
        "app.routes.question_routes.QuestionService.delete_question",
        new=AsyncMock(
            return_value={"message": QuestionMessage.DELETED})
    )

    response = client.delete(f"/questions/{question_id}")

    assert response.status_code == 200
    assert response.json()["message"] == QuestionMessage.DELETED

    app.dependency_overrides.clear()