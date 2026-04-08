package NamanPatel_java_training.session1.StringManipulation;

import java.util.Scanner;

//program to count vowels in a string
public class CountVowels {

    // logic to count vowels
    public static int countVowels(String str) {

        int count = 0;              //to store no. of vowels in string
        str = str.toLowerCase();

        for (int i = 0; i < str.length(); i++) {     // Loop through each character
            char ch = str.charAt(i);

            // Checking if character is a vowel
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                count++; 
            }
        }
        return count;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking user input
        System.out.print("Enter a string :- ");
        String str = sc.nextLine();

        // calling method
        int result = countVowels(str);
        System.out.println("Number of vowels :- " + result);
    }
}