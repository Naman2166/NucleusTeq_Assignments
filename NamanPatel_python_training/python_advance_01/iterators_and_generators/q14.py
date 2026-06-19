"""
Explain the difference between iterator and generator with a small example
"""

# Explanation of theory questions are provided in PDF sent with submission form and also uploaded inside python_advance_01 folder
# This file contains example program of iterator and generator

# Iterator
class NumberIterator:
    """
    returns number till given limit using iterator
    """
    def __init__(self, limit):
        self.limit = limit
        self.current = 1

    def __iter__(self):
        return self

    def __next__(self):
        if self.current > self.limit:
            raise StopIteration

        number = self.current
        self.current += 1
        return number

print("--- Iterator ---")
iterator = NumberIterator(3)
for num in iterator:
    print(num)



# generator 
def number_generator(limit):
    """
    generates number till given limit using generator
    """
    for number in range(1, limit + 1):
        yield number

print("--- Generator ---")
generator = number_generator(3)
for num in generator:
    print(num)