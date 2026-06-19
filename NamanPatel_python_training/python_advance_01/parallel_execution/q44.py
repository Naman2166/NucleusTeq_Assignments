"""
Create multiple threads to simulate file downloading
"""

import threading
import time


def download_file(file_name: str) -> None:
    """
    Simulate file download
    """
    print(f"Downloading {file_name}...")

    time.sleep(2)
    
    print(f"{file_name} downloaded")


if __name__ == "__main__":
    thread_1 = threading.Thread(target=download_file, args=("file1.pdf",))
    thread_2 = threading.Thread(target=download_file, args=("file2.pdf",))
    thread_3 = threading.Thread(target=download_file, args=("file3.pdf",))

    thread_1.start()
    time.sleep(1)
    thread_2.start()
    thread_3.start()