"""
Write a custom iterator class that returns numbers from 1 to N
"""


class NumberIterator:
    """
    Iterator that returns numbers from 1 to N
    """

    # constructor
    def __init__(self, limit: int) -> None:
        self.limit = limit
        self.current = 1

    # it returns the iterator object
    def __iter__(self):
        return self

    # it returns the next value in the sequence
    def __next__(self) -> int:
        if self.current > self.limit:
            raise StopIteration

        number = self.current
        self.current += 1
        return number


def demonstrate_iterator() -> None:
    """
    Print numbers from 1 to N using a custom iterator
    """
    try:
        limit = int(input("Enter the limit: "))
        if limit < 1:
            raise ValueError("Limit must be a positive integer")
        
        number_iterator = NumberIterator(limit)

        for number in number_iterator:
            print(number)

    except ValueError:
        print("invalid input, Please enter a positive integer")

if __name__ == "__main__":
    demonstrate_iterator()