package algorithm.sort;

import algorithm.MyUtils;

import java.util.Arrays;

public class HeapSort {
    /**
     * 堆结构：完全二叉树（概念上的）。堆实现的真实结构是一个数组。
     * 大顶堆：堆顶元素最大，左右孩子都小于等于其父节点，同时每颗子树的根节点也是该子树上的最大值，子树也是堆结构。
     * 而堆排序是基于堆结构的，有2个操作：
     * 1. 建大顶堆，即 heapInsert 过程（从下往上的调整过程）
     * 2. 通过不断换出堆顶元素到数组末尾，再调整大顶堆使其对应的数组有序，即 heapify 过程（从上往下的调整过程）
     * @param arr
     */
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 1. 建立大顶堆 heapInsert
        for (int i = 0; i < arr.length; i++) {
            heapInsert(arr, i); // 将数组元素依次插入堆，构建大堆
        }
        // 2. 数组排序：堆调整，heapify
        int heapSize = arr.length;  // 堆大小
        MyUtils.swapIJ(arr, 0, --heapSize); // 将堆顶元素放到数组最后，并且堆大小-1
        while (heapSize > 0) {
            heapify(arr, 0, heapSize);
            MyUtils.swapIJ(arr, 0, --heapSize);
        }
    }

    /**
     * 建立大顶堆过程，就是逐步向堆中插入一个元素(前index-1个元素已是大顶堆)，然后从下往上调整，保证大顶堆结构
     *
     * @param arr   堆结构的落地实现-数组
     * @param index 要插入堆的元素的索引
     */
    public static void heapInsert(int[] arr, int index) {
        while (arr[index] > arr[(index - 1) / 2]) {// (index - 1) / 2：是当前节点index的父节点
            MyUtils.swapIJ(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;    // 换到父节点后，更新当前索引，并继续向上一层比较，直到满足大堆结构
        }
    }

    /**
     * 将当前堆结构调整成大堆，从 index 所指元素开始调整，从上往下
     *
     * @param arr
     * @param index    指向当前不满足大堆结构的元素
     * @param heapSize 堆大小
     */
    public static void heapify(int[] arr, int index, int heapSize) {
        int left = 2 * index + 1; // 左孩子索引（若存在）
        while (left < heapSize) {
            // 若还存在右孩子，则返回左右孩子中值最大的元素的索引
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            largest = arr[largest] > arr[index] ? largest : index;   // 孩子节点是否比父节点大
            if (largest == index)   // 此时满足大堆结构，则结束本次调整
                break;
            // 否则将父节点往下调，直到满足大堆结构
            MyUtils.swapIJ(arr, index, largest);
            index = largest;
            left = index * 2 + 1;
        }
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generateRandomArray(20, 20);
        System.out.println("before algorithm.sort:");
        System.out.println(Arrays.toString(arr));

        heapSort(arr);

        System.out.println("after algorithm.sort:");
        System.out.println(Arrays.toString(arr));
    }
}
