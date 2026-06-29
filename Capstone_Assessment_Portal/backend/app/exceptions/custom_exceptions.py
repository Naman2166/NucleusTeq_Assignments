"""
Custom exceptions
"""


class BadRequestException(Exception):
    """
    Raised when the request is invalid
    """
    pass


class UnauthorizedException(Exception):
    """
    Raised when authentication fails
    """
    pass


class ForbiddenException(Exception):
    """
    Raised when the user does not have permission
    """
    pass


class ResourceNotFoundException(Exception):
    """
    Raised when a requested resource is not found
    """
    pass


class ConflictException(Exception):
    """
    Raised when a resource conflict occurs
    """
    pass