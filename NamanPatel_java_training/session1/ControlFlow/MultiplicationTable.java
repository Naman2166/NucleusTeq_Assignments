package NamanPatel_java_training.session1.ControlFlow;

import java.util.Scanner;


//program to print multiplication table using for loop
public class MultiplicationTable {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking user input
        System.out.print("enter a number :- ");
        int num = sc.nextInt();

        //using for loop to print table
        for(int i = 1; i <= 10; i++) {
           System.out.println(num + " x " + i + " = " + (num * i));
        }
    }
}