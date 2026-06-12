""" Program to implement inheritance using Person and Employee classes """


class Person:
    """
    this class represents a person with basic details
    """

    def __init__(self, name: str, age: int) -> None:
        self.name = name
        self.age = age

    def display_person_details(self) -> None:
        print("Name:", self.name)
        print("Age:", self.age)



class Employee(Person):
    """
    this class represents an employee inherited from Person
    """

    def __init__(self, name: str, age: int, employee_id: int) -> None:
        super().__init__(name, age)              # reuseing  parent class constructor
        self.employee_id = employee_id

    def display_employee_details(self) -> None:
        self.display_person_details()             # calling function from parent class
        print("Employee ID:", self.employee_id)


if __name__ == "__main__":
    employee = Employee("Naman", 22, 101)
    employee.display_employee_details()