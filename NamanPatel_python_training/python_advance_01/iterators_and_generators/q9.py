"""
Create an iterator for a list and print elements using next()
"""


def print_list_elements() -> None:
    """
    prints list elements using iterator and next() function
    """
    numbers = [10, 20, 30, 40]
    
    iterator = iter(numbers)

    print(next(iterator))
    print(next(iterator))
    print(next(iterator))
    print(next(iterator))


if __name__ == "__main__":
    print_list_elements()