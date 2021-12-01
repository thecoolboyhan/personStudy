package com.JVM.获取各种类加载器;

/**
 * 几种方法的调用的分析
 * @author rose
 */
public class Demo3_9 {

    public Demo3_9(){}

    private void test1(){}

    private final void test2(){}

    public void test3(){}

    public static void test4(){}

    public static void main(String[] args) {
        Demo3_9 demo3_9 = new Demo3_9();
        demo3_9.test1();
        demo3_9.test2();
        demo3_9.test3();
        Demo3_9.test4();
    }
}