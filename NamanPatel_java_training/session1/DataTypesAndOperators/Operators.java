package NamanPatel_java_training.session1.DataTypesAndOperators;

import java.util.Scanner;


// Program to demonstrate the use of different operators
public class Operators {

    // Arithmetic operators
    public static void arithmetic(int a, int b) {
        System.out.println("Arithmetic Operators :- ");
        System.out.println("Addition: " + (a + b));
        System.out.println("Subtraction: " + (a - b));
        System.out.println("Multiplication: " + (a * b));
        System.out.println("Division: " + (a / b));
        System.out.println("Modulus: " + (a % b));      // it is used for taking remainder
        System.out.println();
    }


    // Relational operators
    public static void relational(int a, int b) {
        System.out.println("Relational Operators :- ");
        System.out.println("a > b: " + (a > b));
        System.out.println("a < b: " + (a < b));
        System.out.println("a == b: " + (a == b));      // it is used for comapring values
        System.out.println("a != b: " + (a != b));     
        System.out.println(); 
    }


    // Logical operators
    public static void logical(int a, int b) {
        System.out.println("Logical Operators :- ");
        System.out.println("(a > 0 && b > 0): " + (a > 0 && b > 0));     //both the conditon must be true
        System.out.println("(a > 0 || b < 0): " + (a > 0 || b < 0));     //any of the condition must be true 
        System.out.println("!(a > b): " + !(a > b));                    // this operator reverse the result
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking user input
        System.out.print("Enter 1st number :- ");
        int a = sc.nextInt();
        System.out.print("Enter 2nd number :- ");
        int b = sc.nextInt();

        //calling all the methods
        //here all the methods are defined using static keyword, so it can be called without creating object 
        System.out.println();
        arithmetic(a, b);        
        relational(a, b);
        logical(a, b);
    }
}