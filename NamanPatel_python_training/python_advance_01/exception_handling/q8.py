"""
Write a program that handles FileNotFoundError when trying to open a file
"""


def read_file() -> None:
    """
    Open a file and handle FileNotFoundError
    """
    try:
        with open("naman.txt", "r") as file:
            print(file.read())

    except FileNotFoundError:
        print("File not found")


if __name__ == "__main__":
    read_file()