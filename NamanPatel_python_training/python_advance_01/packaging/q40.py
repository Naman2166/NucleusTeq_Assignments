"""
Create a package for mathematical operations and use it.
"""

# import functions from math_operations package
from math_operations.add import add
from math_operations.subtract import subtract
from math_operations.multiply import multiply
from math_operations.divide import divide

print("Addition:", add(10, 5))
print("Subtraction:",subtract(10, 5))
print("Multiplication:",multiply(10, 5))
print("Division:",divide(10, 5))