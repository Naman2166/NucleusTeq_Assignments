package NamanPatel_java_training.session1.Basics;

import java.util.Scanner;

// Program to print fibonacci series upto given number
public class Fibonacci {

    //logic for fibonacci series
    public static void printFibonacci(int num) {
        int firstNum = 0, secondNum = 1;

        for (int i=1; i<=num; i++) {
            System.out.print(firstNum + " ");
            int thirdNum = firstNum + secondNum;    //thirdNum is the sum of firstNum and seconfNum
            
            //updating values for next iteration
            firstNum = secondNum;                       
            secondNum = thirdNum;                  
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking user input
        System.out.print("Enter number of digits: ");
        int num = sc.nextInt();
        
        printFibonacci(num);    //calling method to print series
    }
}