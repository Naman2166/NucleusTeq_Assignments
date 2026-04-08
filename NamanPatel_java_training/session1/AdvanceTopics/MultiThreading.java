package NamanPatel_java_training.session1.AdvanceTopics;

// NOTE :- Explanation of multithreading is given in PDF document

//program to demonstrate multithreading
class MyThread extends Thread {

    //This method contains the task that the thread will perform
    public void run() {
        for (int i=1; i<=5; i++) {
            System.out.println("Thread running :- " + i);
        }
    }
}

public class MultiThreading {
    public static void main(String[] args) {
        
        MyThread t1 = new MyThread();    //creating new Mythread object
        t1.start();                      //start the thread 

        // This loop runs independently and Main thread also runs separately
        for (int i = 1; i <= 5; i++) {
            System.out.println("Main thread :- " + i);
        }
    }
}