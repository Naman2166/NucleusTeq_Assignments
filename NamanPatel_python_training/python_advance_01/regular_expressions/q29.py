"""
Replace multiple spaces in a string with a single space using re.sub()
"""

import re


def replace_spaces(sentence: str) -> str:
    """
    Replace multiple spaces in a sentence with a single space
    """
    result = re.sub(r"\s+", " ", sentence)

    return result


if __name__ == "__main__":
    sentence = input("Enter the string: ")
    result = replace_spaces(sentence)
    print(result)