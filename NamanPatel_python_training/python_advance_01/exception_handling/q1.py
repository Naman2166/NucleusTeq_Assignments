"""
program that takes a number as input and handles ValueError if the input is not a valid integer
"""


def validate_integer_input() -> None:
    """
    takes integer from user and handles invalid input
    """
    try:
        number = int(input("Enter an integer: "))
        print(f"{number} is valid integer")
    except ValueError:
        # this block will execute when input is not a valid integer
        print("Invalid input, please enter a valid integer")


if __name__ == "__main__":
    validate_integer_input()