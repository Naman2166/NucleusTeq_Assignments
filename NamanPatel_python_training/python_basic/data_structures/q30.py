""" Perform union, intersection and difference on two sets """


def perform_set_operations() -> None:
   
    first_set = {1, 2, 3, 4, 5}
    second_set = {4, 5, 6, 7, 8}

    print("first_set:", first_set)
    print("second_set:", second_set)

    # union combines all unique elements from both sets
    print("Union:", first_set.union(second_set))

    # intersection gives the common elements in both sets
    print("Intersection:", first_set.intersection(second_set))

    # difference gives elements present only in the first set
    print("Difference:", first_set.difference(second_set))


if __name__ == "__main__":
    perform_set_operations()