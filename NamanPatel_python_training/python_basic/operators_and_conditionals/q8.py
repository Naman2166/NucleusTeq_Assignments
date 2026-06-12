""" Check whether a number is positive, negative or zero """


def check_number() -> None:
    """
    Check whether the entered number is positive, negative or zero
    """
    number = float(input("Enter a number: "))

    if number > 0:
        print("number is positive")
    elif number < 0:
        print("number is negative")
    else:
        print("number is zero")


if __name__ == "__main__":
    check_number()