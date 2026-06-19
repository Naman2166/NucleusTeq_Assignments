"""
Use re.findall() to extract all words starting with a capital letter
"""

import re


def extract_capital_words(sentence: str) -> list[str]:
    """
    Extracts all words starting with a capital letter
    """
    words = re.findall(r"\b[A-Z]\w*", sentence)

    return words


if __name__ == "__main__":
    sentence = input("Enter the sentence: ")
    words = extract_capital_words(sentence)
    print(words)