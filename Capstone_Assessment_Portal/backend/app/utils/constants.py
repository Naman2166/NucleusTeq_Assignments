"""
constant values for using across the project
"""

# Roles
class Role:
    ADMIN = "admin"
    STUDENT = "student"

# Common exception Messages
class ExceptionMessage:
    INVALID_TOKEN = "Invalid or expired token received"
    INVALID_ENCRYPTED_PASSWORD = "Invalid encrypted password"
    INVALID_REFRESH_TOKEN = "Invalid refresh token"
    FAILED_TO_LOAD_PRIVATE_KEY = "Failed to load private key"
    ENVIRONMENT_VARIABLE_MISSING = "Environment variable '{variable}' is missing"
    INTERNAL_SERVER_ERROR = "Internal Server Error"

# Auth messages
class AuthMessage:
    INVALID_CREDENTIALS = "Invalid email or password"
    EMAIL_ALREADY_EXISTS = "Email already exists"
    PASSWORD_LENGTH_ERROR = "Password must be between 8 to 30 characters"
    PASSWORD_FORMAT_ERROR = "Password must contain a letter, number and special character (@#$%)"
    ADMIN_ACCESS_REQUIRED = "Admin access required"
    STUDENT_ACCESS_REQUIRED = "Student access required"

# Category messages
class CategoryMessage:
    ALREADY_EXISTS = "Category already exists"
    NOT_FOUND = "Category not found"
    INVALID_ID = "Invalid category ID"

# Quiz messages
class QuizMessage:
    INVALID_PASSING_MARKS = "Passing marks cannot be more than maximum marks"
    ALREADY_EXISTS = "Quiz already exists"
    INVALID_ID = "Invalid Quiz ID"
    NOT_FOUND = "Quiz not found"
    NO_UPDATE_DATA = "No fields provided for update"
    DELETED = "Quiz deleted successfully"