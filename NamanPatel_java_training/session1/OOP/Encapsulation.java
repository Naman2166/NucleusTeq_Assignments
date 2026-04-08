package NamanPatel_java_training.session1.OOP;

/*  
    NOTE :- 
    - Explanation of Encapsulation is given in PDF document
    - This file contains only code
*/


//Program demonstrating Encapsulation
class Student {

    // hidden data
    private String name;
    private int marks;

    // setter method (to set new values)
    public void setData(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    // getter methods (to get current values)
    public String getName() {
        return name;
    }

    public int getMarks() {
        return marks;
    }

}

public class Encapsulation {
    public static void main(String[] args) {
        Student s = new Student();

        // calling both methods
        s.setData("Naman", 85);
        System.out.println("Name: " + s.getName());
        System.out.println("Marks: " + s.getMarks());
    }
}