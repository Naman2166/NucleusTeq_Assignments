package NamanPatel_java_training.session1.Arrays;

import java.util.Scanner;


//program to sort array using bubble sort
public class Sorting {

    //bubble sort logic
    public static void bubbleSort(int[] arr) {

        for (int i=0; i<arr.length-1; i++) {            // number of iterations
            for (int j=0; j<arr.length-1-i; j++) {      // inner loop is used for comapring values

                // swapping if current element is greater than next
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
       
        //taking size of array from user
        System.out.print("Enter number of elements: ");
        int n = sc.nextInt();

        int[] arr = new int[n]; 
    
        //taking array elements from user
        System.out.println("Enter elements:");
        for (int i=0; i<n; i++) {
            arr[i] = sc.nextInt();
        }

        // Calling bubbleSort method 
        bubbleSort(arr);
        
        // displaying sorted array(using for-each loop)
        System.out.println("Sorted array :- ");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}