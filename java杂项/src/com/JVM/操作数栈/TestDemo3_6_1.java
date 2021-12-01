package com.JVM.操作数栈;

/**
 * 判断最后x的值
 *
 * @author rose
 */
public class TestDemo3_6_1 {
    public static void main(String[] args) {
        int i = 0;
        int x = 0;
        while (i<10){
            x=x++;
            i++;
        }
        //结果是0
        System.out.println(x);
    }
}
