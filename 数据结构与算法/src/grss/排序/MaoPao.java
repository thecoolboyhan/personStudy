package grss.排序;

/**
 * 韩永发
 *
 * @Date 11:52 2022/5/13
 */
public class MaoPao {
  public static void main(String[] args) {
    int[] nums={26,53,433,2,23,12,5};
    for (int i = 0; i < nums.length-1; i++) {
      boolean sort=true;
      for (int j = 0; j < nums.length - 1; j++) {
        if (nums[j]>nums[j+1]){
          sort=false;
          //不用临时变量的两树交换
          nums[j]=nums[j]+nums[j+1];
          nums[j+1]=nums[j]-nums[j+1];
          nums[j]=nums[j]-nums[j+1];
        }
        //已经排好了，不需要循环
        if (sort)break;
      }
    }
    for (int num : nums) {
      System.out.print(num+",");
    }
  }
}
