package NamanPatel_java_training.session1.Basics;

import java.util.Scanner;

public class AreaCalculation {
    
    // circle area logic
    public static double circle(double r) {
        return Math.PI * r * r;
    }

    // rectangle area logic
    public static double rectangle(double l, double b) {
        return l * b;
    }

    // triangle area logic
    public static double triangle(double b, double h) {
        return 0.5 * b * h;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select shape :- 1.circle  2.rectangle  3.triangle");
        int num = sc.nextInt();

        switch(num){
            case 1:
                System.out.print("Enter radius: ");
                double r = sc.nextDouble();
                System.out.println("Area = " + circle(r));
                break;

            case 2:
                System.out.print("Enter length: ");
                double l = sc.nextDouble();
                System.out.print("Enter width: ");
                double w = sc.nextDouble();
                System.out.println("Area = " + rectangle(l, w));
                break;

            case 3:
                System.out.print("Enter base: ");
                double b = sc.nextDouble();
                System.out.print("Enter height: ");
                double h = sc.nextDouble();
                System.out.println("Area = " + triangle(b, h));
                break;

            default:
                System.out.println("Invalid choice");
        }
    }
}