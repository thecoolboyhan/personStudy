package com.代理.CGLIB;

import org.springframework.cglib.core.DebuggingClassWriter;

public class CGCustomer {
    public void findLove(){
        System.out.println("肤白貌美大长腿");
    }

    public static void main(String[] args) {
        try {
            //利用CGlib的代理类可以将内存中的.class文件写入硬盘
            System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"/home/rose/");
            CGCustomer instance = (CGCustomer) new CGlibMeipo().getInstance(CGCustomer.class);
            instance.findLove();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
