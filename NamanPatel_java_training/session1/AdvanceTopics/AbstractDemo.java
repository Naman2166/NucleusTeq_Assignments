package NamanPatel_java_training.session1.AdvanceTopics;

// NOTE :- Explanation of abstract class is given in PDF document

//program to demonstrate abstraction using abstract class
abstract class Animal {

    abstract void sound();        // abstract method

    void eat() {                  // normal method
        System.out.println("Animal eats food");
    }
}


class Dog extends Animal {
 
    //overriding sound() method to provide implementation
    void sound() { 
        System.out.println("Dog barks");
    }
}


public class AbstractDemo {
    public static void main(String[] args) {

        //calling methods
        Dog d = new Dog();
        d.sound();
        d.eat();
    }
}