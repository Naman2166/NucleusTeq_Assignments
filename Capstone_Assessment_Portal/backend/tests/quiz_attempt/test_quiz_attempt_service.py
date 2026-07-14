"""
Test cases for QuizAttemptService
"""

from datetime import datetime, UTC
import pytest
from unittest.mock import AsyncMock, MagicMock
from bson import ObjectId
from app.services.quiz_attempt_service import QuizAttemptService
from app.schemas.quiz_attempt_schema import AttemptCreate, StudentAnswer
from app.exceptions.custom_exceptions import BadRequestException, ResourceNotFoundException
from app.utils.constants import QuestionType, DifficultyLevel, QuizAttemptMessage, QuizAttemptStatus


@pytest.mark.asyncio
async def test_start_attempt_success(mocker):
    """
    Test successful quiz attempt creation
    """

    quiz_id = str(ObjectId())
    student_id = str(ObjectId())

    quiz = {
        "_id": ObjectId(quiz_id),
        "title": "Python Quiz",
        "duration": 30,
        "total_marks": 5,
        "passing_marks": 3,
        "max_attempts": 2,
    }

    questions = [{
            "_id": ObjectId(),
            "question": "Python is?",
            "question_type": QuestionType.MCQ,
            "options": ["Language", "OS"],
            "correct_answer": 1,
            "difficulty": DifficultyLevel.EASY,
            "tags": [],
            "marks": 5,
        }]

    mocker.patch(
        "app.services.quiz_attempt_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value=quiz,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.QuestionRepository.get_questions_by_quiz",
        new_callable=AsyncMock,
        return_value=questions,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_in_progress_attempt",
        new_callable=AsyncMock,
        return_value=None,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_student_attempts",
        new_callable=AsyncMock,
        return_value=[],
    )

    inserted_result = MagicMock()
    inserted_result.inserted_id = ObjectId()

    mock_create = mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.create_attempt",
        new_callable=AsyncMock,
        return_value=inserted_result,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_attempt_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": inserted_result.inserted_id,
            "quiz_id": ObjectId(quiz_id),
            "attempt_number": 1,
            "status": QuizAttemptStatus.IN_PROGRESS,
            "started_at": quiz["duration"],
            "submitted_at": None,
        },
    )

    attempt = AttemptCreate(quiz_id=quiz_id)

    current_user = {"user_id": student_id}
    response = await QuizAttemptService.start_attempt(attempt, current_user)

    assert response.quiz_id == quiz_id
    assert response.attempt_number == 1
    assert response.status == QuizAttemptStatus.IN_PROGRESS

    mock_create.assert_awaited_once()



@pytest.mark.asyncio
async def test_start_attempt_quiz_not_found(mocker):
    """
    Test start attempt when quiz does not exist
    """

    mocker.patch(
        "app.services.quiz_attempt_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    attempt = AttemptCreate(quiz_id=str(ObjectId()))
    current_user = {"user_id": str(ObjectId())}

    with pytest.raises(ResourceNotFoundException):
        await QuizAttemptService.start_attempt(attempt, current_user)



@pytest.mark.asyncio
async def test_save_answer_success(mocker):
    """
    Test saving a student's answer successfully
    """

    attempt_id = str(ObjectId())
    student_id = str(ObjectId())
    question_id = str(ObjectId())

    attempt = {
        "_id": ObjectId(attempt_id),
        "student_id": ObjectId(student_id),
        "status": QuizAttemptStatus.IN_PROGRESS,
        "answers": [],
        "snapshot": {
            "duration": 30,
            "questions": [{
                    "question_id": question_id,
                    "question": "Python is?",
                    "question_type": QuestionType.MCQ,
                    "options": ["Language", "Database"],
                    "correct_answer": 1,
                    "difficulty": DifficultyLevel.EASY,
                    "marks": 5,
                    "tags": [],
                }]
        },
    }

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_attempt_by_id",
        new_callable=AsyncMock,
        return_value=attempt,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.check_attempt_time_expired",
        return_value=False,
    )

    mock_update = mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.update_attempt",
        new_callable=AsyncMock,
    )

    answer = StudentAnswer(
        question_id=question_id,
        selected_option=1,
    )

    response = await QuizAttemptService.save_answer(
        attempt_id,
        answer,
        {"user_id": student_id},
    )

    assert response.message == QuizAttemptMessage.ANSWER_SAVED

    mock_update.assert_awaited_once_with(
        ObjectId(attempt_id),
        {"answers": [{
                 "question_id": question_id,
                 "selected_option": 1,
                }]
        }
    )


