package grss.算法;

/**
 * 韩永发
 *
 * 递归分治+备忘录
 * @Date 12:01 2022/5/17
 */
public class Fib2 {
  //用于存储每次计算的结果
  static long[] sub=new long[100000000];
  public static long fib(int n){
    //递归结束的情况
    if (n<=1)
      return n;
    //该数字没有被计算
    if (sub[n]==0){
      sub[n]=fib(n-1)+fib(n-2);
    }
    return sub[n];
  }

  public static void main(String[] args) {
    System.out.println(fib(64));
  }
}
