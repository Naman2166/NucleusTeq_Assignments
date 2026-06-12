""" Copy content from one file to another """


def copy_file_content() -> None:
    """
    Copying content of one file into another
    """
    with open("name.txt", "r") as file:
        content = file.read()

    # Writing the copied content into the new file
    with open("copy.txt", "w") as copy_file:
        copy_file.write(content)

    print("Content copied successfully")


if __name__ == "__main__":
    copy_file_content()