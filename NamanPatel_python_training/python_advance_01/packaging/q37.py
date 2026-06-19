"""
Create a module with two utility functions and import it into another Python file.
"""

# import functions from utility.py
from utility import add, subtract

print("Addition:", add(10, 5))
print("Subtraction:", subtract(10, 5))