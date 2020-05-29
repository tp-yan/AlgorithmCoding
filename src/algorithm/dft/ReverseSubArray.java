package algorithm.dft;

import algorithm.MyUtils;

import java.util.Arrays;

/**
 * 将一个数组的2左右两段子数组顺序互换，并维持子数组中元素的顺序，如 123 | 45 --> 45 | 123。
 * 要求：最多使用一个额外空间！
 * 解法：先分别对左右2个子数组进行逆序，然后再将整个数组进行一次逆序就得到答案
 */
public class ReverseSubArray {
    /**
     * split:右数组第一个元素下标
     */
    public static void reverseSubArray(int[] arr, int split) {
        int len = arr.length;
        int lenLeft = split;
        int lenRight = len - split;

        // 左数组逆序
        for (int i = 0; i < lenLeft / 2; i++) {
            MyUtils.swapIJ(arr, i, lenLeft - i - 1);  // 交换对称元素。全部是在原数组上操作，只使用了一个额外变量
        }
        // 右数组逆序
        for (int i = split; i < split + lenRight / 2; i++) {
            MyUtils.swapIJ(arr, i, split + len - i - 1);
        }
        MyUtils.reverseArray(arr);    // 整个数组再逆序
    }

    public static void main(String[] args) {
        int len = 6;
        int max = 100;
        int split = 3;
        int[] arr = MyUtils.generatePosRandomArray(len, max);

        System.out.println("split element:" + arr[split]);
        System.out.println("before reverse:");
        System.out.println(Arrays.toString(arr));

        reverseSubArray(arr, split);

        System.out.println("after reverse:");
        System.out.println(Arrays.toString(arr));
    }
}