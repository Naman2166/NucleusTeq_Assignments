"""
Write a lambda function to find the square of a number
"""


def calculate_square(number: int) -> int:
    """
    Calculate square of a number using lambda function
    """
    square = lambda number: number ** 2
    return square(number)


if __name__ == "__main__":
    number = int(input("Enter the number:"))
    result = calculate_square(number)
    print("Square:", result)