""" Append data to an existing file """


def append_data_to_file() -> None:
    """
    Add new data to an existing file
    """
    data = input("Enter data to add: ")

    # opening file in append mode to add data without removing existing content
    with open("name.txt", "a") as file:
        file.write("\n" + data)

    print("Data added successfully.")


if __name__ == "__main__":
    append_data_to_file()