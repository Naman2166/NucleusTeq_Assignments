"""
helper functions to use accross application
"""

from bson import ObjectId
from bson.errors import InvalidId
from app.utils.logger import logger
from app.exceptions.custom_exceptions import BadRequestException
import re


def validate_object_id(id: str, error_message: str) -> ObjectId:
    """
    Validate MongoDB ObjectId
    """
    try:
        object_id = ObjectId(id)
    except InvalidId:
        logger.warning(error_message)
        raise BadRequestException(error_message)

    return object_id


def contains_alphabet(field_name: str, value: str) -> str:
    """
    Validate that the value contains at least one alphabet
    """
    if not re.search(r"[A-Za-z]", value):
        raise ValueError(f"{field_name} Must contain at least one alphabet")
    return value
