package com.设计模式.原型模式;

import java.util.ArrayList;
import java.util.List;

/**
 * 浅克隆测试
 * @author rose
 */
public interface Prototype {
    Prototype clone();
}


class ConcretePrototypeA implements Prototype{
    private int age;
    private String name;
    private List hobbies;

    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public List getHobbies() {return hobbies;}
    public void setHobbies(List hobbies) {this.hobbies = hobbies;}

    @Override
    public ConcretePrototypeA clone() {
        ConcretePrototypeA concretePrototypeA = new ConcretePrototypeA();
        concretePrototypeA.setAge(this.age);
        concretePrototypeA.setName(this.name);
        concretePrototypeA.setHobbies(this.hobbies);
        return concretePrototypeA;
    }
}

class Cleient{
    private Prototype prototype;
    public Cleient(Prototype prototype){
        this.prototype=prototype;
    }
    public Prototype startClone(Prototype concretePrototype){
        return concretePrototype.clone();
    }
}

class PrototypeTest{
    public static void main(String[] args) {
        ConcretePrototypeA concretePrototypeA = new ConcretePrototypeA();
        concretePrototypeA.setAge(10);
        concretePrototypeA.setName("prototype");
        List hobbies = new ArrayList<String>();
        concretePrototypeA.setHobbies(hobbies);
        System.out.println(concretePrototypeA);
        Cleient cleient = new Cleient(concretePrototypeA);
        ConcretePrototypeA prototype = (ConcretePrototypeA) cleient.startClone(concretePrototypeA);
        System.out.println(prototype);
        System.out.println("克隆对象中的引用类型地址值"+prototype.getHobbies());
        System.out.println("原对象中的引用类型地址值"+concretePrototypeA.getHobbies());
        System.out.println("对象地址比较"+(prototype.getHobbies()==concretePrototypeA.getHobbies()));
    }
}