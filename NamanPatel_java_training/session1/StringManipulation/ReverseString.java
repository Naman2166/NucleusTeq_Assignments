package NamanPatel_java_training.session1.StringManipulation;

import java.util.Scanner;


//program to reverse string
public class ReverseString {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking user input
        System.out.print("Enter a string :- ");
        String str = sc.nextLine();

        String reversed = "";        

        // loop from last to first character and add each character to 'reversed' string
        for (int i=str.length()-1; i>=0; i--) {
            reversed = reversed + str.charAt(i); 
        }

        //displaying result
        System.out.println("Reversed string :- " + reversed);

    }
}