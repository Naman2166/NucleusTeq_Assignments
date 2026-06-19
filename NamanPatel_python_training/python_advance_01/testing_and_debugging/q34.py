"""
Create a function with a logical bug and use pdb to identify the issue
"""

import pdb


def calculate_average(total_marks: int, subject_count: int) -> float:
    """
    Calculate average marks
    """
    pdb.set_trace()

    # Logical bug
    average = total_marks * subject_count   

    return average


if __name__ == "__main__":
    result = calculate_average(500, 5)
    print("Average:", result)