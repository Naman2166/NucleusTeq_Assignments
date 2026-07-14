"""
Quiz Attempt request and response schemas
"""

from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel, Field
from app.utils.constants import QuestionType, DifficultyLevel, QuizAttemptStatus


class AttemptQuestionSnapshot(BaseModel):
    """
    snapshot of a question stored inside an attempt
    """
    question_id: str 

    question: str = Field(
        description="Question statement"
    )

    question_type: QuestionType = Field(
        description="Type of the question mcq or true/false"
    )

    options: List[str] = Field(
        description="List of answer options"
    )

    correct_answer: int = Field(
        ge=1,
        description="One-based index of the correct option"
    )

    difficulty: DifficultyLevel = Field(
        description="Difficulty level of the question"
    )

    tags: List[str] = Field(default_factory=list)

    marks: int = Field(
        gt=0,
        description="Marks assigned to the question"
    )



class QuizSnapshot(BaseModel):
    """
    snapshot of a quiz stored inside an attempt
    """
    title: str = Field(
        description="Title of the quiz"
    )

    duration: int = Field(
        gt=0,
        description="Duration of the quiz in minutes"
    )

    total_marks: int = Field(
        gt=0,
        description="Maximum marks for the quiz"
    )

    passing_marks: int = Field(
        ge=0,
        description="Minimum marks required to pass the quiz"
    )

    questions: List[AttemptQuestionSnapshot] = Field(
        description="Snapshot of all questions in the quiz"
    )



class StudentAnswer(BaseModel):
    """
    Schema for Student's answer for a question
    """
    question_id: str 

    selected_option: int = Field(
        ge=1,
        description="One-based index of the selected option"
    )



class AttemptCreate(BaseModel):
    """
    Schema for starting a quiz attempt
    """
    quiz_id: str = Field(
        description="ID of the quiz to start an attempt for"
    )



class AttemptResponse(BaseModel):
    """
    Schema returned after starting an attempt
    """
    id: str = Field(
        description="Unique ID of the quiz attempt"
    )

    quiz_id: str = Field(
        description="ID of the quiz"
    )

    attempt_number: int = Field(
        ge=1,
        description="Attempt number for the student"
    )

    status: QuizAttemptStatus = Field(
        description="Current status of the quiz attempt"
    )

    started_at: datetime 
    
    submitted_at: Optional[datetime] = Field(default=None)



class AttemptQuestionResponse(BaseModel):
    """
    Schema for returning a question during a quiz attempt
    """
    id: str
    question_number: int = Field(ge=1, description="Current question number")
    total_questions: int = Field(ge=1, description="Total number of questions in the quiz")
    question: str
    question_type: QuestionType
    options: List[str]
    difficulty: DifficultyLevel
    marks: int
    selected_option: int | None = None
    time_remaining: int
        