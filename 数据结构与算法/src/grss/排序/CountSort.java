package grss.排序;

import java.util.Arrays;

/**
 * 韩永发
 *
 * @Date 12:10 2022/5/14
 */
public class CountSort {

  public static int[] sort(int[] arr,int offSet){
    //用来排序的数组
    int[] nums=new int[arr.length];
    for (int i = 0; i < arr.length; i++) {
      int n=arr[i]-offSet;
      nums[n]++;
    }
    //用来输出的新数组
    int[] nums2=new int[arr.length];
    //i是计数数组的下标，k是输出的新数组的下标
    for (int i = 0, k=0; i <nums.length ; i++) {
      //如果计数数组的值大于0
      for (int j = 0; j < nums[i]; j++) {
        nums2[k++]=i+offSet;
      }
    }
    return nums2;
  }
  public static void main(String[] args) {
    //这里测试没有加如偏移量
    int[] nums={2,4,5,6,7,8,4,3,2,4,4,7,8,4,6,4};
    int[] sort = sort(nums, 0);
    System.out.println(Arrays.toString(sort));
  }
}
