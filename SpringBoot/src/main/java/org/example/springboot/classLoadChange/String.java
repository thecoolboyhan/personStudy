package org.example.springboot.classLoadChange;

public class String{
    char[] aaa;
    public String (char[] aa){
        this.aaa=aa;
    }

    public void bb(){
        for (char c : aaa) {
            System.out.println(c);
        }
    }
}
