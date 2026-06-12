"""
Take two numbers and print sum, difference, multiplication, and division
"""

def print_arithmetic_operations() -> None:
    """
    Taking two numbers as input and print arithmetic operations
    """
    first_number = float(input("Enter first number: "))
    second_number = float(input("Enter second number: "))

    print("sum: ", first_number + second_number)
    print("difference: ", first_number - second_number)
    print("multiplication: ", first_number * second_number)
    print("division: ", first_number / second_number)


if __name__ == "__main__":
    print_arithmetic_operations()