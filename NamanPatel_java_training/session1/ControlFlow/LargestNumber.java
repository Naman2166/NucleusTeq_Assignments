package NamanPatel_java_training.session1.ControlFlow;

import java.util.Scanner;


//program to find largest number among three using conditional statement
public class LargestNumber {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking user input
        System.out.print("Enter 1st number: ");
        int a = sc.nextInt();
        System.out.print("Enter 2nd number: ");
        int b = sc.nextInt();
        System.out.print("Enter 3rd number: ");
        int c = sc.nextInt();


        //displaying result based on conditions
        if (a >= b && a >= c) {       // a is greater than or equal to both b and c
            System.out.println("Largest number is :- " + a);
        } 
        else if (b >= a && b >= c) {    // b is greater than or equal to both a and c
            System.out.println("Largest number is :- " + b);
        } 
        else {                        // if above conditions are false then c is the largest
            System.out.println("Largest number is :- " + c);
        }
    }
}