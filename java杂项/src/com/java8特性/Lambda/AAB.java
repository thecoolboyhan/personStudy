package com.java8特性.Lambda;

import java.util.ArrayList;
import java.util.List;

/**
 * 利用简单的策略模式，来规定打印什么
 */
public class AAB {
    public static void main(String[] args) {
        ArrayList<Apple> apples = new ArrayList<>();
        apples.add(new Apple("1","a"));
        apples.add(new Apple("2","s"));
        apples.add(new Apple("3","d"));
        apples.add(new Apple("4","f"));
        apples.add(new Apple("5","g"));

        System.out.println("颜色：");
        PPAPC ppapc = new PPAPC();
        prettyPrint(apples,ppapc);
        System.out.println("名字：");
        PPAPN ppapn = new PPAPN();
        prettyPrint(apples,ppapn);
        System.out.println("ALL：");
        PPAPA ppapa = new PPAPA();
        prettyPrint(apples,ppapa);

        prettyPrint(apples, Apple::getName);
    }

    public static void prettyPrint(List<Apple> apples,PPAP ppap ){
        for (Apple apple : apples) {
            String out = ppap.test(apple);
            System.out.println(out);
        }
    }
}

interface PPAP{
    String test(Apple apple);
}

class PPAPN implements PPAP{

    @Override
    public String test(Apple apple) {
        return apple.getName();
    }
}

class PPAPC implements PPAP{

    @Override
    public String test(Apple apple) {
        return apple.getColor();
    }
}

class PPAPA implements PPAP{

    @Override
    public String test(Apple apple) {
        return apple.getColor()+"+"+apple.getName();
    }
}

class Apple{
    private String color;
    private String name;

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public Apple(String color, String name){
        this.color=color;
        this.name=name;
    }

}