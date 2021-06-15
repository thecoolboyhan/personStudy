package com.代理.jing;

public class ProGirlJ implements Jing {
    private GirlJing girlJing;

    public ProGirlJ() {
    }

    public ProGirlJ(GirlJing girlJing) {
        this.girlJing = girlJing;
    }


    public void eat() {
        System.out.println("eat前置操作——————————————");
        girlJing.eat();
        System.out.println("eat后置操作——————————————");
    }

    public void bath() {
        System.out.println("bath前置操作——————————————");
        girlJing.bath();
        System.out.println("bath后置操作——————————————");
    }
}