@pytest.mark.asyncio
async def test_get_attempt_questions_success(mocker):
    """
    Test getting questions for an attempt
    """

    attempt_id = str(ObjectId())
    student_id = str(ObjectId())
    question_id = str(ObjectId())

    attempt = {
        "_id": ObjectId(attempt_id),
        "student_id": ObjectId(student_id),
        "status": QuizAttemptStatus.IN_PROGRESS,
        "started_at": datetime.now(),
        "answers": [{
                "question_id": question_id,
                "selected_option": 2,
            }],
        "snapshot": {
            "duration": 30,
            "questions": [
                {
                    "question_id": question_id,
                    "question": "Python is?",
                    "question_type": QuestionType.MCQ,
                    "options": ["Database", "Language"],
                    "difficulty": DifficultyLevel.EASY,
                    "marks": 5,
                }
            ],
        },
    }

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_attempt_by_id",
        new_callable=AsyncMock,
        return_value=attempt,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.check_attempt_time_expired",
        return_value=False,
    )

    response = await QuizAttemptService.get_attempt_questions(
        attempt_id, 0, {"user_id": student_id}
    )

    assert response.question_number == 1
    assert response.total_questions == 1
    assert response.question == "Python is?"
    assert response.selected_option == 2
    assert response.time_remaining > 0
    assert response.marks == 5



@pytest.mark.asyncio
async def test_submit_attempt_success(mocker):
    """
    Test successful quiz submission
    """

    attempt_id = str(ObjectId())
    student_id = str(ObjectId())
    question_id = str(ObjectId())

    attempt = {
        "_id": ObjectId(attempt_id),
        "student_id": ObjectId(student_id),
        "status": QuizAttemptStatus.IN_PROGRESS,
        "started_at": datetime.now(UTC),
        "answers": [
            {
                "question_id": question_id,
                "selected_option": 1,
            }
        ],
        "snapshot": {
            "duration": 30,
            "questions": [
                {
                    "question_id": question_id,
                    "correct_answer": 1,
                    "marks": 5,
                }
            ],
        },
    }

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_attempt_by_id",
        new_callable=AsyncMock,
        return_value=attempt,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.check_attempt_time_expired",
        return_value=False,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.calculate_score",
        return_value=5,
    )

    mock_update = mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.update_attempt",
        new_callable=AsyncMock,
    )

    response = await QuizAttemptService.submit_attempt(
        attempt_id,
        {"user_id": student_id},
    )

    assert response.message == QuizAttemptMessage.SUBMITTED

    mock_update.assert_awaited_once()

    update_data = mock_update.await_args.args[1]

    assert update_data["status"] == QuizAttemptStatus.SUBMITTED
    assert update_data["score"] == 5
    assert "submitted_at" in update_data



@pytest.mark.asyncio
async def test_submit_attempt_already_submitted(mocker):
    """
    Test submitting an already submitted attempt
    """

    attempt_id = str(ObjectId())
    student_id = str(ObjectId())

    attempt = {
        "_id": ObjectId(attempt_id),
        "student_id": ObjectId(student_id),
        "status": QuizAttemptStatus.SUBMITTED,
        "answers": [],
        "snapshot": {
            "duration": 30,
            "questions": [],
        },
    }

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_attempt_by_id",
        new_callable=AsyncMock,
        return_value=attempt,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.check_attempt_time_expired",
        return_value=False,
    )

    with pytest.raises(BadRequestException):
        await QuizAttemptService.submit_attempt(
            attempt_id,
            {"user_id": student_id}
        )


@pytest.mark.asyncio
async def test_submit_attempt_time_expired(mocker):
    """
    Test submitting an expired attempt
    """

    attempt_id = str(ObjectId())
    student_id = str(ObjectId())

    attempt = {
        "_id": ObjectId(attempt_id),
        "student_id": ObjectId(student_id),
        "status": QuizAttemptStatus.IN_PROGRESS,
        "started_at": datetime.now(UTC),
        "answers": [],
        "snapshot": {
            "duration": 30,
            "questions": [],
        },
    }

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_attempt_by_id",
        new_callable=AsyncMock,
        return_value=attempt,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.check_attempt_time_expired",
        return_value=True,
    )

    mock_auto_submit = mocker.patch(
        "app.services.quiz_attempt_service.auto_submit_attempt",
        new_callable=AsyncMock,
    )

    with pytest.raises(BadRequestException):
        await QuizAttemptService.submit_attempt(
            attempt_id,
            {"user_id": student_id}
        )

    mock_auto_submit.assert_awaited_once_with(
        ObjectId(attempt_id),
        attempt,
    )



