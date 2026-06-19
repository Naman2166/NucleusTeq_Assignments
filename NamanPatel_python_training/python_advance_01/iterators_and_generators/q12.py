"""
Write a generator to produce Fibonacci numbers
"""


def fibonacci_generator(limit: int):
    """
    Generates Fibonacci numbers up to given limit
    """
    first = 0
    second = 1
    
    # generates fibonacci numbers using loop until given limit is reached
    for i in range(limit):
        yield first
        first, second = second, first + second


def print_fibonacci_numbers() -> None:
    """
    Print Fibonacci numbers using a generator
    """
    try:
        limit = int(input("Enter the number of terms: "))

        if limit < 1:
            raise ValueError("Limit must be a positive integer")

        fibonacci_numbers = fibonacci_generator(limit)

        for number in fibonacci_numbers:
            print(number)

    except ValueError:
        print("Invalid input, please enter a positive integer")


if __name__ == "__main__":
    print_fibonacci_numbers()