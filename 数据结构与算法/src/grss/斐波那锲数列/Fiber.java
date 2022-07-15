package grss.斐波那锲数列;

/**
 * 韩永发
 *
 * @Date 18:03 2022/5/12
 */
public class Fiber {
  private static int fun(int n){
    if (n<=1) return n;
    return fun(n-1)+fun(n-2);
  }
  public static void main(String[] args) {
    System.out.println(fun(10));
  }
}
