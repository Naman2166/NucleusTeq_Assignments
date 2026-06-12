""" Program to create a file and write a name into it """


def write_name_to_file() -> None:
    """
    Create a file and write name "Naman Patel" into it
    """
    with open("name.txt", "w") as file:
        file.write("Naman Patel")

    print("Name written successfully ")


if __name__ == "__main__":
    write_name_to_file()