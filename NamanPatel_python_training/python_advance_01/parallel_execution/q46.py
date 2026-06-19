"""
Calculate squares using multiprocessing
"""

from multiprocessing import Process


def calculate_square(number: int) -> None:
    """
    Calculate square of a number
    """
    print(f"Square of {number}: {number ** 2}")


if __name__ == "__main__":

    numbers = [1, 2, 3, 4, 5]
    processes = []
    
    # Creating a process for every number and run it in parallel
    for number in numbers:
        process = Process(target=calculate_square, args=(number,))
        processes.append(process)
        process.start()