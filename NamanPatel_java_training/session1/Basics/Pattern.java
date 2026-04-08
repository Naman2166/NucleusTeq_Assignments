package NamanPatel_java_training.session1.Basics;

import java.util.Scanner;


// Program to print pattern
public class Pattern {

    //logic for printing traingle
    public static void triangle(int n) {
        for (int i=1; i<=n; i++) {
            for (int j=1; j<=i; j++)
                System.out.print("* ");
            System.out.println();
        }
    }

    //logic for printing square
    public static void square(int n) {
        for (int i=1; i<=n; i++) {
            for (int j=1; j<=n; j++)
                System.out.print("* ");
            System.out.println();
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking user input
        System.out.println("Select the shape by entering number :- 1.Triangle  2.Square ");
        int num = sc.nextInt();
        System.out.print("Select number of rows :- ");
        int rows = sc.nextInt();
        
        //calling method according to user choice
        switch (num) {
        case 1:
            triangle(rows);
            break;

        case 2:
            square(rows);
            break;

        default:
            System.out.println("Invalid choice");
}
    
    }
}