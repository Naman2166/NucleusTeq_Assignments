"""Import and use a custom module """

# importing functions from custom_module
from custom_module import add_numbers, subtract_numbers

# taking input from user
first_number = int(input("Enter first number: "))
second_number = int(input("Enter second number: "))

# perform operations using functions from custom_module
print("Sum:", add_numbers(first_number, second_number))
print("Difference:", subtract_numbers(first_number, second_number))