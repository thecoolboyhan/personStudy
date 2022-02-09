package com.设计模式.单例模式;

/**
 * 枚举形式单例
 * @author rose
 */

public enum EnumSingleton {
    INSTANCE;
    private Object data;
    public Object getData(){
        return data;
    }
    public void setData(Object data){
        this.data=data;
    }
    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
}

class ThreadLocalSingleton{
    private static final ThreadLocal<ThreadLocalSingleton> THREAD_LOCAL_SINGLETON =
            new ThreadLocal<ThreadLocalSingleton>(){
                @Override
                protected ThreadLocalSingleton initialValue() {
                    return new ThreadLocalSingleton();
                }
            };

    private ThreadLocalSingleton(){}

    public static ThreadLocalSingleton getInstance(){
        return THREAD_LOCAL_SINGLETON.get();
    }
}