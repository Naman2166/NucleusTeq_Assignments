"""
Category routes
"""

from fastapi import APIRouter, Depends
from app.schemas.category_schema import CategoryCreate, CategoryUpdate
from app.services.category_service import (create_category, get_all_categories, update_category, delete_category,)
from app.security.auth import get_current_user, require_admin
from app.utils.logger import logger


router = APIRouter(
    prefix="/categories",
    tags=["Category"]
)


@router.get("/")
async def get_all(user = Depends(get_current_user)):
    """
    get all categories
    """
    logger.info(f"Get all categories requested by {user['email']}")
    return await get_all_categories()


@router.post("/")
async def create(category: CategoryCreate, user=Depends(require_admin)):
    """
    create new category
    """
    logger.info(f"Create category: {category.name} requested by {user['email']}")
    return await create_category(category)


@router.put("/{category_id}")
async def update(
    category_id: str,
    category: CategoryUpdate,
    user=Depends(require_admin)
):
    """
    update category
    """
    logger.info(f"Update category with ID: {category_id} requested by {user['email']}")
    return await update_category(category_id, category)


@router.delete("/{category_id}")
async def delete(category_id: str, user=Depends(require_admin)):
    """
    Delete a category
    """
    logger.info(f"Delete category with ID: {category_id} requested by {user['email']}")
    return await delete_category(category_id)