package NamanPatel_java_training.session1.Arrays;

import java.util.Scanner;


//program to search for specific element using linear search
public class Searching {

    //logic for liner search
    public static int linearSearch(int[] arr, int element) {

        for (int i=0; i<arr.length; i++) {    
            if (arr[i] == element) {
                return i;            // returns index of element if found
            }
        }
        return -1;      // return -1 if element not found
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // taking all inputs from user
        System.out.print("Enter number of elements :- ");
        int n = sc.nextInt();

        int[] arr = new int[n];

        System.out.println("Enter elements :- ");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        System.out.print("Enter element to search :- ");
        int element = sc.nextInt();
        
        // Calling method
        int result = linearSearch(arr, element);

        // displaying result
        if (result == -1) {
            System.out.println("Element not found");
        } else {
            System.out.println("Element found at index :- " + result);
        }
    }
}