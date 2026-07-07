"""
Test cases for QuestionService
"""

import pytest
from unittest.mock import AsyncMock, MagicMock
from bson import ObjectId
from app.services.question_service import QuestionService
from app.schemas.question_schema import QuestionCreate, QuestionUpdate
from app.exceptions.custom_exceptions import BadRequestException, ResourceNotFoundException
from app.utils.constants import QuestionType, DifficultyLevel, QuestionMessage, Role


@pytest.mark.asyncio
async def test_create_mcq_question_success(mocker):
    """
    Test successful MCQ question creation
    """

    quiz_id = str(ObjectId())

    mocker.patch(
        "app.services.question_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(quiz_id),
            "total_marks": 20,
        },
    )

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_questions_by_quiz",
        new_callable=AsyncMock,
        return_value=[],
    )

    inserted_result = MagicMock()
    inserted_result.inserted_id = ObjectId()

    mock_create = mocker.patch(
        "app.services.question_service.QuestionRepository.create_question",
        new_callable=AsyncMock,
        return_value=inserted_result,
    )

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_question_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": inserted_result.inserted_id,
            "quiz_id": ObjectId(quiz_id),
            "question": "What is Python?",
            "question_type": QuestionType.MCQ,
            "options": ["Java", "Python", "C++", "Go"],
            "correct_answer": 2,
            "difficulty": DifficultyLevel.EASY,
            "tags": [],
            "marks": 5,
        },
    )

    question = QuestionCreate(
        quiz_id=quiz_id,
        question="What is Python?",
        question_type=QuestionType.MCQ,
        options=["Java", "Python", "C++", "Go"],
        correct_answer=2,
        difficulty=DifficultyLevel.EASY,
        tags=[],
        marks=5,
    )

    response = await QuestionService.create_question(question)

    assert response.question == "What is Python?"

    mock_create.assert_awaited_once()



@pytest.mark.asyncio
async def test_create_true_false_question_success(mocker):
    """
    Test successful True/False question creation
    """

    quiz_id = str(ObjectId())

    mocker.patch(
        "app.services.question_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(quiz_id),
            "total_marks": 10,
        },
    )

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_questions_by_quiz",
        new_callable=AsyncMock,
        return_value=[],
    )

    inserted_result = MagicMock()
    inserted_result.inserted_id = ObjectId()

    mocker.patch(
        "app.services.question_service.QuestionRepository.create_question",
        new_callable=AsyncMock,
        return_value=inserted_result,
    )

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_question_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": inserted_result.inserted_id,
            "quiz_id": ObjectId(quiz_id),
            "question": "Python is interpreted",
            "question_type": QuestionType.TRUE_FALSE,
            "options": ["True", "False"],
            "correct_answer": 1,
            "difficulty": DifficultyLevel.EASY,
            "tags": [],
            "marks": 2,
        },
    )

    question = QuestionCreate(
        quiz_id=quiz_id,
        question="Python is interpreted",
        question_type=QuestionType.TRUE_FALSE,
        options=["True", "False"],
        correct_answer=1,
        difficulty=DifficultyLevel.EASY,
        tags=[],
        marks=2,
    )

    response = await QuestionService.create_question(question)

    assert response.question == "Python is interpreted"



@pytest.mark.asyncio
async def test_create_question_quiz_not_found(mocker):
    """
    Test create question when quiz does not exist
    """

    mocker.patch(
        "app.services.question_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    question = QuestionCreate(
        quiz_id=str(ObjectId()),
        question="What is Python?",
        question_type=QuestionType.MCQ,
        options=["A", "B"],
        correct_answer=1,
        difficulty=DifficultyLevel.EASY,
        tags=[],
        marks=5,
    )

    with pytest.raises(ResourceNotFoundException):
        await QuestionService.create_question(question)



@pytest.mark.asyncio
async def test_create_question_marks_exceeded(mocker):
    """
    Test create question when marks exceed quiz total
    """

    quiz_id = str(ObjectId())

    mocker.patch(
        "app.services.question_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(quiz_id),
            "total_marks": 10,
        },
    )

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_questions_by_quiz",
        new_callable=AsyncMock,
        return_value=[
            {"marks": 8},
        ],
    )

    question = QuestionCreate(
        quiz_id=quiz_id,
        question="What is Python?",
        question_type=QuestionType.MCQ,
        options=["A", "B"],
        correct_answer=1,
        difficulty=DifficultyLevel.EASY,
        tags=[],
        marks=5,
    )

    with pytest.raises(BadRequestException):
        await QuestionService.create_question(question)



