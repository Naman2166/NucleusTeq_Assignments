package NamanPatel_java_training.session1.OOP;


//Program demonstarting inheritence 
//Parent class
class Student {   

    String name;
    int rollNo;
    int marks;

    // constructor
    Student(String name, int rollNo, int marks) {
        this.name = name;         //here 'this' keyword represents current object of the class
        this.rollNo = rollNo;
        this.marks = marks;
    }

    // Displaying student details
    public void show() {
        System.out.println("Parent Class :-");
        System.out.println("Name: " + name);
        System.out.println("Roll No: " + rollNo);
        System.out.println("Marks: " + marks);
    }
}


// Child class
class GraduateStudent extends Student { 

    //additional fields
    String course;           
    int graduationYear;


    // constructor
    GraduateStudent(String name, int rollNo, int marks, String course, int graduationYear) {
        super(name, rollNo, marks);     //'super' keyword is used to call parent class constructor
        this.course = course;
        this.graduationYear = graduationYear;
    }

    // displaying all details
    void display() {
        System.out.println("Child Class :-");
        show();                //calling parent class method
        System.out.println("Course: " + course);
        System.out.println("graduationYear: " + graduationYear);
    }
}


public class Main {
    public static void main(String[] args) {

        // Creating object of child class
        GraduateStudent s = new GraduateStudent("Naman Patel", 101, 85, "B.Tech", 2026);
        
        //calling methods of both classes with object of child class
        s.show();
        System.out.println();
        s.display();   
    }
}