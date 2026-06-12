""" program to create student dictionary and access its values """


def student_details() -> None:
    
    # student dictionary
    student = {
        "name": "Naman",
        "age": 22,
        "course": "python training"
    }

    # accessing values using their keys
    print("Name:", student["name"])
    print("Age:", student["age"])
    print("Course:", student["course"])


if __name__ == "__main__":
    student_details()