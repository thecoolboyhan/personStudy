package com.设计模式.单例模式;

/**
 * 单例的两种写法
 * @author rose
 */
public class HungrySingleton {
    //利用静态属性创建对象。
    private static final HungrySingleton HUNGRY_SINGLETON=new HungrySingleton();

    private HungrySingleton(){}

    public static HungrySingleton getInstance(){
        return HUNGRY_SINGLETON;
    }
}
