"""Create a tuple and access its elements."""


def access_tuple_elements() -> None:
    
    tuple = ("Apple", "Banana", 10, True)

    # Accessing elements using index
    print("Tuple:", tuple)
    print("First element:", tuple[0])
    print("Second element:", tuple[2])
    print("Third element:", tuple[3])


if __name__ == "__main__":
    access_tuple_elements()