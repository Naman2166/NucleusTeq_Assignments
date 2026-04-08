package NamanPatel_java_training.session1.AdvanceTopics;

import java.io.File;
import java.util.Scanner;


//program to read data from text file
public class ReadDataFromFile {
    public static void main(String[] args) {

        try {
            // Created File object with file path
            File file = new File("NamanPatel_java_training/session1/AdvanceTopics/demo.txt");  
            Scanner sc = new Scanner(file);      // Scanner is used to read data from file

            // it reads line by line and prints it
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            sc.close();         //Closing scanner
        } 
        catch (Exception e) {
            System.out.println("File not found");
        }
    }
}