"""
constant values for using across the project
"""

from enum import Enum

class Role:
    ADMIN = "admin"
    STUDENT = "student"

class ExceptionMessage:
    INVALID_TOKEN = "Invalid or expired token received"
    INVALID_ENCRYPTED_PASSWORD = "Invalid encrypted password"
    INVALID_REFRESH_TOKEN = "Invalid refresh token"
    FAILED_TO_LOAD_PRIVATE_KEY = "Failed to load private key"
    ENVIRONMENT_VARIABLE_MISSING = "Environment variable '{variable}' is missing"
    INTERNAL_SERVER_ERROR = "Internal Server Error"

class AuthMessage:
    INVALID_CREDENTIALS = "Invalid email or password"
    EMAIL_ALREADY_EXISTS = "Email already exists"
    PASSWORD_LENGTH_ERROR = "Password must be between 8 to 30 characters"
    PASSWORD_FORMAT_ERROR = "Password must contain a letter, number and special character (@#$%)"
    ADMIN_ACCESS_REQUIRED = "Admin access required"
    STUDENT_ACCESS_REQUIRED = "Student access required"
    USER_REGISTERED_SUCCESSFULLY = "User registered successfully"

class CategoryMessage:
    ALREADY_EXISTS = "Category already exists"
    NOT_FOUND = "Category not found"
    INVALID_ID = "Invalid category ID"
    NO_UPDATE_DATA = "No fields provided for update"
    DELETED = "Category deleted successfully"

class QuizMessage:
    INVALID_PASSING_MARKS = "Passing marks cannot be more than maximum marks"
    ALREADY_EXISTS = "Quiz already exists"
    INVALID_ID = "Invalid Quiz ID"
    NOT_FOUND = "Quiz not found"
    NO_UPDATE_DATA = "No fields provided for update"
    DELETED = "Quiz deleted successfully"

class QuestionMessage:
    INVALID_OPTIONS = "MCQ must have between 2 and 4 options"
    INVALID_TRUE_FALSE_OPTIONS = "True/False questions must have 'True' and 'False' as options"
    INVALID_CORRECT_ANSWER = "Correct answer index is invalid"
    MARKS_EXCEEDED = "Question marks exceed the total marks of the quiz"
    NOT_FOUND = "Question not found"
    INVALID_ID = "Invalid question ID"
    DUPLICATE_OPTIONS = "Duplicate options are not allowed"
    EMPTY_OPTION = "Question options cannot be empty"
    DELETED = "Question deleted successfully"    

class QuestionType(str, Enum):
    MCQ = "MCQ"
    TRUE_FALSE = "TRUE_FALSE"

class DifficultyLevel(str, Enum):
    EASY = "Easy"
    MEDIUM = "Medium"
    HARD = "Hard"

class QuizAttemptMessage:
    STARTED = "Quiz attempt started successfully"
    ANSWER_SAVED = "Answer saved successfully"
    SUBMITTED = "Quiz submitted successfully"
    ATTEMPT_ALREADY_IN_PROGRESS = "You already have an active quiz attempt"
    ATTEMPT_ALREADY_SUBMITTED = "Quiz attempt has already been submitted"
    MAX_ATTEMPTS_REACHED = "Maximum number of quiz attempts reached"
    INVALID_ID = "Invalid quiz attempt ID"
    INVALID_QUESTION_INDEX = "Invalid question index"
    NOT_FOUND = "Quiz attempt not found"
    TIME_EXPIRED = "Quiz time has expired, Your attempt has been submitted automatically"

class QuizAttemptStatus(str, Enum):
    IN_PROGRESS = "IN_PROGRESS"
    SUBMITTED = "SUBMITTED"
    TIME_EXPIRED = "TIME_EXPIRED"
