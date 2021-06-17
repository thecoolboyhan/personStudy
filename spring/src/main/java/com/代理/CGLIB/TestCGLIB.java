package com.代理.CGLIB;

public class TestCGLIB {
    public static void main(String[] args) {
        GirlCG girlCG = new GirlCG();
        CGLibFactory cgLibFactory = new CGLibFactory(girlCG);
        GirlCG proxy = (GirlCG) cgLibFactory.createProxy();
        proxy.eat();
        proxy.bath();
    }
}
