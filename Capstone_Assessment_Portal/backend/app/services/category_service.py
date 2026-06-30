"""
Category business logic
"""

from app.config.database import db
from app.schemas.category_schema import CategoryCreate, CategoryResponse, CategoryUpdate
from app.schemas.common_schema import MessageResponse
from bson import ObjectId
from bson.errors import InvalidId
from app.utils.logger import logger
from app.exceptions.custom_exceptions import ConflictException, ResourceNotFoundException, BadRequestException
from app.utils.constants import CategoryMessage


def category_helper(category: dict) -> dict:
    """
    Converts MongoDB category document into response dictionary
    """
    category_response = {
        "id": str(category["_id"]),
        "name": category["name"],
        "description": category["description"],
    }
    return category_response


class CategoryService:

    @staticmethod
    async def get_all_categories() -> list[CategoryResponse]:
        """
        Get all categories
        """
        logger.info("Fetching all categories")

        categories = await db.categories.find().to_list(length=None)

        response = []

        for category in categories:
            response.append(CategoryResponse(**category_helper(category)))
    
        logger.info(f"Fetched {len(categories)} categories successfully")

        return response



    @staticmethod
    async def create_category(category: CategoryCreate) -> CategoryResponse:
        """
        Create a new category
        """
        logger.info(f"Creating category: {category.name}")
    
        existing_category = await db.categories.find_one({"name": category.name.strip()})
    
        if existing_category:
            raise ConflictException(CategoryMessage.ALREADY_EXISTS)

        result = await db.categories.insert_one({
            "name": category.name.strip(),
            "description": category.description.strip()
        })
        
        created_category = await db.categories.find_one({"_id": result.inserted_id})
        
        logger.info(f"Category created successfully: {created_category['name']}")

        response = CategoryResponse(**category_helper(created_category))
    
        return response



    @staticmethod
    async def update_category(category_id: str, category: CategoryUpdate) -> CategoryResponse:
        """
        Update an existing category
        """
        logger.info(f"Updating category with id: {category_id}")

        # Validate category id
        try:
            object_id = ObjectId(category_id)
        except InvalidId:
            raise BadRequestException(CategoryMessage.INVALID_ID)

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

        if not update_data:
            raise BadRequestException(CategoryMessage.NO_UPDATE_DATA)
    
        await db.categories.update_one(
           {"_id": object_id},
           {"$set": update_data}
        )

        updated_category = await db.categories.find_one({"_id": object_id}) 

        logger.info(f"Category updated successfully: {category_id}")

        response = CategoryResponse(**category_helper(updated_category))
    
        return response



    @staticmethod
    async def delete_category(category_id: str) -> MessageResponse:
        """
        Delete a category
        """
        logger.info(f"Deleting category with id: {category_id}")
    
        try:
            object_id = ObjectId(category_id)
        except InvalidId:
            raise BadRequestException(CategoryMessage.INVALID_ID)

        existing_category = await db.categories.find_one({"_id": object_id})

        if not existing_category:
            raise ResourceNotFoundException(CategoryMessage.NOT_FOUND)

        await db.categories.delete_one({"_id": object_id})

        logger.info(f"Category deleted successfully: {category_id}")

        response = MessageResponse(message=CategoryMessage.DELETED)
    
        return response


