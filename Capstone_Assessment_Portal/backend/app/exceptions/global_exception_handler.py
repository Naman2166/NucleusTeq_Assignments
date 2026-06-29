"""
Global exception handlers for custom exceptions
"""

from fastapi import FastAPI, Request, status
from fastapi.responses import JSONResponse
from app.utils.logger import logger
from app.exceptions.custom_exceptions import (
    BadRequestException,
    ConflictException,
    ResourceNotFoundException,
    UnauthorizedException,
    ForbiddenException,
)


def create_error_response(status_code: int, details: str):
    """
    Common JSON response for all exceptions
    """
    return JSONResponse(
        status_code = status_code,
        content = {"detail": details}
    )


def register_exception_handlers(app: FastAPI):
    """
    Register all custom exception handlers
    """
    
    @app.exception_handler(BadRequestException)
    async def bad_request_exception_handler(request: Request, exception: BadRequestException):
        """
        Handles bad request exceptions
        """
        logger.error(str(exception))
        return create_error_response(status.HTTP_400_BAD_REQUEST, str(exception))


    @app.exception_handler(UnauthorizedException)
    async def unauthorized_exception_handler(request: Request, exception: UnauthorizedException):
        """
        Handles unauthorized exceptions
        """
        logger.error(str(exception))
        return create_error_response(status.HTTP_401_UNAUTHORIZED, str(exception))


    @app.exception_handler(ForbiddenException)
    async def forbidden_exception_handler(request: Request, exception: ForbiddenException):
        """
        Handles forbidden exceptions
        """
        logger.error(str(exception))
        return create_error_response(status.HTTP_403_FORBIDDEN, str(exception))


    @app.exception_handler(ResourceNotFoundException)
    async def resource_not_found_exception_handler(request: Request, exception: ResourceNotFoundException):
        """
        Handles resource not found exceptions
        """
        logger.error(str(exception))
        return create_error_response(status.HTTP_404_NOT_FOUND, str(exception))


    @app.exception_handler(ConflictException)
    async def conflict_exception_handler(request: Request, exception: ConflictException):
        """
        Handles conflict exceptions
        """
        logger.error(str(exception))
        return create_error_response(status.HTTP_409_CONFLICT, str(exception))


    @app.exception_handler(Exception)
    async def global_exception_handler(request: Request, exception: Exception):
        """
        Handles all unexpected exceptions
        """
        logger.exception(f"Unhandled exception: {exception}")
        return create_error_response(
            status.HTTP_500_INTERNAL_SERVER_ERROR,
            "Internal Server Error"
        )
