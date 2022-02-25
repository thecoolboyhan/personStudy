package com.设计模式.装饰者模式;

public class EggDecorator extends BattercakeDecotator{

    public EggDecorator(Battercake battercake) {
        super(battercake);
    }

    @Override
    protected void doSomeThing() {

    }

    @Override
    protected int getPrice() {
        return super.getPrice()+1;
    }

    @Override
    protected String getMsg() {
        return super.getMsg()+"加一个鸡蛋";
    }
}
