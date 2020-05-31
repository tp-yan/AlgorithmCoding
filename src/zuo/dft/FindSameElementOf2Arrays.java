package zuo.dft;

import zuo.MyUtils;

import java.util.Arrays;

import static zuo.sort.SelectionSort.selectionSort;

public class FindSameElementOf2Arrays {
    /**
     * 找到2个有序数组的公有元素
     * 解法：i，j指针分别指向arrA，arrB的第一个元素，谁小移动谁，每移一次比较i,j元素大小，同理谁小移谁，若元素相等，则一起移
     * 直到某个指针越界
     *
     * @param arrA
     * @param arrB
     */
    public static void findSameElementOf2Arrays(int[] arrA, int[] arrB) {
        int i = 0;
        int j = 0;
        while (i < arrA.length && j < arrB.length) {
            if (arrA[i] == arrB[j]) {
                System.out.print(arrA[i] + "\t");
                i += 1;
                j += 1;
            } else if (arrA[i] < arrB[j])
                i += 1;
            else j += 1;
        }
    }

    public static void main(String[] args) {
        int[] arrA = MyUtils.generateRandomArray(20, 20);
        int[] arrB = MyUtils.generateRandomArray(20, 20);

        selectionSort(arrA);
        selectionSort(arrB);

        System.out.println(Arrays.toString(arrA));
        System.out.println(Arrays.toString(arrB));

        findSameElementOf2Arrays(arrA, arrB);
    }
}
