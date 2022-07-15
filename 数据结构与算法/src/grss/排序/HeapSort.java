package grss.排序;

import java.util.Arrays;

/**
 * 韩永发
 *
 * 堆排序
 * @Date 11:21 2022/5/14
 */
public class HeapSort {
  public  static void sort(int[] arr){
    //把无序数组构建成大顶堆
    //让指针指向当前堆最左非叶子节点，依次调整，使它为大顶堆
    for (int i=arr.length/2-1;i>=0;i--){
      adjustHeap(arr,i,arr.length);
    }
    //调整后嗯值保证了
    System.out.println("调整成大顶堆的值"+Arrays.toString(arr));
    //调整堆结构，末尾元素和堆顶交换，构建成新堆，再调整
    //从最后一个元素交换然后开始调整
    for (int i=arr.length-1;i>0;i--){
      int tmp=arr[0];
      arr[0]=arr[i];
      arr[i]=tmp;
      //继续调整之间的值，是最大的到上面，最小的在下面
      adjustHeap(arr,0,i);
    }
  }

  public static void adjustHeap(int[] arr,int parent,int len){
    //临时变量保存父节点
    int tmp=arr[parent];
    //左孩子节点
    int child=2*parent+1;
    while (child<len){
      //存在右孩子,            并且右大于左
      if (child+1<len&&arr[child+1]>arr[child])
        //让指针指向大的节点
        child++;
      //如果现在的父节点大于最大的孩子节点，则不用交换
      if (arr[parent]>=arr[child]) break;
      //父节点改成大孩子的值
      arr[parent]=arr[child];
      //把当前父节点位置指向大孩子的位置
      parent=child;
      //以此法看是否还存在大节点下还有孩子节点
      child=child*2+1;
    }
    //当前parent指向最原始parent的较大孩子，将原始parent值复制给大孩子
    arr[parent]=tmp;
  }

  public static void main(String[] args) {
    int[] nums={26,5,433,2,23,12,53};
    System.out.println(Arrays.toString(nums));
    sort(nums);
    System.out.println(Arrays.toString(nums));
  }
}
