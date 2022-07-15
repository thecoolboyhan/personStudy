package grss.算法;

import java.util.Scanner;

/**
 * 韩永发
 *
 * 如果要买归类为附件的物品，必须先买该附件所属的主件，且每件物品只能购买一次。
 * 每个主件可以有 0 个、 1 个或 2 个附件。附件不再有从属于自己的附件。
 * 王强查到了每件物品的价格（都是 10 元的整数倍），而他只有 N 元的预算。除此之外，他给每件物品规定了一个重要度，用整数 1 ~ 5 表示。他希望在花费不超过 N 元的前提下，使自己的满意度达到最大。
 * 满意度是指所购买的每件物品的价格与重要度的乘积的总和，假设设第ii件物品的价格为v[i]v[i]，重要度为w[i]w[i]，共选中了kk件物品，编号依次为j_1,j_2,...,j_kj
 * 1
 * ​
 *  ,j
 * 2
 * ​
 *  ,...,j
 * k
 * ​
 *  ，则满意度为：v[j_1]*w[j_1]+v[j_2]*w[j_2]+ … +v[j_k]*w[j_k]v[j
 * 1
 * ​
 *  ]∗w[j
 * 1
 * ​
 *  ]+v[j
 * 2
 * ​
 *  ]∗w[j
 * 2
 * ​
 *  ]+…+v[j
 * k
 * ​
 *  ]∗w[j
 * k
 * ​
 *  ]。（其中 * 为乘号）
 * 请你帮助王强计算可获得的最大的满意度。
 *
 *
 * 输入描述：
 * 输入的第 1 行，为两个正整数N，m，用一个空格隔开：
 *
 * （其中 N （ N<32000 ）表示总钱数， m （m <60 ）为可购买的物品的个数。）
 *
 *
 * 从第 2 行到第 m+1 行，第 j 行给出了编号为 j-1 的物品的基本数据，每行有 3 个非负整数 v p q
 *
 *
 * （其中 v 表示该物品的价格（ v<10000 ）， p 表示该物品的重要度（ 1 ~ 5 ）， q 表示该物品是主件还是附件。如果 q=0 ，表示该物品为主件，如果 q>0 ，表示该物品为附件， q 是所属主件的编号）
 *
 *
 *
 *
 * 输出描述：
 *  输出一个正整数，为张强可以获得的最大的满意度。
 * @Date 10:38 2022/5/20
 */
public class Big1 {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    while (in.hasNextInt()) { // 注意 while 处理多个 case
      int money = in.nextInt() / 10;//钱数是10的倍数，除以10，降低时间和空间复杂度
      int num = in.nextInt();
      //读取输入，区分主件，附件
      int[][] price = new int[num + 1][3];//记录主件和附件的价格
      int[][] pPlusImportant = new int[num + 1][3];//记录主件和附件的价格 * 重要度
      for (int i = 1; i <= num; i++) {
        int v = in.nextInt() / 10;
        int p = in.nextInt();
        int q = in.nextInt();
        int im = v * p;
        if (q == 0) {
          price[i][0] = v;//第一列是主件价格，第二列是附件1的价格，第三列附件2的价格
          pPlusImportant[i][0] = im;
        } else {
          if (price[q][1] == 0) {
            price[q][1] = v;//主件q的附件1的价格
            pPlusImportant[q][1] = im;
          } else {
            price[q][2] = v;//主件q的附件2的价格
            pPlusImportant[q][2] = im;
          }
        }
      }
      int[] dp = new int[money + 1];//背包问题，带附加条件
      for (int i = 1; i <= num; i++) {
        if (price[i][0] == 0) {
          continue;//主件为空，则跳过
        }
        for (int j = money; j >= price[i][0]; j--) {
          int a = price[i][0];//主件
          int a1 = pPlusImportant[i][0];
          int b = price[i][1];//附件1
          int b1 = pPlusImportant[i][1];
          int c = price[i][2];//附件2
          int c1 = pPlusImportant[i][2];
          if (j >= a) {
            //买和不买取最大值
            dp[j] = Math.max(dp[j], dp[j - a] + a1);
          }
          //买附件和主键取最大值
          if (j >= a + b) {
            dp[j] = Math.max(dp[j], dp[j - a - b] + a1 + b1);
          }
          //取出现在买的附件2取最大值
          if (j >= a + c) {
            dp[j] = Math.max(dp[j], dp[j - a - c] + a1 + c1);
          }
          
          if (j >= a + b + c) {
            dp[j] = Math.max(dp[j], dp[j - a - b - c] + a1 + b1 + c1);
          }
        }
      }
      System.out.println(dp[money] * 10);//开始的时候总钱数除10，输出的时候*10
      int aa= 0;
    }
  }
}
