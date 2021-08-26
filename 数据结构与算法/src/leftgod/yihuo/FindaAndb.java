package leftgod.yihuo;

/**
 * 1.有一个数组，只有一种数出现了奇数次，其他数都出现了偶数次
 * 2.有两种数出现了奇数次，其他的都是偶数次。
 * 找出这几个数
 */
public class FindaAndb {

    public void findOne(int[] arr) {
        int eor = 0;
        for (int i : arr) {
            eor ^= i;
        }
        System.out.println(eor);
    }

    public void findTwo(int[] arr) {
        int eor = 0;
        //先找出两个奇数次异或的结果
        for (int i : arr) {
            eor ^= i;
        }
        int right = 0;
        //由于两数不同其中必有一位不同
        right = eor & (~eor + 1);//找出这两个数最右边不同的一位
        int one = 0;
        for (int i : arr) {
            if ((right & i) == 1) {
                one ^= i;//找出其中的一个奇数次数
            }
        }
        eor ^= one;
        System.out.println(eor + "  " + one);
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 1, 2, 2, 3, 4, 4};
        int[] arr2 = {1, 1, 2, 2, 3, 4, 4, 5};

        FindaAndb findaAndb = new FindaAndb();
        findaAndb.findOne(arr1);
        findaAndb.findTwo(arr2);
    }
}
