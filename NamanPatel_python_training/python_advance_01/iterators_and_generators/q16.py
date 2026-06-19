"""
Show an example of a built-in generator and iterate over it
"""


def display_list():
    """
    Display names with their index using enumerate
    """
    names = ["Naman", "Patel", "Java", "Python"]

    for index, name in enumerate(names):
        print(index, name)


if __name__ == "__main__":
    display_list()