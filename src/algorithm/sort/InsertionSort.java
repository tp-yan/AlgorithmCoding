package algorithm.sort;

import algorithm.MyUtils;

import java.util.Arrays;

public class InsertionSort {
    /**
     * 插入排序：假设前面数组已排序，将后面数组的元素逐个插入到前面数组中
     * 提前停止：当找到插入位置后就结束，不一定要遍历完前面整个数组，故此方法比冒泡和选择排序稍微高效
     * 关键：查找插入位置的过程中同时移动元素，从当前要插入元素的前一个元素往后看，是否需要交换位置
     * @param arr
     */
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {  // i前面的元素已有序，第i个元素即是要插入额元素
            // j：移动指针，初始指向i的前一个元素。比较j及其后一个元素是否需要交换，即从前一个位置往后看
            for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) { // 若 j 比 j+1大，则将它们位置互换
                MyUtils.swapIJ(arr, j, j + 1);
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generateRandomArray(20, 20);
        System.out.println("before algorithm.sort:");
        System.out.println(Arrays.toString(arr));

        insertionSort(arr);

        System.out.println("after algorithm.sort:");
        System.out.println(Arrays.toString(arr));
    }
}
