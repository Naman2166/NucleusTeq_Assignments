package NamanPatel_java_training.session1.Arrays;

import java.util.Scanner;


//program to print average of array
public class ArrayAverage {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking size of array from user
        System.out.print("Enter number of elements: ");
        int num = sc.nextInt();

        int[] arr = new int[num];      //created array of size num
        int sum = 0;         

        //taking array elements from user
        System.out.println("Enter elements:");
        for (int i = 0; i < num; i++) {
            arr[i] = sc.nextInt();
            sum = sum + arr[i];          //Adding each element to sum variable
        }

        // Calculating average
        float avg = (float) sum/num;      // casting to float bcoz average might conatins decimal value

        //displaying result
        System.out.println("Average = " + avg);

    }
}