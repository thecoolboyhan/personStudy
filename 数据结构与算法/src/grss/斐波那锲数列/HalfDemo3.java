package grss.斐波那锲数列;

import javax.lang.model.element.VariableElement;

/**
 * 韩永发
 *
 * @Date 18:54 2022/5/12
 */
public class HalfDemo3 {
  /**
   * 阿里面试题：
   * 一个有序数组有一个数出现1次，其他数出现两次，找出出现1次的数
   *
   * 比如：1 1 2 2 3 3 4 4 5 5 6 7 7	6出现1次；
   *
   * 思路：
   *
   * 偶数位索引跟后面的比相同，奇数位索引跟前面的比相同，则说明前面的都对
   *
   * 偶数为所有跟前面的比相同，奇数位索引跟后面的比相同，则说明后面的都对
   */
  private static int fun(int[] nums,int low,int high){
    if (low==high){
      return nums[high];
    }
    int mid= (low+high)/2;
    //奇数
    if (mid%2==1){
      if (nums[mid]==nums[mid-1])
        return fun(nums,mid+1,high);
      else if (nums[mid]==nums[mid+1])
        return fun(nums,low,mid-1);
      else {
        return nums[mid];
      }
    }
    if (nums[mid]==nums[mid-1])
      return fun(nums,low,mid-1);
    else if (nums[mid]==nums[mid+1])
     return fun(nums,mid+1,high);
    else {
      return nums[mid];
    }
  }
  public static void main(String[] args) {
      int[] nums={1,1,2,2,3,3,4,4,5,5,6,6,7,7,8};
    System.out.println(fun(nums, 0, nums.length - 1));
  }
}
