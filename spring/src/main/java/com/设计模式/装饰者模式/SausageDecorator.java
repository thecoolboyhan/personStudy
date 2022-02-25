package com.设计模式.装饰者模式;

public class SausageDecorator extends BattercakeDecotator{

    public SausageDecorator(Battercake battercake) {
        super(battercake);
    }

    @Override
    protected void doSomeThing() {

    }

    @Override
    protected String getMsg() {
        return super.getMsg()+"加一根香肠";
    }

    @Override
    protected int getPrice() {
        return super.getPrice()+2;
    }

    public static void main(String[] args) {
        Battercake battercake;
        battercake = new BaseBattercake();
        battercake = new EggDecorator(battercake);
        battercake = new EggDecorator(battercake);
        battercake = new SausageDecorator(battercake);
        System.out.println(battercake.getMsg()+",总价:"+battercake.getPrice());
    }
}
