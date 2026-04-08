package NamanPatel_java_training.session1.Basics;

import java.util.Scanner;


// Program to find factorial of given number
public class Factorial {

    //logic for factorial
    public static int findFactorial(int n) {
        int fact = 1;
        for (int i=1; i<=n; i++)
            fact = fact * i;
        return fact;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking user input
        System.out.print("Enter the number to find fcatorial :- ");
        int num = sc.nextInt();
        
        //calling method with user input
        System.out.println("factorial :- " + findFactorial(num));
    }
}