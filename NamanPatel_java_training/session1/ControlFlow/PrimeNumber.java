package NamanPatel_java_training.session1.ControlFlow;

import java.util.Scanner;


//program to check given no. is prime or not using if-else
public class PrimeNumber {

    // logic to check prime number
    public static boolean isPrime(int num) {
        
        if (num <= 1) {          // Numbers less than 1 are not prime
            return false;     
        }

        for (int i = 2; i <= Math.sqrt(num); i++) {    // Loop from 2 to sqrt(num) for efficiency
            if (num % i == 0) {            // if num is divisible by i, it is not prime
                return false;
            }
        }

        return true;   
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking user input
        System.out.print("enter a number :- ");
        int num = sc.nextInt();

        // Checking result by calling method inside if-else
        if (isPrime(num)) {
            System.out.println("number is prime");
        } else {
            System.out.println("number is not prime");
        }
    }
}