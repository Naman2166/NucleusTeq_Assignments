"""
Test cases for quiz schemas
"""

import pytest
from pydantic import ValidationError
from app.schemas.quiz_schema import (QuizCreate, QuizUpdate, QuizResponse)


def test_quiz_create_valid():
    """
    Test valid quiz creation schema
    """
    quiz = QuizCreate(
        title="Python Basics",
        description="Basic Python quiz",
        category_id="category123",
        duration=30,
        total_marks=100,
        passing_marks=40,
        max_attempts=2,
    )

    assert quiz.title == "Python Basics"
    assert quiz.category_id == "category123"
    assert quiz.duration == 30



def test_quiz_create_invalid_title():
    """
    Test quiz creation with invalid title
    """
    with pytest.raises(ValidationError):
        QuizCreate(
            title="Py",
            description="Basic Python quiz",
            category_id="category123",
            duration=30,
            total_marks=100,
            passing_marks=40,
            max_attempts=2,
        )



def test_quiz_update_valid():
    """
    Test valid quiz update schema
    """
    quiz = QuizUpdate(title="Updated Quiz")

    assert quiz.title == "Updated Quiz"



def test_response_schema():
    """
    Test quiz response schema
    """
    quiz = QuizResponse(
        id="quiz123",
        title="Python Basics",
        description="Basic Python quiz",
        category_id="category123",
        duration=30,
        total_marks=100,
        passing_marks=40,
        max_attempts=2,
    )

    assert quiz.id == "quiz123"
    assert quiz.title == "Python Basics"
    assert quiz.total_marks == 100