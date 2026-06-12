""" Program to create a Student class and display student details """


class Student:
    """
    this class represents a student with basic details
    """

    # constructor
    def __init__(self, name: str, age: int, course: str) -> None:
        self.name = name
        self.age = age
        self.course = course

    # function to display details
    def display_details(self) -> None:
        print("Name:", self.name)
        print("Age:", self.age)
        print("Course:", self.course)


if __name__ == "__main__":
    # creating student class object and displaying details
    student = Student("Naman", 22, "Python training")
    student.display_details()