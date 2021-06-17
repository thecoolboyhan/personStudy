package com.代理.CGLIB;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibFactory implements MethodInterceptor {

    private Object object;

    public CGLibFactory() {
    }

    public CGLibFactory(Object object) {
        super();
        this.object = object;
    }

    public Object createProxy(){
        //增强器
        Enhancer enhancer = new Enhancer();
        //这是增强器的父类
        enhancer.setSuperclass(object.getClass());
        //回调
        enhancer.setCallback(this);
        return enhancer.create();
    }


    public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("前");
        method.invoke(object,objects);
        System.out.println("后");
        return null;
    }
}
