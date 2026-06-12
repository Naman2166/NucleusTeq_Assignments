""" Search a word in a file """


def search_word_in_file() -> None:
    """
    Search for a word in a file
    """
    word = input("Enter the word to search: ")

    with open("name.txt", "r") as file:
        content = file.read()

    # checking whether the word exists in the file or not
    if word in content:
        print("word found in the file")
    else:
        print("word not found in the file")


if __name__ == "__main__":
    search_word_in_file()