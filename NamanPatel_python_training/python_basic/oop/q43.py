"""
Program to implement encapsulation using private variables in a Bank class 
"""


class Bank:
    """
    this class represents a bank account with private balance
    """

    def __init__(self, balance: float) -> None:
        self.__balance = balance
    
    # function to add money
    def deposit(self, amount: float) -> None:
        self.__balance += amount

    # function to get balance
    def get_balance(self) -> float:
        return self.__balance


if __name__ == "__main__":
    account = Bank(1000)
    account.deposit(500)

    print("Balance:", account.get_balance())