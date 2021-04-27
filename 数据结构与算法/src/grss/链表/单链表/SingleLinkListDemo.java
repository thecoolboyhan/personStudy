package grss.链表.单链表;

import java.util.Stack;

/**
 * 韩永发
 *
 * @author hp
 * @Date 15:51 2021/3/23
 */
public class SingleLinkListDemo {
  public static void main(String[] args) {
    HeroNode heroNode = new HeroNode(1, "宋江", "及时雨");
    HeroNode heroNode1 = new HeroNode(2, "宋江1", "及时雨1");
    HeroNode heroNode2 = new HeroNode(3, "宋江2", "及时雨2");
    HeroNode heroNode3 = new HeroNode(4, "宋江3", "及时雨3");
    HeroNode heroNode4 = new HeroNode(5, "宋江4", "及时雨4");
    sigleLinkedList sigleLinkedList = new sigleLinkedList();
//    sigleLinkedList.add(heroNode);
//    sigleLinkedList.add(heroNode2);
//    sigleLinkedList.add(heroNode3);
//    sigleLinkedList.add(heroNode1);
//    sigleLinkedList.add(heroNode4);

    //按no来插
    sigleLinkedList.addByNo(heroNode);
    sigleLinkedList.addByNo(heroNode2);
    sigleLinkedList.addByNo(heroNode4);
    sigleLinkedList.addByNo(heroNode1);
    sigleLinkedList.change(heroNode3);
    sigleLinkedList.show();
  }
}

//单链表
class sigleLinkedList {

  //先定义头节点，头节点内容为空
  private HeroNode head = new HeroNode(0, "", "");

  //尾插
  public void add(HeroNode node) {
    //头节点不动,所以搞一个指针
    HeroNode temp = head;

    //尾插，所以要遍历
    while (true) {
      //先判断有没有下一个节点
      if (temp.next == null) {
        break;
      }
//      System.out.println(temp);
      temp = temp.next;
    }
    //上面的遍历是找到最后的节点，然后把数据放到最后
    temp.next = node;
  }

  //遍历
  public void show() {
    if (head.next == null) {
      System.out.println("链表为空");
      return;
    }
    HeroNode temp = head.next;
    while (temp != null) {
      System.out.println(temp);
      temp = temp.next;
    }
  }

  //按no添加
  public void addByNo(HeroNode node) {
    //从头开始遍历
    HeroNode temp = head;
    boolean flag = false;//用flag来记录是否已经存在相同no的节点
    while (true) {
      if (temp.next == null) {//已经是最后一个节点了
        break;
      }
      if (temp.next.no > node.no) {//找到了要插入的位置
        break;
      } else if (temp.next.no == node.no) {
        flag = true;
        break;
      }
      temp = temp.next;
    }
    if (flag) {
      System.out.println("插入值的位置已经被其他节点占用了。");
      return;
    }
    node.next = temp.next;
    temp.next = node;
  }

  //修改
  public void change(HeroNode newHeroNode) {
    if (head.next == null) {
      System.out.println("链表是空的");
      return;
    }
    //判断有没有这个节点
    boolean flag = false;
    HeroNode temp = head.next;
    while (true) {
      if (temp == null) {//现在到了链表的最后
        break;
      }
      if (temp.no == newHeroNode.no) {
        flag = true;
        break;
      }
      temp = temp.next;
    }
    if (flag) {
      temp.name = newHeroNode.name;
      temp.nickName = newHeroNode.nickName;
      return;
    }
    //没有找到这个节点
    System.out.printf("没有找到编号为%d的节点，修改失败!\n", newHeroNode.no);
  }

  //删除功能
  public void delByNo(int no) {
    boolean flag = false;//用来标记是否找到要被删除的节点
    //判断链表是否为空
    if (head.next == null) {
      System.out.println("链表为空！不能删除。");
      return;
    }
    HeroNode temp = head;
    while (true) {
      if (temp.next == null) {//已经遍历到最后了
        break;
      }
      if (temp.next.no == no) {
        flag = true;
        break;
      }
      temp = temp.next;
    }
    if (flag) {
      //指向他的下两个节点就算删除了。
      temp.next = temp.next.next;
      return;
    }
    System.out.println("没有找到要被删除的节点！");
  }

  //反转链表
  public static void revereSetList(HeroNode theHead) {
    //判断当前链表如果只有一个或者没有节点，就直接返回
    if (theHead.next == null || theHead.next.next == null) {
      System.out.println("当前链表不满足反转条件");
      return;
    }

    //定义一个指针
    HeroNode temp = theHead.next;
    HeroNode next = null;
    HeroNode newHead = new HeroNode(0, null, null);

    while (temp != null) {
      next = temp.next;//把当前节点的下一个节点先保存下来
      temp.next = newHead.next;//让当前指针的下一个节点指向新头的第一个节点
      newHead.next = temp;//新头的第一个节点变成当前的指针
      temp = next;//当前指针在原来基础上后移
    }
    theHead.next = newHead.next;
  }

  //逆序打印一个单链表且不改变原来的顺序
  public static void reversePrintList(HeroNode theHead) {
    //判断传入的链表是否为空
    if (theHead.next == null) {
      return;
    }
    //创建一个栈
    Stack<HeroNode> stack = new Stack<HeroNode>();
    //定义一个指针
    HeroNode cear = theHead.next;
    //压入栈，两个方法
    while (cear != null) {
//		  stack.add(cear);
      stack.push(cear);
      cear = cear.next;
    }
    //出栈输出
    while (cear != null) {
      HeroNode pop = stack.pop();
      System.out.println(pop);
    }
  }

}

//英雄节点
class HeroNode {
  public int no;//编号
  public String name;//名字
  public String nickName;//花名
  public HeroNode next;//下一个节点

  public HeroNode(int no, String name, String nickName) {
    this.name = name;
    this.no = no;
    this.nickName = nickName;
  }

  @Override
  public String toString() {
    return "HeroNode{" +
            "no=" + no +
            ", name='" + name + '\'' +
            ", nickName='" + nickName + '\'' +
//            ", next=" + next +
            '}';
  }
}