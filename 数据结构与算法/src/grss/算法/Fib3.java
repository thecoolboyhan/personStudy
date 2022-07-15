package grss.算法;

import javax.lang.model.element.VariableElement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 韩永发
 * <p>
 * 利用dp方程求解
 *
 * @Date 12:19 2022/5/17
 */
public class Fib3 {
  private static long fib(int n) {
    long[] a = new long[n + 1];
    a[0] = 0;
    a[1] = 1;
    int i;
    for (i = 2; i <= n; i++) {
      //a[n]=a[n-1]+a[n-2]
      a[i] = a[i - 1] + a[i - 2];
    }
    //上面循环最后i+1，现在减1
    return a[i - 1];
  }

//  public static void main(String[] args) {
//    System.out.println(fib(64));
//  }


}
