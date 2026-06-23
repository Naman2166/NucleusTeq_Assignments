"""
Authentication business logic
"""

from app.config.database import db
from app.schemas.user_schema import (UserRegister, UserLogin)
from app.security.password import (hash_password, verify_password)
from app.security.jwt_handler import create_token
from app.utils.constants import STUDENT


class AuthService:

    @staticmethod
    async def register(user: UserRegister):
        """
        Register a new user
        """

        existing_user = await db.users.find_one({"email": user.email})

        if existing_user:  
            raise ValueError("Email already exists")
        
        user_data = user.model_dump()         # it converts user model to dictionary 
        user_data["role"] = STUDENT
        user_data["password"] = hash_password(user.password)

        # user saved to db
        await db.users.insert_one(user_data)

        return {"message": "User registered successfully"}



    @staticmethod
    async def login(user: UserLogin):
        """
        Login user
        """

        existing_user = await db.users.find_one({"email": user.email})

        if not existing_user:
            raise ValueError("Invalid email or password")

        if not verify_password( user.password, existing_user["password"]):
            raise ValueError("Invalid email or password")

        token = create_token(existing_user)

        return {
            "access_token": token,
            "token_type": "bearer"
        }
    