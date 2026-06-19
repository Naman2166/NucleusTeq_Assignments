"""
Convert a loop-based program into functional style using map
"""


def calculate_squares_using_loop() -> None:
    """
    Convert numbers into squares using loop
    """
    numbers = [1, 2, 3, 4, 5]
    squares = []

    for number in numbers:
        squares.append(number ** 2)
    
    print("result using loop:", squares)



def calculate_squares_using_map() -> None:
    """
    Convert numbers into squares using map
    """
    numbers = [1, 2, 3, 4, 5]
    squares = list(map(lambda number: number ** 2, numbers))
    print("result using map:", squares)


if __name__ == "__main__":
    calculate_squares_using_loop()
    calculate_squares_using_map()