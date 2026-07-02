"""
Authentication business logic
"""

from app.schemas.user_schema import (LoginResponse, UserRegister, UserLogin, RefreshTokenResponse)
from app.security.password import (hash_password, verify_password, validate_password)
from app.security.jwt_handler import (create_access_token, create_refresh_token, verify_token)
from app.security.decryption import decrypt_password
from app.utils.logger import logger
from app.exceptions.custom_exceptions import ConflictException
from app.exceptions.custom_exceptions import (UnauthorizedException, ResourceNotFoundException)
from app.utils.constants import (Role, ExceptionMessage, AuthMessage)
from app.schemas.common_schema import MessageResponse
from app.repositories.user_repository import UserRepository


PUBLIC_KEY_PATH = "app/keys/public_key.pem"


class AuthService:

    @staticmethod
    async def register(user: UserRegister):
        """
        Register a new user
        """
        logger.info(f"Registering user: {user.email}")
        existing_user = await UserRepository.get_user_by_email(user.email)

        if existing_user:  
            raise ConflictException(AuthMessage.EMAIL_ALREADY_EXISTS)
        
        original_password = decrypt_password(user.password) 
        
        validate_password(original_password)
        
        # converting user model to dictionary
        user_data = user.model_dump()     

        user_data["password"] = hash_password(original_password)    
        user_data["role"] = Role.STUDENT

        await UserRepository.create_user(user_data)
        logger.info(f"User registered successfully: {user.email}")

        response = MessageResponse(message=AuthMessage.USER_REGISTERED_SUCCESSFULLY)
    
        return response



    @staticmethod
    async def login(user: UserLogin):
        """
        Login user
        """
        logger.info(f"Login attempt: {user.email}")
        existing_user = await UserRepository.get_user_by_email(user.email)

        if not existing_user:
            logger.warning(f"Login failed. User not found: {user.email}")
            raise ResourceNotFoundException(AuthMessage.INVALID_CREDENTIALS)
        
        original_password = decrypt_password(user.password)

        if not verify_password(original_password, existing_user["password"]):
            raise UnauthorizedException(AuthMessage.INVALID_CREDENTIALS)

        access_token = create_access_token(existing_user)
        refresh_token = create_refresh_token(existing_user)

        logger.info(f"Login successful: {user.email}")

        response =  LoginResponse(
            access_token = access_token,
            refresh_token = refresh_token,
            role = existing_user["role"],
            token_type = "bearer"
        )
        
        return response
    


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

        response = RefreshTokenResponse(
            access_token = access_token,
            token_type = "bearer"
        )

        return response
            


    @staticmethod
    async def get_public_key():
        """
        Get public key
        """
        logger.info("Public key requested")

        with open(PUBLIC_KEY_PATH, "r") as file:
            public_key = file.read()

        response  = {"publicKey": public_key}

        return response 