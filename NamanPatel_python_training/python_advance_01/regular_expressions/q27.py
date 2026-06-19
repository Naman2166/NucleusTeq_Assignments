"""
Use re.search() to check whether a word exists in a sentence
"""

import re


def check_word(sentence: str, word: str) -> None:
    """
    Check whether a word exists in a sentence
    """

    if re.search(word, sentence, re.IGNORECASE):
        print("Word found")
    else:
        print("Word not found")


if __name__ == "__main__":
    sentence = input("Enter the sentence: ")
    word = input("Enter the word to find: ")
    check_word(sentence, word)