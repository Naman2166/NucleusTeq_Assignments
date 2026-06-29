"""
function to decrypt password using private key
"""

import base64
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import padding
from app.utils.logger import logger


# Path to private key 
PRIVATE_KEY_PATH = "app/keys/private_key.pem"

with open(PRIVATE_KEY_PATH, "rb") as key_file:
    private_key = serialization.load_pem_private_key(
        key_file.read(),
        password=None
    )


def decrypt_password(encrypted_password: str) -> str:
    """
    Decrypts the encrypted password received from the frontend
    """
    logger.info("Decrypting user password")
    decrypted_password = private_key.decrypt(
        base64.b64decode(encrypted_password),
        padding.PKCS1v15()
    )

    return decrypted_password.decode()