""" Count the frequency of characters in a string using a dictionary """


def count_character_frequency() -> None:
    """
    Count how many times each character appears in a string
    """
    text = input("Enter a string: ")

    character_frequency = {}

    # here dictionary stores each character as key and its frequency as value
    for character in text:
        if character in character_frequency:
            character_frequency[character] += 1
        else:
            character_frequency[character] = 1

    print("Character frequency:", character_frequency)


if __name__ == "__main__":
    count_character_frequency()