package com.代理.dongDJ;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author rose
 */
public class JDKMeipo implements InvocationHandler {
    //被代理对象
    private Object target;
    //通过反射创建代理类被代理后的对象
    public Object getInstance(Object target){
        this.target=target;
        Class<?> aClass = target.getClass();
        //传入被代理类的类加载器，和它所实现的接口
        return Proxy.newProxyInstance(aClass.getClassLoader(),aClass.getInterfaces(),this);
    }
    //设置对于代理类的代理方法要执行什么操作
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object invoke = method.invoke(this.target, args);
        after();
        return invoke;
    }

    private void before(){
        System.out.println("我是媒婆，已经确认你的需求");
        System.out.println("开始物色");
    }
    private void after(){
        System.out.println("如果觉得合适就办事");
    }
}
