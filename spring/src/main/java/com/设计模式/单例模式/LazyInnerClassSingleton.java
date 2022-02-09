package com.设计模式.单例模式;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 静态内部类饿汉式单例模式
 * @author rose
 */
public class LazyInnerClassSingleton {

    private LazyInnerClassSingleton(){
        //只能通过内部类的静态属性来创建此类
        if (LazyHolder.LAZY!=null){
            new RuntimeException();
        }
    }

    public static final LazyInnerClassSingleton getInstance(){
        return LazyHolder.LAZY;
    }

    private static class LazyHolder{
        private static final LazyInnerClassSingleton LAZY=new LazyInnerClassSingleton();
    }
}

/**
 * 反射破坏上面的单例
 */
class LazyInnerClassSingletonTest{
    public static void main(String[] args) {
        Class<?> aClass = LazyInnerClassSingleton.class;
        try {
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            Object o = declaredConstructor.newInstance();
            Object o1 = declaredConstructor.newInstance();
            System.out.println(o==o1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}