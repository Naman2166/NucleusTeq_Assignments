"""
Demonstrate the use of join() method in threading
"""

import threading
import time


def display_message() -> None:
    """
    Display a message after a delay
    """
    time.sleep(3)
    print("Thread completed")


if __name__ == "__main__":
    thread = threading.Thread(target=display_message)

    thread.start()

    # Wait for thread to finish execution
    thread.join()

    print("program completed")