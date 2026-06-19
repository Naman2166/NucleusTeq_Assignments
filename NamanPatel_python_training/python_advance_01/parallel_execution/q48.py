"""
Convert a normal function into parallel execution using ProcessPoolExecutor
"""

from concurrent.futures import ProcessPoolExecutor


def calculate_square(number: int) -> int:
    """
    Calculate square of a number
    """
    return number ** 2


if __name__ == "__main__":
    numbers = [1, 2, 3, 4, 5]

    with ProcessPoolExecutor() as executor:
        results = executor.map(calculate_square, numbers)

    print("Result:", list(results))