@pytest.mark.asyncio
async def test_start_attempt_max_attempts_reached(mocker):
    """
    Test starting attempt when maximum attempts are reached
    """

    quiz_id = str(ObjectId())
    student_id = str(ObjectId())

    quiz = {
        "_id": ObjectId(quiz_id),
        "title": "Python Quiz",
        "duration": 30,
        "total_marks": 5,
        "passing_marks": 3,
        "max_attempts": 2,
    }

    questions = [
        {
            "_id": ObjectId(),
            "question": "Python?",
            "question_type": QuestionType.MCQ,
            "options": ["A", "B"],
            "correct_answer": 1,
            "difficulty": DifficultyLevel.EASY,
            "tags": [],
            "marks": 5,
        }
    ]

    mocker.patch(
        "app.services.quiz_attempt_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value=quiz,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.QuestionRepository.get_questions_by_quiz",
        new_callable=AsyncMock,
        return_value=questions,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_in_progress_attempt",
        new_callable=AsyncMock,
        return_value=None,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_student_attempts",
        new_callable=AsyncMock,
        return_value=[
            {"attempt_number": 1},
            {"attempt_number": 2},
        ],
    )

    with pytest.raises(BadRequestException):
        await QuizAttemptService.start_attempt(
            AttemptCreate(quiz_id=quiz_id),
            {"user_id": student_id}
        )


@pytest.mark.asyncio
async def test_start_attempt_already_in_progress(mocker):
    """
    Test starting an attempt when another attempt is already in progress
    """

    quiz_id = str(ObjectId())
    student_id = str(ObjectId())

    mocker.patch(
        "app.services.quiz_attempt_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(quiz_id),
            "title": "Quiz",
            "duration": 30,
            "total_marks": 5,
            "passing_marks": 3,
            "max_attempts": 2,
        },
    )

    mocker.patch(
        "app.services.quiz_attempt_service.QuestionRepository.get_questions_by_quiz",
        new_callable=AsyncMock,
        return_value=[
            {
                "_id": ObjectId(),
                "question": "Q1",
                "question_type": QuestionType.MCQ,
                "options": ["A", "B"],
                "correct_answer": 1,
                "difficulty": DifficultyLevel.EASY,
                "tags": [],
                "marks": 5,
            }
        ],
    )

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_in_progress_attempt",
        new_callable=AsyncMock,
        return_value={"_id": ObjectId()},
    )

    with pytest.raises(BadRequestException):
        await QuizAttemptService.start_attempt(
            AttemptCreate(quiz_id=quiz_id),
            {"user_id": student_id},
        )



@pytest.mark.asyncio
async def test_save_answer_invalid_selected_option(mocker):
    """
    Test saving answer with invalid selected option
    """

    attempt_id = str(ObjectId())
    student_id = str(ObjectId())
    question_id = str(ObjectId())

    attempt = {
        "_id": ObjectId(attempt_id),
        "student_id": ObjectId(student_id),
        "status": QuizAttemptStatus.IN_PROGRESS,
        "answers": [],
        "snapshot": {
            "duration": 30,
            "questions": [
                {
                    "question_id": question_id,
                    "options": ["A", "B"],
                }
            ],
        },
    }

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_attempt_by_id",
        new_callable=AsyncMock,
        return_value=attempt,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.check_attempt_time_expired",
        return_value=False,
    )

    with pytest.raises(BadRequestException):
        await QuizAttemptService.save_answer(
            attempt_id,
            StudentAnswer(
                question_id=question_id,
                selected_option=3,
            ),
            {"user_id": student_id},
        )


@pytest.mark.asyncio
async def test_get_attempt_questions_time_expired(mocker):
    """
    Test getting questions after attempt time has expired
    """

    attempt_id = str(ObjectId())
    student_id = str(ObjectId())

    attempt = {
        "_id": ObjectId(attempt_id),
        "student_id": ObjectId(student_id),
        "status": QuizAttemptStatus.IN_PROGRESS,
        "started_at": datetime.now(),
        "answers": [],
        "snapshot": {
            "duration": 30,
            "questions": [],
        },
    }

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_attempt_by_id",
        new_callable=AsyncMock,
        return_value=attempt,
    )

    mocker.patch(
        "app.services.quiz_attempt_service.check_attempt_time_expired",
        return_value=True,
    )

    mock_auto_submit = mocker.patch(
        "app.services.quiz_attempt_service.auto_submit_attempt",
        new_callable=AsyncMock,
    )

    with pytest.raises(BadRequestException):
        await QuizAttemptService.get_attempt_questions(
            attempt_id, 0, {"user_id": student_id}
        )

    mock_auto_submit.assert_awaited_once_with(
        ObjectId(attempt_id),
        attempt,
    )



