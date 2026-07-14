"""
Test cases for quiz attempt schemas
"""

from datetime import datetime
import pytest
from pydantic import ValidationError
from app.utils.constants import QuestionType, DifficultyLevel, QuizAttemptStatus
from app.schemas.quiz_attempt_schema import (
    AttemptQuestionSnapshot,
    QuizSnapshot,
    StudentAnswer,
    AttemptCreate,
    AttemptResponse,
    AttemptQuestionResponse,
)


def test_attempt_question_snapshot_valid():
    """
    Test valid attempt question snapshot schema
    """
    question = AttemptQuestionSnapshot(
        question_id="question123",
        question="Python is interpreted?",
        question_type=QuestionType.TRUE_FALSE,
        options=["True", "False"],
        correct_answer=1,
        difficulty=DifficultyLevel.EASY,
        tags=["python"],
        marks=2,
    )

    assert question.question == "Python is interpreted?"
    assert question.correct_answer == 1
    assert question.marks == 2



def test_attempt_question_snapshot_invalid_marks():
    """
    Test invalid marks in question snapshot
    """
    with pytest.raises(ValidationError):
        AttemptQuestionSnapshot(
            question_id="question123",
            question="Python is interpreted?",
            question_type=QuestionType.TRUE_FALSE,
            options=["True", "False"],
            correct_answer=1,
            difficulty=DifficultyLevel.EASY,
            tags=[],
            marks=0,
        )



def test_quiz_snapshot_valid():
    """
    Test valid quiz snapshot schema
    """
    quiz = QuizSnapshot(
        title="Python Quiz",
        duration=30,
        total_marks=20,
        passing_marks=10,
        questions=[
            AttemptQuestionSnapshot(
                question_id="question123",
                question="Python is interpreted?",
                question_type=QuestionType.TRUE_FALSE,
                options=["True", "False"],
                correct_answer=1,
                difficulty=DifficultyLevel.EASY,
                tags=[],
                marks=2,
            )
        ],
    )

    assert quiz.title == "Python Quiz"
    assert len(quiz.questions) == 1



def test_student_answer_valid():
    """
    Test valid student answer schema
    """
    answer = StudentAnswer(
        question_id="question123",
        selected_option=2,
    )

    assert answer.selected_option == 2



def test_student_answer_invalid_option():
    """
    Test invalid selected option
    """
    with pytest.raises(ValidationError):
        StudentAnswer(
            question_id="question123",
            selected_option=0,
        )



def test_attempt_create_valid():
    """
    Test valid attempt creation schema
    """
    attempt = AttemptCreate(quiz_id="quiz123")

    assert attempt.quiz_id == "quiz123"



def test_attempt_response_schema():
    """
    Test attempt response schema
    """
    now = datetime.now()

    response = AttemptResponse(
        id="attempt123",
        quiz_id="quiz123",
        attempt_number=1,
        status=QuizAttemptStatus.IN_PROGRESS,
        started_at=now,
        submitted_at=None,
    )

    assert response.id == "attempt123"
    assert response.status == QuizAttemptStatus.IN_PROGRESS
    assert response.attempt_number == 1


def test_attempt_response_invalid_attempt_number():
    """
    Test invalid attempt number
    """
    with pytest.raises(ValidationError):
        AttemptResponse(
            id="attempt123",
            quiz_id="quiz123",
            attempt_number=0,
            status=QuizAttemptStatus.IN_PROGRESS,
            started_at=datetime.now(),
        )


def test_attempt_question_response_schema():
    """
    Test valid attempt question response schema
    """
    response = AttemptQuestionResponse(
        id="question123",
        question_number=1,
        total_questions=10,
        question="Python is interpreted?",
        question_type=QuestionType.TRUE_FALSE,
        options=["True", "False"],
        difficulty=DifficultyLevel.EASY,
        marks=2,
        selected_option=None,
    )

    assert response.id == "question123"
    assert response.question_number == 1
    assert response.total_questions == 10
    assert response.question == "Python is interpreted?"
    assert response.selected_option is None