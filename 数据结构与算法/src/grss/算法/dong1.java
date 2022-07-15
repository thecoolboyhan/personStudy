package grss.算法;

import java.util.Scanner;

/**
 * 韩永发
 *
 * @Date 12:42 2022/5/20
 */
public class dong1 {
  public static void main(String[] args){
    Scanner sc= new Scanner(System.in);
    while(sc.hasNext()){
      int n=sc.nextInt();//人数
      int[] person=new int[n];
      for(int i=0;i<n;i++){
        person[i]=sc.nextInt();
      }

      int[] left=new int[n];
      int[] right=new int[n];
      left[0]=1;
      right[n-1]=1;

      for(int i=0;i<n;i++){
        //默认左边只有一个，就是它本身
        left[i]=1;
        for(int j=0;j<i;j++){
          if(person[j]<person[i]){
            //不需要挨个比较，只需要知道如果当前满足小于i的身高
            //就可以用当前的左边符合条件数+1，和上一个满足的i来比较
            left[i]=Math.max(left[j]+1,left[i]);
          }
        }
      }

      //right需要反向遍历
      for(int i=n-1;i>=0;i--){
        right[i]=1;
        for(int j=n-1;j>i;j--){
          if(person[j]>person[i]){
            right[i]=Math.max(right[j]+1,right[i]);
          }
        }
      }

      //记录每个位置的值
      int[] result =new int[n];
      for(int i=0;i<n;i++){
        result[i]=left[i]+right[i]-1;
      }
      //取出最大值
      int max=1;
      for(int i=0;i<n;i++){
        max=Math.max(max,result[i]);
      }
      System.out.print(max);
      int daa=0;
    }
  }
}
