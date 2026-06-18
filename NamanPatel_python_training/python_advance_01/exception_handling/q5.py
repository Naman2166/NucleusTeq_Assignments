"""
Write a program that catches all exceptions and prints the error message
"""


def catch_all_exceptions() -> None:
    """
    Catch all exceptions and display the error message
    """
    try:
        number = int(input("Enter a number: "))
        result = 10 / number
        print(f"Result: {result}")

    except Exception as error:
        # this block will execute for any exception that occurs in the try block
        print(f"Error: {error}")


if __name__ == "__main__":
    catch_all_exceptions()