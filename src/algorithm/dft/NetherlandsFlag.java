package algorithm.dft;

import algorithm.MyUtils;

import java.util.Arrays;

public class NetherlandsFlag {
    /**
     * 划分：用指定值split作为划分元素，而非数组中的元素，使得 [less < split] [== split] [ > split]
     *
     * @param arr
     * @param l     划分范围左边界
     * @param r     划分范围右边界
     * @param split 划分元素
     * @return 相等区间左右界
     */
    public static int[] partition(int[] arr, int l, int r, int split) {
        int less = l - 1; // 左区间边界
        int more = r + 1; // 右区间界限
        int index = l;  // 遍历指针

        while (index < more) {  // 当遍历指针碰到右区间边界时停止
            if (arr[index] < split) { // 小于元素放于左区间
                MyUtils.swapIJ(arr, ++less, index++);   // 左区间向右扩大，交换元素，遍历指针右移
            } else if (arr[index] > split) {
                MyUtils.swapIJ(arr, --more, index); // 右区间向左扩大，交换元素，遍历指针不动
            } else {
                index++; // 相等时，只移动遍历指针
            }
        }
        return new int[]{less + 1, more - 1};   // 中间为相等区间，若 less+1 > more-1，则没有相等区间
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generatePosRandomArray(10, 20);
        System.out.println(Arrays.toString(arr));
        int[] border = partition(arr, 0, arr.length - 1, 10);
        if (border[0] > border[1]) {
            System.out.println("没有相等区间！");
        } else
            System.out.println("border: [" + border[0] + "," + border[1] + "]");
        System.out.println(Arrays.toString(arr));
    }
}
