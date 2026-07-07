"""
Test cases for Quiz Attempt routes
"""

from datetime import datetime, UTC
from unittest.mock import AsyncMock
from bson import ObjectId
from main import app
from app.security.auth import require_student
from app.utils.constants import QuizAttemptStatus, QuizAttemptMessage


def test_start_attempt_route(client, mocker):
    """
    Test start attempt endpoint
    """

    quiz_id = str(ObjectId())

    app.dependency_overrides[require_student] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
        "user_id": str(ObjectId()),
    }

    mocker.patch(
        "app.routes.quiz_attempt_routes.QuizAttemptService.start_attempt",
        new=AsyncMock(
            return_value={
                "id": str(ObjectId()),
                "quiz_id": quiz_id,
                "attempt_number": 1,
                "status": QuizAttemptStatus.IN_PROGRESS,
                "started_at": datetime.now(UTC),
                "submitted_at": None,
            })
    )

    response = client.post("/quiz-attempts/", json={"quiz_id": quiz_id})

    assert response.status_code == 201
    assert response.json()["attempt_number"] == 1

    app.dependency_overrides.clear()



def test_get_attempt_questions_route(client, mocker):
    """
    Test get attempt questions endpoint
    """

    attempt_id = str(ObjectId())

    app.dependency_overrides[require_student] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
        "user_id": str(ObjectId()),
    }

    mocker.patch(
        "app.routes.quiz_attempt_routes.QuizAttemptService.get_attempt_questions",
        new=AsyncMock(
            return_value=[{
                    "id": str(ObjectId()),
                    "question": "Python is?",
                    "question_type": "MCQ",
                    "options": ["A", "B"],
                    "difficulty": "Easy",
                    "marks": 5,
                    "selected_option": None,
                }]
        )
    )

    response = client.get(f"/quiz-attempts/{attempt_id}/questions")

    assert response.status_code == 200
    assert len(response.json()) == 1

    app.dependency_overrides.clear()



def test_save_answer_route(client, mocker):
    """
    Test save answer endpoint
    """

    attempt_id = str(ObjectId())
    question_id = str(ObjectId())

    app.dependency_overrides[require_student] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
        "user_id": str(ObjectId()),
    }

    mocker.patch(
        "app.routes.quiz_attempt_routes.QuizAttemptService.save_answer",
        new=AsyncMock(
            return_value={"message": QuizAttemptMessage.ANSWER_SAVED}
        )
    )

    response = client.patch(
        f"/quiz-attempts/{attempt_id}/answer",
        json={"question_id": question_id, "selected_option": 2}
    )

    assert response.status_code == 200
    assert response.json()["message"] == QuizAttemptMessage.ANSWER_SAVED

    app.dependency_overrides.clear()



def test_submit_attempt_route(client, mocker):
    """
    Test submit attempt endpoint
    """

    attempt_id = str(ObjectId())

    app.dependency_overrides[require_student] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
        "user_id": str(ObjectId()),
    }

    mocker.patch(
        "app.routes.quiz_attempt_routes.QuizAttemptService.submit_attempt",
        new=AsyncMock(
            return_value={"message": QuizAttemptMessage.SUBMITTED}
        )
    )

    response = client.post(f"/quiz-attempts/{attempt_id}/submit")

    assert response.status_code == 200
    assert response.json()["message"] == QuizAttemptMessage.SUBMITTED

    app.dependency_overrides.clear()



def test_get_student_attempts_route(client, mocker):
    """
    Test get student attempts endpoint
    """

    quiz_id = str(ObjectId())

    app.dependency_overrides[require_student] = lambda: {
        "email": "student@gmail.com",
        "role": "student",
        "user_id": str(ObjectId()),
    }

    mocker.patch(
        "app.routes.quiz_attempt_routes.QuizAttemptService.get_student_attempts",
        new=AsyncMock(
            return_value=[{
                    "id": str(ObjectId()),
                    "quiz_id": quiz_id,
                    "attempt_number": 1,
                    "status": QuizAttemptStatus.SUBMITTED,
                    "started_at": datetime.now(UTC),
                    "submitted_at": datetime.now(UTC),
                }]
        )
    )

    response = client.get(f"/quiz-attempts/quiz/{quiz_id}")

    assert response.status_code == 200
    assert len(response.json()) == 1

    app.dependency_overrides.clear()
    