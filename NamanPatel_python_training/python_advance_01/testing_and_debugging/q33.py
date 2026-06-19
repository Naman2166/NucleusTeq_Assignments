"""
Write pytest test cases for a function that checks whether a number is prime
"""


def is_prime(number: int) -> bool:
    """
    Check whether a number is prime
    """
    if number < 2:
        return False

    for divisor in range(2, number):
        if number % divisor == 0:
            return False

    return True


def test_prime_number():
    assert is_prime(7) == True


def test_non_prime_number():
    assert is_prime(8) == False


def test_number_less_than_two():
    assert is_prime(1) == False


if __name__ == "__main__":
    number = int(input("Enter a number: "))
    print(is_prime(number))