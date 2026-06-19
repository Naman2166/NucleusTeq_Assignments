"""
Validate a 10-digit mobile number using regular expressions
"""

import re


def validate_mobile_number(mobile_number: str) -> bool:
    """
    Validate a 10-digit mobile number
    """
    pattern = r"^\d{10}$"
    result = re.match(pattern, mobile_number)
    
    return bool(result)


if __name__ == "__main__":
    mobile_number = input("Enter mobile number: ")

    if validate_mobile_number(mobile_number):
        print("Valid mobile number")
    else:
        print("Invalid mobile number")