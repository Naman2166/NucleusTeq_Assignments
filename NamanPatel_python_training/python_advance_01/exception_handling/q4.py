"""
Write a program to handle multiple exceptions in a single program
"""


def handle_multiple_exceptions() -> None:
    """
    Handle multiple exceptions in a single program
    """
    try:
        numbers = [10, 20, 30]
        print("numbers:", numbers)

        index = int(input("Enter index: "))
        divisor = int(input("Enter divisor: "))

        result = numbers[index] / divisor
        print(f"Result: {result}")

    except ValueError:
        print("Please enter valid integers")

    except IndexError:
        print("Index is out of range")

    except ZeroDivisionError:
        print("Cannot divide by zero")


if __name__ == "__main__":
    handle_multiple_exceptions()