package com.myspring.demo;

import com.myspring.mvcframework.annotation.MYService;

/**
 * 核心业务代码
 */
@MYService
public class DemoService implements IDemoService{
    @Override
    public String get(String name){
        return "My name is "+ name;
    }
}
