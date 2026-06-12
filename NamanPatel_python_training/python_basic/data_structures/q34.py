""" Merge two dictionaries """


def merge_dictionaries() -> None:
    
    dictionary_one = {
        "name": "Naman",
        "age": 21
    }

    dictionary_two = {
        "course": "Computer Science",
        "city": "Indore"
    }

    # combining both dictionaries using union operator
    merged_dictionary = dictionary_one | dictionary_two

    print("Dictionary One:", dictionary_one)
    print("Dictionary Two:", dictionary_two)
    print("Merged dictionary:", merged_dictionary)


if __name__ == "__main__":
    merge_dictionaries()