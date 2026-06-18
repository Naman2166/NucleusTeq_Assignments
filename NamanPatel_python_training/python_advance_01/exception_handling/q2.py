"""
Write a program to divide two numbers entered by the user and handle ZeroDivisionError
"""


def divide_numbers() -> None:
    """
    takes two numbers from user and perform division
    """
    try:
        first_number = float(input("Enter the first number: "))
        second_number = float(input("Enter the second number: "))

        result = first_number / second_number
        print(f"Result: {result}")

    except ZeroDivisionError:
        print("Cannot divide by zero")


if __name__ == "__main__":
    divide_numbers()