"""
Create two threads that print numbers from 1 to 5
"""

import threading


def print_numbers() -> None:
    """
    Print numbers from 1 to 5
    """
    for number in range(1, 6):
        print(number)


if __name__ == "__main__":
    thread_1 = threading.Thread(target=print_numbers)
    thread_2 = threading.Thread(target=print_numbers)
    
    print("thread 1")
    thread_1.start()

    print("thread 2")
    thread_2.start()