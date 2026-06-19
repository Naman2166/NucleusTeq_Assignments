"""
Extract all numbers from a string using regular expressions
"""

import re


def extract_numbers() -> None:
    """
    Extract all numbers from a string
    """
    text = "My name is Naman and my birth date is 21 july 2003"
    numbers = re.findall(r"\d+", text)
    print("Extracted number:", numbers)


if __name__ == "__main__":
    extract_numbers()