"""
Category request and response schemas
"""

from typing import Optional
from pydantic import BaseModel, Field


class CategoryCreate(BaseModel):
    """
    Schema for creating a category
    """
    name: str = Field(min_length=3, max_length=50)
    description: str = Field(min_length=5, max_length=200)


class CategoryUpdate(BaseModel):
    """
    Schema for updating a category
    """
    name: Optional[str] = Field(None, min_length=3, max_length=50)
    description: Optional[str] = Field(None, min_length=5, max_length=200)


class CategoryResponse(BaseModel):
    """
    Schema for returning category details
    """
    id: str
    name: str
    description: str