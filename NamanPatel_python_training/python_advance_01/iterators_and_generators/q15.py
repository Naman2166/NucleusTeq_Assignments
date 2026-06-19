"""
Process a large dataset using a generator
"""


def generate_data(limit):
    """
    Generate numbers one at a time
    """
    for number in range(1, limit + 1):
        yield number


def process_data():
    """
    Calculate sum of all values from large dataset using generator
    """
    total = 0
    data = generate_data(1000000)

    for number in data:
        total += number

    print("Sum:", total)


if __name__ == "__main__":
    process_data()