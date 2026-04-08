package NamanPatel_java_training.session1.StringManipulation;

import java.util.Scanner;
import java.util.Arrays;


//program to check whether strings are anagram or not
public class Anagram {

    //logic to check anagram
    public static boolean isAnagram(String str1, String str2) {

        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();

        if (str1.length() != str2.length()) {   // if lengths are different then not an anagram
            return false;
        }

        // Converting both string to char array
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();

        // Sorting both arrays
        Arrays.sort(arr1);
        Arrays.sort(arr2);

        // checking whether both arrays are equal or not
        return Arrays.equals(arr1, arr2);
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Taking user input
        System.out.print("Enter first string :- ");
        String str1 = sc.nextLine();

        System.out.print("Enter second string :- ");
        String str2 = sc.nextLine();

        // Calling method inside if-else to check and display result
        if (isAnagram(str1, str2)) {
            System.out.println("Strings are anagrams");
        } else {
            System.out.println("Strings are not anagrams");
        }
    }
}