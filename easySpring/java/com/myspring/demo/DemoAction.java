package com.myspring.demo;

import com.myspring.mvcframework.annotation.MYAutowired;
import com.myspring.mvcframework.annotation.MYController;
import com.myspring.mvcframework.annotation.MYRequestMapping;
import com.myspring.mvcframework.annotation.MYRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MYController
@MYRequestMapping("/demo")
public class DemoAction {
    @MYAutowired
    private IDemoService iDemoService;

    @MYRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @MYRequestParam("name") String name){
        String result = iDemoService.get(name);
        try {
            response.getWriter().write(result);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MYRequestMapping("/add")
    public void add(HttpServletRequest request,HttpServletResponse response,@MYRequestParam("a") Integer a,@MYRequestParam("b") Integer b){
        try {
            response.getWriter().write(a+"+"+b+"="+(a+b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MYRequestMapping("/remove")
    public void remove(HttpServletRequest request,HttpServletResponse response,@MYRequestParam("id") Integer ids){

    }
}
