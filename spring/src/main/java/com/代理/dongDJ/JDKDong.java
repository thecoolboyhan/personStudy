package com.代理.dongDJ;


import com.test.Hello;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class JDKDong {
    public static void main(String[] args) {

        final GirlDJ girlDJ = new GirlDJ();
        /**
         * InvocationHandler:拦截结果对于拦截的结果进行处理
         */
        ProGirlDJ proGirlDJ  = (ProGirlDJ) Proxy.newProxyInstance(GirlDJ.class.getClassLoader(), GirlDJ.class.getInterfaces(), new InvocationHandler() {
            /**
             *
             * @param proxy :代理对象
             * @param method: 执行的方法
             * @param args：参数
             * @return
             * @throws Throwable
             */
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName());
                if (method.getName().endsWith("bath")) {
                    System.out.println("偷看前");
                    Object invoke = method.invoke(girlDJ, args);
                    System.out.println("偷看后");
                    return invoke;
                } else {
                    System.out.println("饭前");
                    Object invoke = method.invoke(girlDJ, args);
                    System.out.println("饭后");
                    return invoke;
                }
            }
        });

        proGirlDJ.bath();
        proGirlDJ.eat();
    }
}
