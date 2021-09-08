package leftgod.arrtest;

/**
 * 求一个数组的最大值
 * 利用递归的形式
 */
public class GetMax {

    public static void main(String[] args) {

    }

    public static int getMax(int[] arr){
        return process(arr,0,arr.length-1);
    }

    /**
     * 利用递归来求出数组上一段距离的最大值。
     * 二分查找
     * @param arr
     * @param L
     * @param R
     * @return
     */
    public static int process(int[] arr,int L,int R){
        if (L==R){
            return arr[L];
        }
        //求中点算法
        int mid=L+((R-L)>>1);
        int leftMax=process(arr,L,mid);
        int rightMax=process(arr,mid+1,R);
        return Math.max(leftMax,rightMax);
    }
}
