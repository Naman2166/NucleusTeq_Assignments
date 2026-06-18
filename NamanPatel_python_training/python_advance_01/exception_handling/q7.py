"""
Create a custom exception called AgeException and raise it if age is less than 18
"""


class AgeException(Exception):
    """
    Custom exception for age less than 18
    """
    pass


def check_age() -> None:
    """
    Check whether the entered age is valid or not
    """
    try:
        age = int(input("Enter your age: "))

        # raise an exception if age is less than 18
        if age < 18:
            raise AgeException("Age must be 18 or above")
        
        print("Age is valid")

    except AgeException as error:
        print(error)

    except ValueError:
        print("Please enter a valid age")    


if __name__ == "__main__":
    check_age()