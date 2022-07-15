package grss.排序;

import sun.invoke.util.BytecodeName;

import java.util.HashMap;

/**
 * 韩永发
 *
 * @Date 18:36 2022/5/13
 */
public class OneQuick {
  volatile int a=0;
  public static void quickSort(int[] nums, int start, int end) {
    if (start >= end) return;
    //得到基准
    int pivot = partition(nums, start, end);
    quickSort(nums, start, pivot - 1);
    quickSort(nums, pivot + 1, end);

  }

  private static int partition(int[] nums, int start, int end) {
    int pivot = nums[start];
    int mark = start;
    for (int i = start + 1; i <= end; i++) {
      if (nums[i] < pivot) {
        mark++;
        int a = nums[mark];
        nums[mark] = nums[i];
        nums[i] = a;
      }
    }
    nums[start] = nums[mark];
    nums[mark] = pivot;
    return mark;
  }

  public static void main(String[] args) {
    int[] nums = {26, 5, 433, 2, 23, 12, 53};
    /**         26,5
     *          26,5,2,433,
     *          26,5,2,23,433,
     *          26,5,2,23,12,433,52
     *          12,5,2,23,26,433,52
     *
     */

    aa(nums, 0, nums.length - 1);
//    quickSort(nums,0,nums.length-1);
    for (int num : nums) {
      System.out.print(num + ",");
    }
  }

  private static void aa(int[] nums,int start,int end){
    if (start>=end) return;;
    //确认中点
    int pivot =bb(nums,start,end);
    aa(nums,start,pivot-1);
    aa(nums,pivot+1,end);
  }
  private static int bb(int[] nums,int start,int end){
    int temp=nums[start];
    int index=start;
    for (int i =start+1; i <=end; i++) {
      if(nums[i]<temp) {
        index++;
        int aa = nums[i];
        nums[i] = nums[index];
        nums[index] = aa;
      }
    }
    nums[start]=nums[index];
    nums[index]=temp;
    return index;
  }

}
