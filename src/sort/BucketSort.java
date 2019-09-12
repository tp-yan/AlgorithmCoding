package sort;

import dft.MyUtils;

public class BucketSort {
    /**
     * 桶排序适用于数组元素有一定范围，而且元素个数很多，比如0~200范围内，有几亿个元素.
     * 这里实现 计数法 实现桶排序
     * @param arr
     */
    public static void bucketSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 只能处理 元素 >= 0 且 max不能太大的数组，若要处理带负数，或者 max很大的数组，需要进行偏移处理
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max,arr[i]);
        }
        int range = max +1;   // 数组的取值范围，即桶的个数，每个取值对应一个桶
        int[] bucket = new int[range];
        for (int i = 0; i < arr.length; i++) {
            bucket[arr[i]]++;   // 统计每个取值的个数
        }
        int i = 0;
        for (int j = 0; j < range; j++) {// 将每个桶中的元素依次倒出排成一个数组
            while (bucket[j]-- > 0) {   // 一个桶里的所有元素倒出，0个就跳过
                arr[i++] = j;   // 直接覆盖原数组
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generatePosRandomArray(10, 20);
        System.out.println("before sort:");
        MyUtils.printArray(arr);

        bucketSort(arr);

        System.out.println("after sort:");
        MyUtils.printArray(arr);
    }
}
