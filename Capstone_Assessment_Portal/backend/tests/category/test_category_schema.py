"""
Test cases for category schemas
"""

import pytest
from pydantic import ValidationError
from app.schemas.category_schema import (
    CategoryCreate,
    CategoryUpdate,
    CategoryResponse,
)


def test_category_create_valid():
    """
    Test CategoryCreate schema with valid data
    """

    category = CategoryCreate(
        name="Programming",
        description="Programming related quizzes",
    )

    assert category.name == "Programming"
    assert category.description == "Programming related quizzes"



def test_category_create_invalid_name():
    """
    Test CategoryCreate schema with invalid name
    """

    with pytest.raises(ValidationError):
        CategoryCreate(
            name="Py",
            description="Programming related quizzes",
        )



def test_category_update_valid():
    """
    Test CategoryUpdate schema with valid data
    """

    category = CategoryUpdate(
        name="Java",
        description="Java programming quizzes",
    )

    assert category.name == "Java"
    assert category.description == "Java programming quizzes"



def test_category_update_empty():
    """
    Test CategoryUpdate schema with no fields
    """

    category = CategoryUpdate()

    assert category.name is None
    assert category.description is None



def test_category_update_invalid_name():
    """
    Test CategoryUpdate schema with invalid name
    """

    with pytest.raises(ValidationError):
        CategoryUpdate(name="AI")



def test_category_response_valid():
    """
    Test CategoryResponse schema with valid data
    """

    category = CategoryResponse(
        id="123",
        name="Programming",
        description="Programming related quizzes",
    )

    assert category.id == "123"
    assert category.name == "Programming"
    assert category.description == "Programming related quizzes"