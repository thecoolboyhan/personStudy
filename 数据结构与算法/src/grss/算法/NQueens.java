package grss.算法;

import java.time.temporal.ValueRange;

/**
 * 韩永发
 *
 * @Date 11:00 2022/5/17
 */
public class NQueens {
  private int count;
  static final int QUEENS = 8;
  //用来存放放好的皇后，数组下标为行，值为列
  private int[] result = new int[QUEENS];

  //在指定的行放皇后
  public void setQueens(int row) {
    if (row == QUEENS) {
      print();
      System.out.println(++count);
      return;
    }
    //在每一列尝试放入皇后
    for (int i = 0; i < QUEENS; i++) {
      if (isOK(row, i)) {
        //放入皇后
        result[row] = i;
        //继续下一行
        setQueens(row + 1);
      }
    }

  }

  //是否可以放置
  private boolean isOK(int row, int col) {
    int leftUp = col - 1;//左对角线
    int rightUp = col + 1;//右对角线所在列
    for (int i = row - 1; i >= 0; i--) {
      //等于列，原列有皇后
      if (result[i] == col) return false;
      //左对角线
      if (leftUp >= 0) {
        if (result[i] == leftUp) {
          return false;
        }
      }
      //右对角线
      if (rightUp < QUEENS) {
        if (result[i] == rightUp) {
          return false;
        }
      }
      leftUp--;
      rightUp++;
    }
    return true;

  }

  //打印结果
  private void print() {
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result.length; j++) {
        //j代表此行的哪一列
        if (result[i] == j) {
          System.out.print(" Q|");
        } else {
          System.out.print(" *|");
        }
      }
      System.out.println();
    }
    System.out.println("--------------------");
  }

  public static void main(String[] args) {
    NQueens nQueens = new NQueens();
    nQueens.setQueens(0);
  }

}
