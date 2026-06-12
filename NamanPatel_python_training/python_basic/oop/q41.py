""" Create a Car class with a constructor """


class Car:
    
    # constructor
    def __init__(self, name: str, company: str, year: int) -> None:
        self.name = name
        self.company = company
        self.year = year
    
    # function to display car details
    def display_details(self) -> None:
        print("Name:", self.name)
        print("Company:", self.company)
        print("Year:", self.year)


if __name__ == "__main__":
    car = Car("Hyundai", "Creta", 2024)
    car.display_details()