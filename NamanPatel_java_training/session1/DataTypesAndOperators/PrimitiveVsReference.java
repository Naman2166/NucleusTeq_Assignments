package NamanPatel_java_training.session1.DataTypesAndOperators;

/*  
    NOTE :- 
    - Explanation of Primitive and Reference dataType is given in PDF document
    - This file contains only code
*/


public class PrimitiveVsReference {

    // Example of Primitive datatype
    public static void Primitive() {
        int a = 10;
        int b = a;               // value of 'a' is copied to 'b'
        b = 20;        

        System.out.println(a);   // output = 10 ('a' does not affect by changing value of 'b')
        System.out.println(b);   // output = 20
    }


    // Example of Referenec datatype
    public static void Reference() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = arr1;         // reference of 'arr1' is copied to 'arr2' (now both points to same memory location)
        arr2[0] = 99;

        System.out.println(arr1[0]);  // output :- 99 (here arr1 gets affected by changing arr2)
        System.out.println(arr2[0]);  // output :- 99
    }


    public static void main(String[] args) {
        System.out.println("Primitive Datatype Example :- ");
        Primitive();
        System.out.println("Reference Datatype Example :- ");
        Reference();
    }
}