"""
Use filter() to extract even numbers from a list
"""


def extract_even_numbers() -> None:
    """
    Extract even numbers using filter
    """
    numbers_list = [1, 2, 3, 4, 5, 6]
    even_numbers = list(filter(lambda number: number % 2 == 0, numbers_list))
    print("list of even numbers:", even_numbers)


if __name__ == "__main__":
    extract_even_numbers()