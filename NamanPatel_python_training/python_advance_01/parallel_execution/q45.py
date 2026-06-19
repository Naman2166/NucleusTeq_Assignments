"""
Create two processes that print their Process IDs
"""

from multiprocessing import Process
import os


def display_process_id(process_number: int) -> None:
    """
    Print process ID
    """
    print(f"Process {process_number} ID: {os.getpid()}")


if __name__ == "__main__":
    process_1 = Process(target=display_process_id, args=(1,))
    process_2 = Process(target=display_process_id, args=(2,))

    process_1.start()
    process_2.start()