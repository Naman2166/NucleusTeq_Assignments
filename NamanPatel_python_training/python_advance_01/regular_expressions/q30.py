"""
Write a pattern to check if a string contains only alphabet
"""

import re


def validate_string(text: str) -> bool:
    """
    Check whether a string contains only alphabets
    """
    pattern = r"^[A-Za-z]+$"
    result = re.match(pattern, text)

    return bool(result)


if __name__ == "__main__":
    text = input("Enter the string: ")

    if validate_string(text):
        print("Valid string")
    else:
        print("Invalid string")