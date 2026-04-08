package NamanPatel_java_training.session1.Basics;

import java.util.Scanner;


// Program to check whether the number is even or odd
public class EvenOdd {

    //logic for checking even and odd
    public static void checkEvenOdd(int num) {
        if (num % 2 == 0)
            System.out.println("number is even");
        else
            System.out.println("number is odd");
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("enter a number: ");
        int num = sc.nextInt();    // taking user input

        checkEvenOdd(num);       //calling checkEvenOdd method with user input
    }
}