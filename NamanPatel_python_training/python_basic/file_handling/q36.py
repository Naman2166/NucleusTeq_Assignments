""" Read a file and count words, lines and characters """


def count_file_contents() -> None:
    """
    Read a file and print number of words, lines, and characters
    """
    with open("name.txt", "r") as file:
        content = file.read()

    # word count
    word_count = len(content.split())

    # line count
    line_count = len(content.splitlines())

    # characters count
    character_count = len(content)

    print("Words:", word_count)
    print("Lines:", line_count)
    print("Characters:", character_count)


if __name__ == "__main__":
    count_file_contents()