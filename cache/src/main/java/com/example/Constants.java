package com.example;

import ch.qos.logback.core.model.INamedModel;

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
        solution.buildTree(new int[]{3,9,20,15,7},new int[]{9,3,15,20,7});
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
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        var stack=new LinkedList<TreeNode>();
        TreeNode root=new TreeNode(preorder[0]);
        stack.push(root);
        var map=new HashMap<Integer,Integer>();
        for(int i=0;i<inorder.length;i++){
            map.put(inorder[i],i);
        }
        TreeNode pn=root;
        A:
        for(int i=1;i<preorder.length;i++){
            int t=preorder[i];
            int tl=map.get(preorder[i]);
            while(!stack.isEmpty()){
                int p=map.get(stack.peek().val);
                if(p>tl){
                    TreeNode node =new TreeNode(t);
                    if(stack.peek().left!=null)stack.peek().left.right=node;
                    else stack.peek().left=node;
                    stack.push(node);
                    pn=node;
                    continue A;
                }
                pn=stack.pop();
            }
            TreeNode node =new TreeNode(t);
            pn.right=node;
            stack.push(node);
            pn=node;
        }
        System.out.println((root.left==null?"null":root.left.val)+","+(root.right==null?"null":root.right.val));
        return root;
    }
}