"""
 Create a package with two modules and include an __init__.py file.
"""

# import functions from custom_package
from custom_package.greetings import say_hello
from custom_package.calculations import add, subtract

print(say_hello("Naman"))
print(add(10,20))
print(subtract(10,5))