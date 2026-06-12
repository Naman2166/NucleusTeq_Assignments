""" Convert a tuple into a list and modify it """


def modify_tuple() -> None:
    
    fruits = (10,20,30)

    # converting tuple to list because tuples cannot be modified
    fruits_list = list(fruits)
    
    # modifying the list
    fruits_list.append(100)
    fruits_list[0] = "hello"
    
    print("Original tuple:", fruits)
    print("Modified list:", fruits_list)


if __name__ == "__main__":
    modify_tuple()