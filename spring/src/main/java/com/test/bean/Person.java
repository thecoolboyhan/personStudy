package com.test.bean;

public class Person {
    private String name;
    private String age;
    private Food food;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Person() {
    }

    public Person(String name, String age, Food food) {
        this.name = name;
        this.age = age;
        this.food = food;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", food=" + food +
                '}';
    }
}
