""" Program to check whether a number is even or odd """


def check_even_or_odd() -> None:
    """
    Take a number and check if it is even or odd
    """
    number = int(input("Enter a number: "))

    # even number always leaves remainder 0 when divided by 2
    if number % 2 == 0:
        print("number is even")
    else:
        print("number is odd")


if __name__ == "__main__":
    check_even_or_odd()