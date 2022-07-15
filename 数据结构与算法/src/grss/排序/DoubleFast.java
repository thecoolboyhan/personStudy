package grss.排序;

/**
 * 韩永发
 *
 * 快速排序双边循环法
 * @author hp
 * @Date 12:23 2022/5/13
 */
public class DoubleFast {
  private static void quickSort(int[] nums,int startIndex,int endIndex){
    //如果两端指针碰撞，本次循环结束
    if (startIndex>=endIndex)return;
    
    int pivotIndex=partition(nums,startIndex,endIndex);
    //左右分开再进行一次
    quickSort(nums,startIndex,pivotIndex-1);
    quickSort(nums,pivotIndex+1,endIndex);
  }

  private static int partition(int[] nums,int startIndex,int endIndex) {
    //取第一个位置作为基准元素
    int pivot=nums[startIndex];
    int left=startIndex;
    int right=endIndex;
    while (left!=right){
      //控制right指针比较并左移
      while (left<right&&nums[right]>pivot)
        right--;
      while (left<right&&nums[left]<=pivot)
        left++;
      //交换两个元素
      if (left<right){
        int a= nums[left];
        nums[left]=nums[right];
        nums[right]=a;
      }
    }
    //pivot和两指针重和点交换
    nums[startIndex]=nums[left];
    nums[left]=pivot;
    return left;
  }

  public static void main(String[] args) {
    int[] nums={26,53,433,2,23,12,5};
    quickSort(nums,0,nums.length-1);
    for (int num : nums) {
      System.out.print("num = " + num);
    }
  }
}
