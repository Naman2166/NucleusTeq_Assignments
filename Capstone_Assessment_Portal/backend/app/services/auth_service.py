"""
Authentication business logic
"""

from app.config.database import db
from app.schemas.user_schema import (LoginResponse, UserRegister, UserLogin, RefreshTokenResponse)
from app.security.password import (hash_password, verify_password, validate_password)
from app.security.jwt_handler import (create_access_token, create_refresh_token, verify_token)
from app.security.decryption import decrypt_password
from app.utils.logger import logger
from app.exceptions.custom_exceptions import ConflictException
from app.exceptions.custom_exceptions import UnauthorizedException
from app.utils.constants import (Role, ExceptionMessage)


PUBLIC_KEY_PATH = "app/keys/public_key.pem"


class AuthService:

    @staticmethod
    async def register(user: UserRegister):
        """
        Register a new user
        """
        logger.info(f"Registering user: {user.email}")
        existing_user = await db.users.find_one({"email": user.email})

        if existing_user:  
            raise ConflictException(ExceptionMessage.EMAIL_ALREADY_EXISTS)
        
        # decrypt the encrypted password
        original_password = decrypt_password(user.password) 
        
        # Validating password
        validate_password(original_password)
        
        # converting user model to dictionary
        user_data = user.model_dump()     

        # hashing original password before saving to db
        user_data["password"] = hash_password(original_password)    
        user_data["role"] = Role.STUDENT

        # user saved to db
        await db.users.insert_one(user_data)
        logger.info(f"User registered successfully: {user.email}")

        return {"message": "User registered successfully"}



    @staticmethod
    async def login(user: UserLogin):
        """
        Login user
        """
        logger.info(f"Login attempt: {user.email}")
        existing_user = await db.users.find_one({"email": user.email})

        if not existing_user:
            logger.warning(f"Login failed. User not found: {user.email}")
            raise UnauthorizedException(ExceptionMessage.INVALID_CREDENTIALS)
        
        original_password = decrypt_password(user.password)

        if not verify_password(original_password, existing_user["password"]):
            raise UnauthorizedException(ExceptionMessage.INVALID_CREDENTIALS)

        access_token = create_access_token(existing_user)
        refresh_token = create_refresh_token(existing_user)

        logger.info(f"Login successful: {user.email}")

        return LoginResponse(
            access_token = access_token,
            refresh_token = refresh_token,
            role = existing_user["role"],
            token_type = "bearer"
        )
    


    @staticmethod
    async def regenerate_access_token(refresh_token: str):
        """
        Regenerate access token using refresh token
        """
        logger.info("Refresh token request received")
        payload = verify_token(refresh_token)

        if payload["type"] != "refresh":
            logger.warning("Invalid refresh token received")
            raise UnauthorizedException(ExceptionMessage.INVALID_REFRESH_TOKEN)

        access_token = create_access_token({
            "_id": payload["user_id"],
            "email": payload["email"],
            "role": payload["role"]
        })

        logger.info(f"Access token regenerated for: {payload['email']}")

        return RefreshTokenResponse(
            access_token = access_token,
            token_type = "bearer"
        )
            


    @staticmethod
    async def get_public_key():
        """
        Get public key
        """
        logger.info("Public key requested")

        with open(PUBLIC_KEY_PATH, "r") as file:
            public_key = file.read()

        return {"publicKey": public_key}