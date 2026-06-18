"""
Create a function that raises a ValueError if a number is negative
"""


def check_positive_number(number: int) -> None:
    """
    Raise a ValueError if the number is negative
    """
    if number < 0:
        raise ValueError("Number cannot be negative")


def validate_number() -> None:
    """
    Check whether the entered number is positive
    """
    try:
        number = int(input("Enter a number: "))
        check_positive_number(number)
        print("number is valid")

    except ValueError as error:
        print(error)


if __name__ == "__main__":
    validate_number()