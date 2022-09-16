package com.代理.dongDJ;


public class Customer implements Person{

    public void findLove() {
        System.out.println("高富帅");
        System.out.println("身高180");
        System.out.println("有六块腹肌");
    }

    //系统自带的JDK动态代理测试
//    public static void main(String[] args) {
//        Person instance = (Person) new JDKMeipo().getInstance(new Customer());
//        instance.findLove();
//        byte[] bytes = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{Person.class});
//        try {
//            FileOutputStream os = new FileOutputStream("/home/rose/$Proxy0.class");
//            os.write(bytes);
//            os.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
