package com.rmi.client;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Demo {

    public static void main(String[] args) throws InterruptedException {
        Solution solution = new Solution();


    }


    private  class Example{


        public void sort(Comparable[] a){
           sort(a,0,a.length-1);
        }
        private void sort(Comparable[] a, int lo, int hi) {
            //跳出条件
           if (hi<=lo)  return;
           //lt:要排序的数字，i起始位置，gt右边界
           int lt=lo,i=lo+1,gt=hi;
            Comparable v = a[lo];
            //从第一个元素一直判断到最后一个元素
            while(i<=gt){
                //当前元素和v比较
                int cmp = a[i].compareTo(v);
                //如果当前元素比要比较的元素小，交换当前元素和目标元素，目标指针后移，保证目标指针左侧的元素都
                if(cmp<0) exch(a,lt++,i++);
                else if(cmp>0) exch(a,i,gt--);
                else i++;
            }
            sort(a,lo,lt-1);
            sort(a,gt+1,hi);
        }

        //把目标数字放到合适的位置
        private int partition(Comparable[] a, int lo, int hi) {
            //i表示当前遍历到第几个数，j表示上线边界
            int i=lo,j=hi+1;
            //选第一个数出来放到合适的位置，用第一个数来切割
            Comparable v=a[lo];
            while(true){
                //找到从左到右第一个大于目标数组的数
                while(less(a[++i],v))   if(i==hi)   break;
                //从右到左，第一个小于目标数字的数
                while(less(v,a[--j]))   if(j==lo)   break;
                //跳出条件，当i==j时，表示i左边的数字都小于目标数，右边的数字都大于目标数
                if(i>=j)    break;
                //把小于目标的数字放到左边，大于目标的数字放到右边
                exch(a,i,j);
            }
            //由于左边界为选出的数字，上方循环到跳出条件时，j最后一个指向小于目前的数字，交换这两个数字
            exch(a,lo,j);
            return j;
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
    public List<String> findHighAccessEmployees(List<List<String>> access_times) {
        List<List<String>> collect = access_times.stream().sorted((o1, o2) -> {
            Integer a = Integer.valueOf(o1.get(1));
            Integer b = Integer.valueOf(o2.get(1));
            return a - b;
        }).collect(Collectors.toList());
        Map<String,Deque<Integer>> map=new HashMap<>();
        Set<String> res=new HashSet<>();
        for (List<String> list : collect) {
            String name=list.get(0);
            int time= Integer.parseInt(list.get(1));
            Deque<Integer> queue = map.getOrDefault(name, new LinkedList<>());
            while (!queue.isEmpty()&&time-queue.peek()>=100)  queue.pop();
            queue.offer(time);
            if(queue.size()>=3) res.add(name);
            map.put(name,queue);
        }
        return new ArrayList<>(res);
    }
}