@pytest.mark.asyncio
async def test_get_student_attempts_success(mocker):
    """
    Test getting all attempts of a student
    """

    quiz_id = str(ObjectId())
    student_id = str(ObjectId())

    quiz = {
        "_id": ObjectId(quiz_id),
        "title": "Python Quiz",
    }

    attempts = [
        {
            "_id": ObjectId(),
            "quiz_id": ObjectId(quiz_id),
            "attempt_number": 1,
            "status": QuizAttemptStatus.SUBMITTED,
            "started_at": datetime.now(UTC),
            "submitted_at": datetime.now(UTC),
        },
        {
            "_id": ObjectId(),
            "quiz_id": ObjectId(quiz_id),
            "attempt_number": 2,
            "status": QuizAttemptStatus.IN_PROGRESS,
            "started_at": datetime.now(UTC),
            "submitted_at": None,
        },
    ]

    mocker.patch(
        "app.services.quiz_attempt_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value=quiz,
    )

    mock_get_attempts = mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_student_attempts",
        new_callable=AsyncMock,
        side_effect=[
            attempts,
            attempts,
        ],
    )

    mocker.patch(
        "app.services.quiz_attempt_service.check_attempt_time_expired",
        return_value=False,
    )

    mock_auto_submit = mocker.patch(
        "app.services.quiz_attempt_service.auto_submit_attempt",
        new_callable=AsyncMock,
    )

    response = await QuizAttemptService.get_student_attempts(
        quiz_id, {"user_id": student_id}
    )

    assert len(response) == 2
    assert response[0].attempt_number == 1
    assert response[1].attempt_number == 2

    assert mock_get_attempts.await_count == 2
    mock_auto_submit.assert_not_called() 


@pytest.mark.asyncio
async def test_get_attempt_questions_student_not_owner(mocker):
    """
    Test getting questions of another student's attempt
    """

    attempt_id = str(ObjectId())

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_attempt_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(attempt_id),
            "student_id": ObjectId(),
            "status": QuizAttemptStatus.IN_PROGRESS,
            "started_at": datetime.now(UTC),
            "answers": [],
            "snapshot": {
                "duration": 30,
                "questions": [],
            },
        },
    )

    with pytest.raises(ResourceNotFoundException):
        await QuizAttemptService.get_attempt_questions(
            attempt_id, 0, {"user_id": str(ObjectId())}
        )


@pytest.mark.asyncio
async def test_save_answer_attempt_already_submitted(mocker):
    """
    Test saving answer after attempt is submitted
    """

    attempt_id = str(ObjectId())
    student_id = str(ObjectId())

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_attempt_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(attempt_id),
            "student_id": ObjectId(student_id),
            "status": QuizAttemptStatus.SUBMITTED,
            "answers": [],
            "snapshot": {
                "duration": 30,
                "questions": [],
            },
        },
    )

    mocker.patch(
        "app.services.quiz_attempt_service.check_attempt_time_expired",
        return_value=False,
    )

    with pytest.raises(BadRequestException):
        await QuizAttemptService.save_answer(
            attempt_id,
            StudentAnswer(
                question_id=str(ObjectId()),
                selected_option=1,
            ),
            {"user_id": student_id}
        )


@pytest.mark.asyncio
async def test_get_student_all_attempts(mocker):
    """
    Test get all attempts of logged-in student
    """

    student_id = ObjectId()

    mocker.patch(
        "app.services.quiz_attempt_service.QuizAttemptRepository.get_student_all_attempts",
        new_callable=AsyncMock,
        return_value=[
            {
                "_id": ObjectId(),
                "quiz_id": ObjectId(),
                "student_id": student_id,
                "attempt_number": 1,
                "status": QuizAttemptStatus.IN_PROGRESS,
                "started_at": datetime.now(UTC),
                "submitted_at": None,
            },
            {
                "_id": ObjectId(),
                "quiz_id": ObjectId(),
                "student_id": student_id,
                "attempt_number": 1,
                "status": QuizAttemptStatus.SUBMITTED,
                "started_at": datetime.now(UTC),
                "submitted_at": datetime.now(UTC),
            },
        ],
    )

    mocker.patch(
        "app.services.quiz_attempt_service.check_attempt_time_expired",
        return_value=False,
    )

    response = await QuizAttemptService.get_student_all_attempts({"user_id": str(student_id)})

    assert len(response) == 2
    assert response[0].status == QuizAttemptStatus.IN_PROGRESS
    assert response[1].status == QuizAttemptStatus.SUBMITTED