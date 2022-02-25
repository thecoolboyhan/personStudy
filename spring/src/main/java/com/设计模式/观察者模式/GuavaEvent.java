package com.设计模式.观察者模式;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class GuavaEvent {

    @Subscribe
    public void subscribe(String str){
        //业务逻辑代码
        System.out.println("执行了subscribe方法，传入的参数是"+str);
    }

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        GuavaEvent guavaEvent = new GuavaEvent();
        eventBus.register(guavaEvent);
        eventBus.post("Tom");
    }
}
