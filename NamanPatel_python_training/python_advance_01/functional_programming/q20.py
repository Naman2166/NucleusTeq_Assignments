"""
Use reduce() to find the product of all elements in a list
"""

from functools import reduce


def calculate_product() -> None:
    """
    Find the product of all elements using reduce
    """
    numbers = [1, 2, 3, 4, 5]
    product = reduce(lambda x, y: x * y, numbers)
    print("product:", product)


if __name__ == "__main__":
    calculate_product()