""" 
Create variables of type int, float, string and boolean
Print their types using type() 
"""

def print_variable_types() -> None:
    """
    Create variables and display their data types
    """
    integer_value = 10
    float_value = 10.5
    string_value = "Python"
    boolean_value = True

    # displaying data types of variable 
    print(type(integer_value))
    print(type(float_value))
    print(type(string_value))
    print(type(boolean_value))


if __name__ == "__main__":
    print_variable_types()
