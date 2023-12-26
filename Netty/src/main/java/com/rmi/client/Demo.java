package com.rmi.client;

import java.util.*;
import java.util.stream.Collectors;

public class Demo {

    public static void main(String[] args) {

    }

    //二分查找（基于有序数组）
    class BinarySearchST<Key extends Comparable<Key>,Value>{
        //基于数组的实现
        private Key[] keys;
        private Value[] vals;
        private int N;
        public BinarySearchST(int capacity){
            keys=(Key[]) new Comparable[capacity];
            vals=(Value[]) new Object[capacity];
        }
        public int size(){
            return N;
        }
        //获取一个元素
        public Value get(Key key){
            //如果堆为空，直接返回null
            if(isEmpty()) return null;
            //找到当前key在数组中的位置
            int i=rank(key);
//            如果找到当前元素，返回val
            if(i<N&&keys[i].compareTo(key)==0) return vals[i];
//            否则返回null
            else return null;
        }
//        放入一对key-val
        public void put(Key key,Value val){
//            找到当前元素所在的位置
            int i=rank(key);
//            如果存在当前元素
            if(i<N&&keys[i].compareTo(key)==0){
//                把val改成新val
                vals[i]=val;
                return;
            }
//            没有找到key，让之之前rank查找到的左边界之后的所有元素全部向后移动一格
            for(int j=N;j>i;j--){
                keys[j]=keys[j-1];
                vals[j]=vals[j-1];
            }
            //找到的i设置一对新key-val
            keys[i]=key;
            vals[i]=val;
//            当前元素数量++
            N++;
        }
        public void delete(Key key){
            put(key,null);
        }
        //找到key在数组中的位置
        private int rank(Key key){
            //lo下边界，hi上边界
            int lo=0,hi=N-1;
            //二分查找模版
            while(lo<=hi){
                //中点
                int mid=lo+(hi-lo)/2;
//                要选择元素和当前中点所指元素来比较
                int cmp=key.compareTo(keys[mid]);
                //小
                if(cmp<0) hi=mid-1;
                //大
                else if(cmp>0) lo=mid+1;
//                bingo
                else return mid;
            }
//            如果没有找到，返回当前左边界下标，此下标指向如果需要插入当前key合适的位置
            return lo;
        }
        public boolean isEmpty(){
            return size()==0;
        }
        public boolean contains(Key key){
            return get(key)!=null;
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