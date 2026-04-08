package NamanPatel_java_training.session1.ControlFlow;


//program to calculate sum of even number from 1 to 10 using while loop
public class SumOfEvenNumbers {

    public static void main(String[] args) {
        int i = 1, sum = 0;

        while (i <= 10) {         //loop runs 10 times
            if (i % 2 == 0) {     // taking only even number to add to sum variable
               sum = sum + i;
            }
            
            i++;      // increasing value of i by 1
        }

        // displaying result
        System.out.println("Sum is :- " + sum);
    }
}