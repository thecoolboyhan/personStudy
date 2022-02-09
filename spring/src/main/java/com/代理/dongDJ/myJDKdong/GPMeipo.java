package com.代理.dongDJ.myJDKdong;

import com.代理.dongDJ.Customer;
import com.代理.dongDJ.Person;

import java.lang.reflect.Method;

public class GPMeipo implements GPInvocationHandler{

    private Object target;

    public Object getInstance(Object target){
        this.target=target;
        //通过传入的接口，拿到class文件
        Class<?> aClass = target.getClass();
        //getInterfaces():获取此class实例，实现的所有接口(这里只实现了一个接口)
        //给自定义的生成代码方法传入自定义的类加载器
        return GPProxy.newProxyInstance(new GPClassLoader(),aClass.getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        method.invoke(target,args);
        after();
        return null;
    }

    private void before(){
        System.out.println("这是我的自定义JDK动态代理");
        System.out.println("开始");
    }
    private void after(){
        System.out.println("代理结束");
    }
}

//测试自定义类加载器
class TestMyJDKDong{
    public static void main(String[] args) {
        //给代理工具传入被代理类所实现的接口
        Person object = (Person) new GPMeipo().getInstance(new Customer());
        System.out.println(object.getClass());
        object.findLove();
    }
}