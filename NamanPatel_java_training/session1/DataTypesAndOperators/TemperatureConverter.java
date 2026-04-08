package NamanPatel_java_training.session1.DataTypesAndOperators;

import java.util.Scanner;


//Program to convert temperature from celcius to Fahrenheit and vice versa
public class TemperatureConverter {

    // logic for Celsius to Fahrenheit
    public static double celsiusToFahrenheit(double c) {     // here i haved used double datatype for taking decimal values also
        return (c * 9/5) + 32;
    }

    // logic for Fahrenheit to Celsius
    public static double fahrenheitToCelsius(double f) {
        return (f - 32) * 5/9;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //taking user input
        System.out.println("1. Celsius to Fahrenheit");
        System.out.println("2. Fahrenheit to Celsius");
        System.out.println("Choose option by entering number :- ");
        int num = sc.nextInt();

        //calling method according to user input
        if (num == 1) {
            System.out.print("Enter temperature in Celsius :- ");
            double c = sc.nextDouble();
            System.out.println("Fahrenheit :- " + celsiusToFahrenheit(c));
        } 
        else if (num == 2) {
            System.out.print("Enter temperature in Fahrenheit :- ");
            double f = sc.nextDouble();
            System.out.println("Celsius :- " + fahrenheitToCelsius(f));
        } 
        else {
            System.out.println("Invalid choice");
        }
    }
}