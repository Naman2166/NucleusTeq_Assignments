""" Print the multiplication table of a number """


def print_multiplication_table() -> None:
    """
    Print the multiplication table of a number entered by user
    """
    number = int(input("Enter a number: "))

    for count in range(1, 11):
        print(f"{number} x {count} = {number * count}")


if __name__ == "__main__":
    print_multiplication_table()