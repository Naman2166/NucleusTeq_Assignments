"""
Write a generator expression to generate even numbers from 1 to 50
"""


def generate_even_numbers() -> None:
    """
    Print even numbers from 1 to 50 using a generator expression
    """

    even_numbers = (number for number in range(1, 51) if number % 2 == 0)

    for number in even_numbers:
        print(number)


if __name__ == "__main__":
    generate_even_numbers()