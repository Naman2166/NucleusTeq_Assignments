""" function to demonstrate the use of default parameter """


def print_message(name: str = "bro") -> None:
    """
    Print a message
    """
    # it prints the default value when no argument is provided
    print("Hello", name)


if __name__ == "__main__":
    print_message()                # function call without any argument
    print_message("Naman")        # function call with an argument