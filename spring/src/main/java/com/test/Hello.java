package com.test;

import com.test.bean.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Hello {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        Person person = ctx.getBean("person", Person.class);
//        person.setAge("11");
//        person.setName("11");
        System.out.println(person);
    }
}
