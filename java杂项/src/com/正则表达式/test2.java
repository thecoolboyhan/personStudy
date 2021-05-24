package com.正则表达式;

import javax.print.attribute.standard.Media;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test2 {
    public static void main(String[] args) {
        String abc="aBc$(abc(123(";
        String reg="[abc]";
//        String reg="(?i)abc";//匹配abc字符串不区分大小写
        Pattern compile = Pattern.compile(reg);
        Matcher matcher = compile.matcher(abc);
        while (matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }
}