@pytest.mark.asyncio
async def test_get_question_by_id_admin(mocker):
    """
    Test get question by id for admin
    """

    question_id = str(ObjectId())

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_question_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(question_id),
            "quiz_id": ObjectId(),
            "question": "What is Python?",
            "question_type": QuestionType.MCQ,
            "options": ["A", "B"],
            "correct_answer": 2,
            "difficulty": DifficultyLevel.EASY,
            "tags": [],
            "marks": 5,
        },
    )

    response = await QuestionService.get_question_by_id(
        question_id,
        {"role": Role.ADMIN},
    )

    assert response.correct_answer == 2



@pytest.mark.asyncio
async def test_get_question_by_id_student(mocker):
    """
    Test get question by id for student
    """

    question_id = str(ObjectId())

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_question_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(question_id),
            "quiz_id": ObjectId(),
            "question": "What is Python?",
            "question_type": QuestionType.MCQ,
            "options": ["A", "B"],
            "correct_answer": 2,
            "difficulty": DifficultyLevel.EASY,
            "tags": [],
            "marks": 5,
        },
    )

    response = await QuestionService.get_question_by_id(
        question_id,
        {"role": Role.STUDENT},
)

    assert response.question == "What is Python?"
    assert not hasattr(response, "correct_answer")



@pytest.mark.asyncio
async def test_get_questions_by_quiz_success(mocker):
    """
    Test get questions by quiz
    """

    quiz_id = str(ObjectId())

    mocker.patch(
        "app.services.question_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(quiz_id),
            "total_marks": 20,
        },
    )

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_questions_by_quiz",
        new_callable=AsyncMock,
        return_value=[
            {
                "_id": ObjectId(),
                "quiz_id": ObjectId(quiz_id),
                "question": "Question 1",
                "question_type": QuestionType.MCQ,
                "options": ["A", "B"],
                "correct_answer": 1,
                "difficulty": DifficultyLevel.EASY,
                "tags": [],
                "marks": 5,
            }
        ],
    )

    response = await QuestionService.get_questions_by_quiz(
        quiz_id,
        {"role": Role.ADMIN},
    )

    assert len(response) == 1



@pytest.mark.asyncio
async def test_get_questions_by_quiz_not_found(mocker):
    """
    Test get questions by quiz when quiz does not exist
    """

    mocker.patch(
        "app.services.question_service.QuizRepository.get_quiz_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    with pytest.raises(ResourceNotFoundException):
        await QuestionService.get_questions_by_quiz(
            str(ObjectId()),
            {"role": Role.ADMIN},
        )



@pytest.mark.asyncio
async def test_update_question_success(mocker):
    """
    Test successful question update
    """

    question_id = str(ObjectId())
    quiz_id = ObjectId()

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_question_by_id",
        new_callable=AsyncMock,
        side_effect=[
            {
                "_id": ObjectId(question_id),
                "quiz_id": quiz_id,
                "question": "Old",
                "question_type": QuestionType.MCQ,
                "options": ["A", "B"],
                "correct_answer": 1,
                "difficulty": DifficultyLevel.EASY,
                "tags": [],
                "marks": 5,
            },
            {
                "_id": ObjectId(question_id),
                "quiz_id": quiz_id,
                "question": "Updated",
                "question_type": QuestionType.MCQ,
                "options": ["A", "B"],
                "correct_answer": 1,
                "difficulty": DifficultyLevel.EASY,
                "tags": [],
                "marks": 5,
            },
        ],
    )

    mock_update = mocker.patch(
        "app.services.question_service.QuestionRepository.update_question",
        new_callable=AsyncMock,
    )

    question = QuestionUpdate(question="Updated")

    response = await QuestionService.update_question(
        question_id,
        question,
    )

    assert response.question == "Updated"

    mock_update.assert_awaited_once()


@pytest.mark.asyncio
async def test_update_question_not_found(mocker):
    question_id = str(ObjectId())

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_question_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    with pytest.raises(ResourceNotFoundException) as exception:
        await QuestionService.update_question(
            question_id,
            QuestionUpdate(question="Updated"),
        )

    assert str(exception.value) == QuestionMessage.NOT_FOUND



@pytest.mark.asyncio
async def test_delete_question_success(mocker):
    """
    Test successful question deletion
    """

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_question_by_id",
        new_callable=AsyncMock,
        return_value={
            "_id": ObjectId(),
            "question": "Python",
        },
    )

    mock_delete = mocker.patch(
        "app.services.question_service.QuestionRepository.delete_question",
        new_callable=AsyncMock,
    )

    response = await QuestionService.delete_question(
        str(ObjectId())
    )

    assert response.message == QuestionMessage.DELETED

    mock_delete.assert_awaited_once()



@pytest.mark.asyncio
async def test_delete_question_not_found(mocker):
    """
    Test delete question when question does not exist
    """

    mocker.patch(
        "app.services.question_service.QuestionRepository.get_question_by_id",
        new_callable=AsyncMock,
        return_value=None,
    )

    with pytest.raises(ResourceNotFoundException):
        await QuestionService.delete_question(str(ObjectId()))