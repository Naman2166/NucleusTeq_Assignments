""" program to take user input and print a formatted message """

def print_user_details():

    """ 
    taking user input 
    """
    name = input("enter your name: ")
    age = int(input("enter your age: "))

    """ 
    printing formatted message 
    """
    print(f"Hello, my name is {name} and I am {age} years old")


if __name__ == "__main__":
    print_user_details()