"""
Use map() to convert a list of numbers into their squares
"""


def calculate_squares() -> None:
    """
    Convert numbers into their squares using map
    """
    numbers_list = [1, 2, 3, 4, 5]
    squares = list(map(lambda number: number ** 2, numbers_list))
    print("result list:", squares)


if __name__ == "__main__":
    calculate_squares()