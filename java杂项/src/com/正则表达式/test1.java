package com.正则表达式;

import sun.java2d.pipe.SpanIterator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分析java正则表达式的底层实现
 */
public class test1 {
    public static void main(String[] args) {
        String text="Java是一门面向对象编程语言，不仅吸收了C++语言的各种优点，还摒弃了C++里难以理解的多继承、" +
                "指针等概念，因此Java语言具有功能强大和简单易用两个特征。Java语言作为静态面向对象编程语言的代表，" +
                "极好地实现了面向对象理论，允许程序员以优雅的思维方式进行复杂的编程 [1]  。\n111" +
                "1Java具有简单性、面向对象、3443分布式、健壮性、安全性、平台独立与可移植性、多线程、动态性等特点 [2]  " +
                "。Java可以编写桌面应用程序、Web应用程序、分布式系统和嵌入式系统应用程序等 [3]  。\n";

        //四个数字的字符串
        //\\d表示一个任意的数字
        String regStr="(\\d\\d)(\\d\\d)";
        //创建模式对象
        Pattern pattern = Pattern.compile(regStr);
        //创建匹配器
        //按照正则表达式嗯规则匹配字符串
        Matcher matcher = pattern.matcher(text);

        //开始匹配
        /**
         * matcher.find():完成任务
         *
         */
        while (matcher.find()){
            System.out.println("找到"+matcher.group(1));
        }
        System.out.println("over");
    }
}
