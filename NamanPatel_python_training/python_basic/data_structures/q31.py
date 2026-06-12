""" Remove duplicates from a list using a set """


def remove_duplicates() -> None:
    
    numbers = [10, 20, 30, 20, 40, 10, 50, 30]

    # converting list to set bcoz set keeps only unique values
    unique_set = set(numbers)
    unique_list = list(unique_set)

    print("Original list:", numbers)
    print("List after removing duplicates:", unique_list)


if __name__ == "__main__":
    remove_duplicates()