package com.设计模式.装饰者模式;

//抽象装试者类
public abstract class BattercakeDecotator extends Battercake {

    //静态代理，委派
    private Battercake battercake;

    public BattercakeDecotator(Battercake battercake){
        this.battercake=battercake;
    }
    protected abstract void doSomeThing();

    protected String getMsg() {
        return this.battercake.getMsg();
    }

    protected int getPrice() {
        return this.battercake.getPrice();
    }
}
