package NamanPatel_java_training.session1.OOP;


//Program demonstrating Polymorphism (using method overloading)
class Student {

    //method having only 1 parameter
    void show(int marks) {
        System.out.println("parent class :- ");
        System.out.println("Marks: " + marks);
    }
}

class GraduateStudent extends Student {

    //overloading parent class method (now having 2 parameters)
    void show(int marks, String course) {
        System.out.println("child class :- ");
        System.out.println("Marks: " + marks);
        System.out.println("Course: " + course);
    }
}

public class Polymorphism {
    public static void main(String[] args) {

        GraduateStudent s = new GraduateStudent();

        //calling parent class method by passing 1 argument
        s.show(85);

        System.out.println();

        //calling child class method by passing 2 argument
        s.show(85, "B.Tech");
    }
}