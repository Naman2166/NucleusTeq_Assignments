"""
Common request and response schemas
"""

from pydantic import BaseModel


class MessageResponse(BaseModel):
    """
    General response schema for returningm a messages
    """
    message: str