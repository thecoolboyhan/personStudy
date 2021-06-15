package com.代理.jing;

public class TestJ {
    public static void main(String[] args) {
        GirlJing girlJing = new GirlJing();
        Jing proGirlJ = new ProGirlJ(girlJing);
        proGirlJ.eat();
        proGirlJ.bath();
    }
}
