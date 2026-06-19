"""
Create a thread that calculates the sum of numbers from 1 to 100
"""

import threading


def calculate_sum() -> None:
    """
    Calculate sum from 1 to 100
    """
    total = sum(range(1, 101))
    print("Sum:", total)


if __name__ == "__main__":
    thread = threading.Thread(target=calculate_sum)

    thread.start()