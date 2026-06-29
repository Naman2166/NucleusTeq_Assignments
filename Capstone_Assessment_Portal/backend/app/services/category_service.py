"""
Category business logic
"""

from app.config.database import db
from app.schemas.category_schema import CategoryCreate, CategoryUpdate
from bson import ObjectId
from bson.errors import InvalidId
from app.utils.logger import logger
from app.exceptions.custom_exceptions import ConflictException, ResourceNotFoundException, BadRequestException
from app.utils.constants import CategoryMessage



async def get_all_categories():
    """
    Get all categories
    """
    logger.info("Fetching all categories")

    categories = await db.categories.find().to_list(length=None)

    for category in categories:
        category["_id"] = str(category["_id"])
    
    logger.info(f"Fetched {len(categories)} categories successfully")

    return categories



async def create_category(category: CategoryCreate):
    """
    Create a new category
    """
    logger.info(f"Creating category: {category.name}")

    existing_category = await db.categories.find_one({"name": category.name.strip()})

    if existing_category:
        raise ConflictException(CategoryMessage.ALREADY_EXISTS)

    # inserting new category in db
    await db.categories.insert_one({
            "name": category.name.strip(),
            "description": category.description.strip()
        })
    
    logger.info(f"Category created successfully: {category.name}")

    return {"message": "Category created successfully"}



async def update_category(category_id: str, category: CategoryUpdate):
    """
    Update an existing category
    """
    logger.info(f"Updating category with id: {category_id}")

    # Validate category id
    try:
        object_id = ObjectId(category_id)
    except InvalidId:
        raise BadRequestException(CategoryMessage.INVALID_ID)

    # Checking if category exists or not
    existing_category = await db.categories.find_one({"_id": object_id})

    if not existing_category:
        raise ResourceNotFoundException(CategoryMessage.NOT_FOUND)

    update_data = {}

    if category.name is not None:
        # checking if new category name already exist or not
        duplicate_category = await db.categories.find_one({
            "name": category.name.strip(),
            "_id": {"$ne": object_id}    
        })

        if duplicate_category:
            raise ConflictException(CategoryMessage.ALREADY_EXISTS)
        
        update_data["name"] = category.name.strip()

    if category.description is not None:
        update_data["description"] = category.description.strip()

    await db.categories.update_one(
       {"_id": object_id},
       {"$set": update_data}
    )

    logger.info(f"Category updated successfully: {category_id}")

    return {"message": "Category updated successfully"}



async def delete_category(category_id: str):
    """
    Delete a category
    """
    logger.info(f"Deleting category with id: {category_id}")

    # Validate category id
    try:
        object_id = ObjectId(category_id)
    except InvalidId:
        raise BadRequestException(CategoryMessage.INVALID_ID)

    # Check if category exists or not
    existing_category = await db.categories.find_one({"_id": object_id})

    if not existing_category:
        raise ResourceNotFoundException(CategoryMessage.NOT_FOUND)

    await db.categories.delete_one({"_id": object_id})

    logger.info(f"Category deleted successfully: {category_id}")

    return {"message": "Category deleted successfully"}


