package com.设计模式.单例模式;

/**
 * @author rose
 */
public class HungrySingleton1 {
    //利用静态代码块创建
    private static final HungrySingleton1 HUNGRY_SINGLETON_1;

    static {
        HUNGRY_SINGLETON_1=new HungrySingleton1();
    }

    private HungrySingleton1(){}

    public static HungrySingleton1 getInstance(){
        return HUNGRY_SINGLETON_1;
    }
}
