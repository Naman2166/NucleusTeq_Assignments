package NamanPatel_java_training.session1.AdvanceTopics;

// NOTE :- Explanation of interface is given in PDF document

//program to demonstrate interface 
interface Animal {
    void sound();         // method with no body (ie abstract method)
}

class Dog implements Animal {
    
    //overriding sound() method to provide implementation
    public void sound() {        
        System.out.println("Dog barks");
    }
}

public class InterfaceDemo {
    public static void main(String[] args) {

        //calling method with child class object
        Dog d = new Dog();
        d.sound();                 
    }
}