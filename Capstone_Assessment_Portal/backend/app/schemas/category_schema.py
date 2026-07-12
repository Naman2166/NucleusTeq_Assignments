"""
Category request and response schemas
"""

from typing import Optional
from pydantic import BaseModel, Field, field_validator
from app.utils.helper import contains_alphabet


class CategoryCreate(BaseModel):
    """
    Schema for creating a category
    """
    name: str = Field(min_length=3, max_length=50)
    description: str = Field(min_length=5, max_length=200)

    @field_validator("name")
    @classmethod
    def validate_name(cls, value):
        return contains_alphabet("Category name", value)

    @field_validator("description")
    @classmethod
    def validate_description(cls, value):
        return contains_alphabet("Description", value)



class CategoryUpdate(BaseModel):
    """
    Schema for updating a category
    """
    name: Optional[str] = Field(None, min_length=3, max_length=50)
    description: Optional[str] = Field(None, min_length=5, max_length=200)

    @field_validator("name")
    @classmethod
    def validate_name(cls, value):
        if value is None:
            return value
        return contains_alphabet("Category name", value)

    @field_validator("description")
    @classmethod
    def validate_description(cls, value):
        if value is None:
            return value
        return contains_alphabet("Description", value)



class CategoryResponse(BaseModel):
    """
    Schema for returning category details
    """
    id: str
    name: str
    description: str
