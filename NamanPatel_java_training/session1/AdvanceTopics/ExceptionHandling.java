package NamanPatel_java_training.session1.AdvanceTopics;

//program to handle exceptions using try-catch block
public class ExceptionHandling {
    public static void main(String[] args) {

        try {
            int a = 10/0;          // here error will occur
        } 
        catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero");
        }
        finally{
            System.out.println("Exception handled");
        }
    }
}