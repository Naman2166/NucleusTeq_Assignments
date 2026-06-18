"""
Write a program using try-except-else-finally to read a number from a file and print its square
"""


def read_number_from_file(filename: str) -> int:
    """
    Read a number from a file
    """
    with open(filename, "r") as file:
        number = int(file.read())
        return number


def read_and_print_square() -> None:
    """
    Read a number from a file and print its square
    """

    try:
        number = read_number_from_file("number.txt")

    except FileNotFoundError:
        print("File not found")

    except ValueError:
        print("File does not contain a valid integer")

    else:
        print(f"Square of {number} is {number ** 2}")

    finally:
        print("Program finished")


if __name__ == "__main__":
    read_and_print_square()