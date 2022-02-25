package com.设计模式.观察者模式;

import java.util.Observable;
import java.util.Observer;

//观察者
public class Teacher implements Observer {
    private String name;
    public Teacher(String name){
        this.name=name;
    }

    @Override
    public void update(Observable o, Object arg) {
        GPer gper = (GPer) o;
        Question question = (Question) arg;
        System.out.println("=======================");
        System.out.println(name+"老师，你好！\n"+"您收到了一个来自"+gper.getName()+"的提问，希望您解答，具体内容如下:\n"+question.getContent()+"\n"+"提问者:"+ question.getUserName());
    }

    public static void main(String[] args) {
        //要在哪个圈子发布订阅
        GPer gper = GPer.getInstance();
        Teacher tom = new Teacher("Tom");
        Teacher mic = new Teacher("Mic");
        //添加两个订阅者（观察者）
        gper.addObserver(tom);
        gper.addObserver(mic);
        //业务逻辑代码
        Question question = new Question();
        question.setUserName("小明");
        question.setContent("观察者设计模式适用于哪些场景？");
        //调用发布接口把内容发布到圈子里
        gper.publishQuestion(question);
    }
}
