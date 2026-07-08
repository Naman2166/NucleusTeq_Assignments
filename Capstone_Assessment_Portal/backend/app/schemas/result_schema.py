"""
Result schemas
"""

from datetime import datetime
from pydantic import BaseModel, Field


class ResultQuestionResponse(BaseModel):
    """
    Question-wise result
    """
    question: str
    options: list[str]
    selected_option: int | None = None
    correct_answer: int
    marks: int = Field(gt=0)
    obtained_marks: int = Field(ge=0)
    is_correct: bool


class ResultResponseAdmin(BaseModel):
    """
    Detailed quiz result
    """
    attempt_id: str
    quiz_id: str
    quiz_title: str
    attempt_number: int = Field(gt=0)
    score: int = Field(ge=0)
    total_marks: int = Field(gt=0)
    percentage: float = Field(ge=0, le=100)
    passing_marks: int = Field(ge=0)
    is_pass: bool
    started_at: datetime
    submitted_at: datetime
    questions: list[ResultQuestionResponse]


class ResultResponseStudent(BaseModel):
    """
    Student quiz result
    """
    attempt_id: str
    quiz_id: str
    quiz_title: str
    attempt_number: int = Field(gt=0)
    score: int = Field(ge=0)
    total_marks: int = Field(gt=0)
    percentage: float = Field(ge=0, le=100)
    passing_marks: int = Field(ge=0)
    is_pass: bool
    started_at: datetime
    submitted_at: datetime


class AttemptHistoryResponse(BaseModel):
    """
    Student attempt history
    """
    attempt_id: str
    quiz_id: str
    quiz_title: str
    attempt_number: int = Field(gt=0)
    score: int = Field(ge=0)
    total_marks: int = Field(gt=0)
    percentage: float = Field(ge=0, le=100)
    is_pass: bool
    submitted_at: datetime