"""
Quiz request and response schemas
"""

from typing import Optional
from pydantic import BaseModel, Field


class QuizCreate(BaseModel):
    """
    Schema for creating a quiz
    """
    title: str = Field(
        min_length=3, 
        max_length=100
    )

    description: str = Field(
        min_length=5, 
        max_length=500
    )

    category_id: str

    duration: int = Field(
        gt=0, 
        description="Duration in minutes"
    )

    total_marks: int = Field(
        gt=0, 
        description="Total marks for the quiz"
    )

    passing_marks: int = Field(
        ge=0, 
        description="Marks required to pass"
    )
    max_attempts: int = Field(
        default=1, 
        gt=0, 
        description="Number of attempts allowed"
    )

    status: bool = True



class QuizUpdate(BaseModel):
    """
    Schema for updating a quiz
    """
    title: Optional[str] = Field(
        None, 
        min_length=3, 
        max_length=100
    )

    description: Optional[str] = Field(
        None, 
        min_length=5, 
        max_length=500
    )

    category_id: Optional[str] = None

    duration: Optional[int] = Field(
        None,
        gt=0, 
        description="Duration in minutes"
    )

    total_marks: Optional[int] = Field(
        None, 
        gt=0, 
        description="Total marks for the quiz"
    )

    passing_marks: Optional[int] = Field(
        None, 
        ge=0, 
        description="Marks required to pass"
    )

    max_attempts: Optional[int] = Field(
        None, 
        gt=0, 
        description="Number of attempts allowed"
    )

    status: Optional[bool] = None



class QuizResponse(BaseModel):
    """
    Schema for returning quiz information
    """
    id: str
    title: str
    description: str
    category_id: str
    duration: int
    total_marks: int
    passing_marks: int
    max_attempts: int
    status: bool