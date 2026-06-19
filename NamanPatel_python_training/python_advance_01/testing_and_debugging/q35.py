"""
Use pdb breakpoints inside a loop and inspect variable values
"""

import pdb


def display_numbers(limit: int) -> None:
    """
    Display numbers using a loop
    """
    for number in range(1, limit + 1):
        # Program stops here and we can check variable values
        pdb.set_trace()
        print(number)


if __name__ == "__main__":
    display_numbers(5)