"""
Question request and response schemas
"""

from typing import List, Optional
from pydantic import BaseModel, Field

from app.utils.constants import QuestionType, DifficultyLevel


class QuestionCreate(BaseModel):
    """
    Schema for creating a question
    """
    quiz_id: str = Field(
        description="ID of the quiz to which the question belongs"
    )

    question: str = Field(
        min_length=5,
        max_length=500,
        description="Question statement"
    )
    

    question_type: QuestionType

    options: List[str] = Field(
        min_length=2,
        max_length=4,
        description="List of answer options"
    )

    correct_answer: int = Field(
        ge=1, 
        description="One-based index of the correct option"
    )

    difficulty: DifficultyLevel

    tags: List[str] = Field(default_factory=list)

    marks: int = Field(gt=0)



class QuestionUpdate(BaseModel):
    """
    Schema for updating a question
    """
    question: Optional[str] = Field(
        None,
        min_length=5,
        max_length=500
    )

    question_type: Optional[QuestionType] = None

    options: Optional[List[str]] = Field(
        None,
        min_length=2,
        max_length=4
    )

    correct_answer: Optional[int] = Field(
        None,
        ge=1,
        description="One-based index of the correct option"
    )

    difficulty: Optional[DifficultyLevel] = None

    tags: Optional[List[str]] = None

    marks: Optional[int] = Field(None, gt=0)



class QuestionResponseAdmin(BaseModel):
    """
    Schema for returning question details to Admin
    """
    id: str
    quiz_id: str
    question: str
    question_type: QuestionType
    options: List[str]
    correct_answer: int
    difficulty: DifficultyLevel
    tags: List[str]
    marks: int


class QuestionResponseStudent(BaseModel):
    """
    Schema for returning question details to student during quiz attempt
    """
    id: str
    question: str
    question_type: QuestionType
    options: List[str]
    difficulty: DifficultyLevel
    marks: int