"""
Write a recursive function to calculate factorial
"""


def calculate_factorial(number: int) -> int:
    """
    Calculate factorial using recursion
    """
    if number == 0 or number == 1:
        return 1

    return number * calculate_factorial(number - 1)


if __name__ == "__main__":
    number = int(input("Enter the number: "))
    factorial = calculate_factorial(number)
    print("factorial:", factorial)