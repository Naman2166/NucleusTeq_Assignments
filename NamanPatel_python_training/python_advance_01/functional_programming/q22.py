"""
Write a recursive function to calculate Fibonacci
"""


def fibonacci(number: int) -> int:
    """
    Calculate Fibonacci number using recursion
    """
    if number <= 1:
        return number

    return fibonacci(number - 1) + fibonacci(number - 2)


if __name__ == "__main__":
    terms = int(input("Enter number of terms: "))

    for index in range(terms):
        print(fibonacci(index), end=" ")