package com.example;

import java.util.*;

public class Constants {

    public  static Map<String,String> hm=new HashMap<>();
    static {
        hm.put("1","张飞");
        hm.put("2","赵云");
        hm.put("3","马超");
        hm.put("4","关羽");
        hm.put("5","黄忠");
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
    }
}



class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}


class Solution {

    private Map<TreeNode,Map<Long,Integer>> map;
    private int res=0;
    private long target;
    public int pathSum(TreeNode root, int targetSum) {
        if(root==null) return 0;
        this.map=new HashMap<>();
        this.target=targetSum;
        dd(root);
        return res;
    }

    private void dd(TreeNode node){
        Map<Long,Integer> map1=new HashMap<>();
        map1.put((long) node.val,1);
        if(node.left!=null){
            dd(node.left);
            var mapl=map.get(node.left);
            change(node.val,map1,mapl);
        }

        if(node.right!=null){
            dd(node.right);
            var mapr=map.get(node.right);
            change(node.val,map1,mapr);
        }
        res+=map1.getOrDefault(target,0);
        map.put(node,map1);
    }

    private void change(int val,Map<Long,Integer> map1,Map<Long,Integer> map2){
        for (Map.Entry<Long, Integer> entry : map2.entrySet()) {
            long t = entry.getKey() + val;
            map1.put(t,map1.getOrDefault(t,0)+entry.getValue());
        }
    }
}