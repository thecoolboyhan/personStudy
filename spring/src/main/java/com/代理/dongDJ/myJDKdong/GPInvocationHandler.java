package com.代理.dongDJ.myJDKdong;

import java.lang.reflect.Method;

/**
 * 自定义JDK动态代理需要的接口
 * @author rose
 */
public interface GPInvocationHandler {
    /**
     * 自定义的invoke方法，可以让代理方法实现这个接口来执行功能。
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
