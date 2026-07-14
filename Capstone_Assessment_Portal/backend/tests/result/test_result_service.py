"""
Test cases for ResultService
"""

from datetime import datetime, UTC
import pytest
from unittest.mock import AsyncMock
from bson import ObjectId
from app.schemas.result_schema import AttemptHistoryResponse
from app.services.result_service import ResultService


def sample_attempt(student_id: ObjectId, score: int = 8, passing_marks: int = 5):
    """
    Sample quiz attempt document
    """
    return {
        "_id": ObjectId(),
        "quiz_id": ObjectId(),
        "student_id": student_id,
        "attempt_number": 1,
        "score": score,
        "started_at": datetime.now(UTC),
        "submitted_at": datetime.now(UTC),
        "answers": [{
                "question_id": "q1",
                "selected_option": 1,
            }],
        "snapshot": {
            "title": "Python Quiz",
            "total_marks": 10,
            "passing_marks": passing_marks,
            "questions": [{
                    "question_id": "q1",
                    "question": "Python is?",
                    "options": ["Language", "Database"],
                    "correct_answer": 1,
                    "marks": 10,
                }]
        }
    }


@pytest.mark.asyncio
async def test_generate_result(mocker):
    """
    Test generate result
    """

    student_id = ObjectId()

    mocker.patch(
        "app.services.result_service.get_attempt",
        new_callable=AsyncMock,
        return_value=sample_attempt(student_id),
    )

    response = await ResultService.get_result(
        str(ObjectId()),
        {"user_id": str(student_id)},
    )

    assert response.quiz_title == "Python Quiz"
    assert response.score == 8
    assert response.total_marks == 10



@pytest.mark.asyncio
async def test_calculate_percentage(mocker):
    """
    Test calculate percentage
    """

    student_id = ObjectId()

    mocker.patch(
        "app.services.result_service.get_attempt",
        new_callable=AsyncMock,
        return_value=sample_attempt(student_id, score=7),
    )

    response = await ResultService.get_result(
        str(ObjectId()),
        {"user_id": str(student_id)},
    )

    assert response.percentage == 70
    assert response.score == 7



@pytest.mark.asyncio
async def test_pass_fail_validation(mocker):
    """
    Test Pass/Fail validation
    """

    student_id = ObjectId()

    mocker.patch(
        "app.services.result_service.get_attempt",
        new_callable=AsyncMock,
        return_value=sample_attempt(
            student_id,
            score=4,
            passing_marks=5,
        ),
    )

    response = await ResultService.get_result(
        str(ObjectId()),
        {"user_id": str(student_id)},
    )

    assert response.is_pass is False



@pytest.mark.asyncio
async def test_get_student_results(mocker):
    """
    Test Fetch student results
    """

    student_id = ObjectId()

    mocker.patch(
        "app.services.result_service.ResultRepository.get_student_results",
        new_callable=AsyncMock,
        return_value=[sample_attempt(student_id)],
    )

    mocker.patch(
        "app.services.result_service.history_helper",
        return_value=AttemptHistoryResponse(
            attempt_id="attempt123",
            student_name="Naman Patel",
            category_name="Python",
            quiz_id="quiz123",
            quiz_title="Python Quiz",
            attempt_number=1,
            score=8,
            total_marks=10,
            percentage=80,
            is_pass=True,
            submitted_at=datetime.now(UTC),
        ),
    )

    response = await ResultService.get_student_results(
        {"user_id": str(student_id)}
    )

    assert len(response) == 1
    assert response[0].quiz_title == "Python Quiz"



@pytest.mark.asyncio
async def test_get_all_results(mocker):
    """
    Test Fetch admin dashboard results
    """

    category_id = ObjectId()

    mocker.patch(
        "app.services.result_service.ResultRepository.get_all_results",
        new_callable=AsyncMock,
        return_value=[sample_attempt(ObjectId())],
    )

    mocker.patch(
        "app.services.result_service.UserRepository.get_user_by_id",
        new_callable=AsyncMock,
        return_value={
            "first_name": "Naman",
            "last_name": "Patel",
        },
    )

    mocker.patch(
        "app.services.result_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value={
            "category_id": category_id,
        },
    )

    mocker.patch(
        "app.services.result_service.CategoryRepository.get_category_by_id",
        new_callable=AsyncMock,
        return_value={
            "name": "Python",
        },
    )

    response = await ResultService.get_all_results()

    assert len(response) == 1
    assert response[0].quiz_title == "Python Quiz"
    assert response[0].student_name == "Naman Patel"
    assert response[0].category_name == "Python"



@pytest.mark.asyncio
async def test_get_result_student_not_owner(mocker):
    """
    Test student cannot access another student's result
    """

    mocker.patch(
        "app.services.result_service.get_attempt",
        new_callable=AsyncMock,
        return_value=sample_attempt(ObjectId()),
    )

    from app.exceptions.custom_exceptions import ResourceNotFoundException

    with pytest.raises(ResourceNotFoundException):
        await ResultService.get_result(
            str(ObjectId()),
            {"user_id": str(ObjectId())},
        )


@pytest.mark.asyncio
async def test_result_breakdown_student(mocker):
    """
    Test Result breakdown for student
    """

    student_id = ObjectId()

    mocker.patch(
        "app.services.result_service.get_attempt",
        new_callable=AsyncMock,
        return_value=sample_attempt(student_id),
    )

    response = await ResultService.get_result(
        str(ObjectId()),
        {"user_id": str(student_id)},
    )

    assert response.score == 8
    assert response.percentage == 80
    assert "questions" not in response.model_dump()


@pytest.mark.asyncio
async def test_result_breakdown_admin(mocker):
    """
    Test admin result contains question breakdown
    """

    student_id = ObjectId()

    mocker.patch(
        "app.services.result_service.get_attempt",
        new_callable=AsyncMock,
        return_value=sample_attempt(student_id),
    )

    response = await ResultService.get_result_admin(str(ObjectId()))

    question = response.questions[0]

    assert question.question == "Python is?"
    assert question.selected_option == 1
    assert question.correct_answer == 1
    assert question.obtained_marks == 10
    assert question.is_correct is True