package grss.链表.环形单链表;

import java.security.PublicKey;

/**
 * 韩永发
 *
 * @Date 10:28 2021/3/29
 */
public class Josephu {
  public static void main(String[] args) {
    CircleSingleLinkedList circleSingleLinkedList = new CircleSingleLinkedList();
    circleSingleLinkedList.addBoy(10);
    circleSingleLinkedList.viewBoy();
    circleSingleLinkedList.countBoy(2, 3, 10);
  }
}

//单向环形链表
class CircleSingleLinkedList{
  //创建一个头
  private Boy first=null;

  //添加
  public void addBoy(int nums){
    //判断输入的数据是否合法
    if (nums<1){
      System.out.println("输入的数据有误");
      return;
    }
    //搞一个指针
    Boy curBoy=null;
    for (int i = 1; i <= nums; i++) {
      Boy boy = new Boy(i);
      if (i==1){
        first=boy;
        first.setNext(first);
        curBoy=first;
      }else {
        curBoy.setNext(boy);//让指针下一个指向新建的
        boy.setNext(first);
        curBoy=boy;
      }
    }
  }


  //遍历
  public void viewBoy(){
    if (first==null){
      System.out.println("当前链表为空");
      return;
    }
    Boy curBoy=first;
    do {
      System.out.printf("当前小孩的编号为:%d\n",curBoy.getNo());
      curBoy=curBoy.getNext();
    }while (curBoy.getNext()!=first.getNext());//不满足条件才循环
  }

  /**
   * 出圈
   * @param startNum 从第几个开始
   * @param countNum 数几下
   * @param nums 最初有多少个
   */
  public void countBoy(int startNum,int countNum,int nums){
	  //判断各参数是否合法
	  if(startNum<1||countNum<startNum||first==null) {
		  System.out.println("输入的参数不合法");
		  return;
	  }
	  //创建辅助指针
	  Boy helper =first;
	  //把辅助指针放到链表尾
	  while(true) {
		  if(helper.getNext()==first) {
			  break;
		  }
		  helper=helper.getNext();
	  }
	  //把指针移动到从第几个孩子开始喊的地方
	  for (int i = 0; i < startNum-1; i++) {
		first=first.getNext();
		helper=helper.getNext();
	  }
	  //出圈
	  while(true) {
		  //如果圈中只剩一人让她出圈
		  if(helper==first) {
			  break;
		  }
		  //正常的出圈方法，先让helper和first同时移动countNum-1
		  for (int i = 0; i < countNum-1; i++) {
			first=first.getNext();
			helper=helper.getNext();
		  }
		  //小孩要出圈了
		  System.out.printf("第%d个小孩出圈了\n",first.getNo());
		  first=first.getNext();
		  helper.setNext(first);
	  }
	  System.out.printf("最后出圈的小孩%d\n",first.getNo());
  }

}

class Boy{
  private int no;//编号
  private Boy next;//下一个
  public Boy(int no){
    this.no=no;
  }

  public int getNo() {
    return no;
  }

  public void setNo(int no) {
    this.no = no;
  }

  public Boy getNext() {
    return next;
  }

  public void setNext(Boy next) {
    this.next = next;
  }
}
