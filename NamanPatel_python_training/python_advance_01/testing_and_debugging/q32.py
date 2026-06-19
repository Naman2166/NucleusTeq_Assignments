"""
Write pytest test cases for a function that adds two numbers
"""


def add_numbers(first_number: int, second_number: int) -> int:
    """
    Add two numbers
    """
    return first_number + second_number


def test_add_positive_numbers():
    assert add_numbers(10, 20) == 30


def test_add_negative_numbers():
    assert add_numbers(-5, -3) == -8


def test_add_zero():
    assert add_numbers(0, 5) == 5


if __name__ == "__main__":
    result = add_numbers(20,30)
    print("Addition:", result)
    