"""
Test cases for question schemas
"""

import pytest
from pydantic import ValidationError
from app.utils.constants import QuestionType, DifficultyLevel
from app.schemas.question_schema import (
    QuestionCreate,
    QuestionUpdate,
    QuestionResponseAdmin,
    QuestionResponseStudent,
)


def test_question_create_valid():
    """
    Test QuestionCreate schema with valid data
    """

    question = QuestionCreate(
        quiz_id="quiz123",
        question="What is Python?",
        question_type=QuestionType.MCQ,
        options=["Java", "Python", "C++", "Go"],
        correct_answer=2,
        difficulty=DifficultyLevel.EASY,
        tags=["programming"],
        marks=5,
    )

    assert question.quiz_id == "quiz123"
    assert question.question == "What is Python?"
    assert question.correct_answer == 2
    assert question.marks == 5


def test_question_create_invalid_question():
    """
    Test QuestionCreate schema with invalid question length
    """

    with pytest.raises(ValidationError):
        QuestionCreate(
            quiz_id="quiz123",
            question="Test",
            question_type=QuestionType.MCQ,
            options=["A", "B"],
            correct_answer=1,
            difficulty=DifficultyLevel.EASY,
            marks=5,
        )


def test_question_update_valid():
    """
    Test QuestionUpdate schema with valid data
    """

    question = QuestionUpdate(
        question="Updated question?",
        marks=10,
    )

    assert question.question == "Updated question?"
    assert question.marks == 10


def test_question_update_empty():
    """
    Test QuestionUpdate schema with no fields
    """

    question = QuestionUpdate()

    assert question.question is None
    assert question.question_type is None
    assert question.options is None
    assert question.correct_answer is None
    assert question.difficulty is None
    assert question.tags is None
    assert question.marks is None


def test_question_update_invalid_marks():
    """
    Test QuestionUpdate schema with invalid marks
    """

    with pytest.raises(ValidationError):
        QuestionUpdate(marks=0)


def test_question_response_admin_valid():
    """
    Test QuestionResponseAdmin schema with valid data
    """

    question = QuestionResponseAdmin(
        id="123",
        quiz_id="quiz123",
        question="What is Python?",
        question_type=QuestionType.MCQ,
        options=["Java", "Python", "C++", "Go"],
        correct_answer=2,
        difficulty=DifficultyLevel.EASY,
        tags=["programming"],
        marks=5,
    )

    assert question.id == "123"
    assert question.quiz_id == "quiz123"
    assert question.correct_answer == 2


def test_question_response_student_valid():
    """
    Test QuestionResponseStudent schema with valid data
    """

    question = QuestionResponseStudent(
        id="123",
        question="What is Python?",
        question_type=QuestionType.MCQ,
        options=["Java", "Python", "C++", "Go"],
        difficulty=DifficultyLevel.EASY,
        marks=5,
    )

    assert question.id == "123"
    assert question.question == "What is Python?"
    assert question.marks == 5