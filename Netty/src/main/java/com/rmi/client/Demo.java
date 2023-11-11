package com.rmi.client;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo {

    public static void main(String[] args) throws InterruptedException {
        Solution solution = new Solution();
        System.out.println(solution.minOperations("1100011000", "0101001010", 2));

    }


    private  class Example{

        public void merge(Comparable[] a,int lo,int mid, int hi){
            //放最后排序的结果
            Comparable[] res=new Comparable[a.length];
            //将数组的lo到mid，和mid+1到hi部分归并
            int i=lo,j=mid+1;
            for(int k=lo;k<=hi;k++)
                res[k]=a[k];

            for(int k=lo;k<=hi;k++)
                //如果左边界大于中点，表示左边元素已经被取完，从右边取
                if (i>mid)
                    a[k]=res[j++];
                    //如果右边大于边界，右边取完，取左边
                else if (j>hi)
                    a[k]=res[i++];
                    //如果右边小于左边，取右边
                else if(less(res[j],res[i]))
                    a[k]=res[j++];
                else
                    //否则取左边
                    a[k]=res[i++];
        }
        //辅助数组
        private Comparable[] aux;
        public void sort(Comparable[] a){
            int n = a.length;
            aux=new Comparable[n];
            //每次对sz个元素来排序
            for(int sz=1;sz<n;sz+=sz)
                //左边界从0开始，每段左边界都是上一次+sz
                for(int lo=0;lo<n-sz;lo+=sz+sz)
                    //对于每次需要归并的两个数组，左边边界已经确认，中点为小数组+sz-1，右边边界为中点+sz和数组长度-1取较小值
                    merge(a,lo,lo+sz-1,Math.min(lo+sz+sz+-1,n-1));
        }


        //判断a是否小于b
        private   boolean less(Comparable a,Comparable b){
            return a.compareTo(b)<0;
        }

        //交换集合中i，j两个下标的元素
        private  void  exch(Comparable[] a,int i,int j){
            Comparable t = a[i];
            a[i]=a[j];
            a[j]=t;
        }

        //打印集合
        private   void show(Comparable[] a){
            for (int i = 0; i < a.length; i++) {
                System.out.print(a[i]+" ");
                System.out.println();
            }
        }

        public  boolean isSorted(Comparable[] a){
            for (int i = 1; i < a.length; i++) {
                if(less(a[i],a[i-1]))
                    return false;
            }
            return true;
        }
    }
}

class Solution {
    public int minOperations(String s1, String s2, int x) {
        if(s1.equals(s2))
            return 0;
        char[] chars1=s1.toCharArray();
        char[] chars2=s2.toCharArray();
        int res=0;
        int[] nums=new int[chars1.length];
        int t=0;
        for(int i=0;i<nums.length;i++){
            if(chars1[i]!=chars2[i]){
                nums[i]=1;
                t++;
            }
        }
        if((t&1)!=0)
            return -1;
        boolean f=false;
        for(int i=0;i<nums.length;i++){
            if(i<nums.length-1&&nums[i]==1){
                res++;
                nums[i]=0;
                nums[i+1]=0;
                i++;
                t-=2;
            }
        }
        return res+(t/2)*x;
    }
}


