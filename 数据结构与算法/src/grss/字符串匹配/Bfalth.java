package grss.字符串匹配;

import java.util.HashMap;
import java.util.Locale;

/**
 * 韩永发
 *
 * @Date 17:33 2022/5/14
 */
public class Bfalth {
  public static boolean isMath(String main, String sub){
    boolean b=false;
    for (int i = 0; i <=main.length()-sub.length(); i++) {
      if(main.substring(i,i+sub.length()).equals(sub)) b=true;
    }
    return b;
  }
  public static void main(String[] args) {
    String a="abbaaaa";
    String b="aba".toLowerCase();
    System.out.println(isMath(a, b));
  }

  /**
   * Hello, Sir,
   *     先生您好
   * It is an honor to participate in today's interview.
   * 很荣幸参加今天的采访
   * I hope I can have a good performance today.
   *    我希望我今天能有好的表现
   * I would like to give you a brief self-introduction.
   *  我想给你做一个简短的自我介绍
   * My name is Hanyongfa,
   * 我叫韩永发
   * I graduated from Chifeng college majoring in information and Computing Science
   *我毕业与赤峰学院信息与计算科学专业
   * After graduation, I have been working for i2finance until now.
   * 毕业后一直在i2finance工作至今。
   * During this period, I have mainly participated in the two projects of puzzle system and Mobile bank of SRCB;
   * 
   * In terms of technology stack, I think the basics are very important, so I mainly learn JVM, Java concurrency
   * 技术栈方面，我觉得基础很重要，所以我主要学习JVM、Java并发基础、MySQL,Redis，Spring源码、Netty等
   * foundation, MySQL, Redis,  Data structure and algorithm、Netty and so on.
   *
   * I consider myself an excellent team player and a very honest person.
   *     我认为自己是一个优秀的团队合作者  ,也是一个非常诚实的人
   * Also, I am able to work under pressure.
   * 此外,      我能够在压力下工作.
   * I am very confident that I am qualified for the position of the company's engineer.
   *    我非常有信心胜任公司工程师的职位
   * I appreciate the opportunity you gave me.
   *    我很感激你给我的机会
   * thank you for your time....
   *     感谢您的时间....
   *
   */
}
