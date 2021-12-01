package com.aop;

/**
 * @author rose
 */
public class Animal {
    private String height;
    private float weight;

    public Animal() {
    }

    public void eat() throws InterruptedException {

        long start = System.currentTimeMillis();

        System.out.println("I can eat...");
        Thread.sleep(2000);
        long end = System.currentTimeMillis();
        System.out.println("1执行时间"+(end-start)/1000f+"s");
    }

    public void run() throws InterruptedException {
        long start = System.currentTimeMillis();

        System.out.println("I can run...");
        Thread.sleep(200);
        long end = System.currentTimeMillis();
        System.out.println("2执行时间"+(end-start)/1000f+"s");
    }

    public static void main(String[] args) {
        Animal animal = new Animal();
        try {
            animal.run();
            animal.eat();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
