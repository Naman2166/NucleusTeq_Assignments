"""
Create a password validation program using regex (minimum length, one digit, one special character)
"""

import re


def validate_password(password: str) -> bool:
    """
    Validate password based on given rules
    """
    
    # Password must be 8 to 16 characters long and contain at least 1 digit and 1 special character
    pattern = r"^(?=.*\d)(?=.*[@#$%^&+=!]).{8,16}$"
    result = re.match(pattern, password)

    return bool(result)


if __name__ == "__main__":
    password = input("Enter password: ")

    if validate_password(password):
        print("Valid password")
    else:
        print("Invalid password")