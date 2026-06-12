"""function to return the maximum number from a list """


def find_maximum_number(number_list : list[int]) -> int:
    """
    Return the largest number from the list
    """
    maximum_number = number_list[0]

    # Update the maximum number whenever a larger value is found
    for number in number_list:
        if number > maximum_number:
            maximum_number = number

    return maximum_number


def take_user_input() -> list[int]:
    """
    Take input from user and return a list of integers
    """
    list = input("Enter numbers separated by spaces : ").split()       # it Splits the input into list of string values

    numbers = []
    for value in list:
        numbers.append(int(value))         # converts each string value from list into integer and appends it to the numbers list
    return numbers


if __name__ == "__main__":
    number_list = take_user_input()
    result = find_maximum_number(number_list)
    print("Maximum number:", result)