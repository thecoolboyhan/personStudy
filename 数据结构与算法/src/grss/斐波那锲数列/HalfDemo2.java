package grss.斐波那锲数列;

/**
 * 韩永发
 *
 * @Date 18:34 2022/5/12
 */
public class HalfDemo2 {
  private static int halfSearch(int[] nums, int low,int high,int value){
    if (low>high)return -1;
    int mid=(low+high)/2;
    int val=nums[mid];
    if (val==value)
      return val;
    if (val<value)
      return halfSearch(nums,mid+1,high,value);
    if (val>value)
      return halfSearch(nums,low,mid-1,value);
    return -1;
  }

  public static void main(String[] args) {
    int[] nums={1,2,3,4,5,6,7,8,9,11};
    System.out.println(halfSearch(nums, 0, 9, 2));
  }
}
