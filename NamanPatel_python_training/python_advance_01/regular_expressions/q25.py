"""
Validate an email address using regular expressions
"""

import re


def validate_email(email: str) -> bool:
    """
    Validate email address
    """
    # Checks if the email has a valid format (example: naman123@gmail.com)
    pattern = r"^[\w\.-]+@[\w\.-]+\.\w+$"
    result = re.match(pattern, email)
    
    return bool(result)


if __name__ == "__main__":
    email = input("Enter email: ")

    if validate_email(email):
        print("Valid email")
    else:
        print("Invalid email")