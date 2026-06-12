"""
Demonstrate polymorphism using different classes with the same method name
"""


class Dog:
    def sound(self) -> None:
        print("Dog barks")


class Cat:
    def sound(self) -> None:
        print("Cat meows")


if __name__ == "__main__":
    dog = Dog()
    cat = Cat()

    # calling same method name on different objects
    dog.sound()
    cat.sound()