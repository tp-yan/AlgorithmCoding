package sort;

import dft.MyUtils;

public class SelectionSort {
    /**
     * 选择排序：依次从数组中选择最大元素，放在第0，1,2,..位置
     * 关键：找到剩余数组最大元素与当前被占位置元素交换
     * @param arr
     */
    public static void selectionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int len = arr.length;
        for (int i = 0; i < len - 1; i++) { // i:指向当前最小值所放的索引，0~n-1
            int maxIndex = i;   // 当前最小值索引
            for (int j = i + 1; j < len; j++) {   // 找到剩余数组中最小值索引
                if (arr[j] < arr[maxIndex]) {
                    maxIndex = j;
                }
            }
            MyUtils.swap_i_j(arr, i, maxIndex);   // 将当前位置的元素与最小元素交换位置
        }
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generateRandomArray(20,20);
        System.out.println("before sort:");
        MyUtils.printArray(arr);

        selectionSort(arr);

        System.out.println("after sort:");
        MyUtils.printArray(arr);
    }
}
