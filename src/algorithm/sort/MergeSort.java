package algorithm.sort;

import algorithm.MyUtils;

import java.util.Arrays;

public class MergeSort {

    /**
     * 归并排序：基于递归。将一个数组从中间划分为2个子数组，然后将2个子数组分别排序后，再合并这2个子数组使其整体有序，
     * 而子数组的处理过程是一样的。包括2种操作：①划分 ②合并。
     * 时间O(N*logN),空间O(N)
     * 时间复杂度计算： T(N) = 2T(N/2) + O(N)，合并在O(N)的时间内完成。
     * 估计递归函数时间复杂度：T(N) = aT(N/b) + O(N^d) 类型的时间复杂度使用 master公式计算，根据a,b,d的值分为3种情况。
     *
     * @param arr
     */
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2)
            return;
        sortProcess(arr, 0, arr.length - 1);
    }

    /**
     * 这里才是真正的归并排序过程，只排序arr的 l~r 这个范围，不影响数组的其他部分
     *
     * @param arr 要排序的数组
     * @param l   排序范围的左指针
     * @param r   排序范围的右指针
     */
    public static void sortProcess(int[] arr, int l, int r) {
        if (l == r) {   // 递归结束条件，只有一个元素时不再排序
            return;
        }
        // 划分
        int mid = l + ((r - l) >> 1);  // 不使用 (l+r)/2是防止求和溢出，同时 位操作比 四则运算都要快。 >>:带符号右移；>>>:补0的右移
        sortProcess(arr, l, mid);
        sortProcess(arr, mid + 1, r);
        // 合并
        merge(arr, l, mid, r);
    }

    /**
     * 只合并指定范围内的子数组，不影响数组的其他部分
     *
     * @param arr l~mid,mid+1~r已是有序数组，但l~r整体不是有序的
     * @param l   合并范围的左指针
     * @param mid 分割位置
     * @param r   合并范围的右指针
     */
    public static void merge(int[] arr, int l, int mid, int r) {
        int[] help = new int[r - l + 1];    // 辅助数组
        int i = 0;
        int leftPointer = l;    // 左子数组第一个元素
        int rightPointer = mid + 1; // 右子数组第一个元素

        while (leftPointer <= mid && rightPointer <= r) {
            // 指向较小元素的指针移动
            help[i++] = arr[leftPointer] < arr[rightPointer] ? arr[leftPointer++] : arr[rightPointer++];
        }

        // 下面2个只有一个成立
        while (leftPointer <= mid) {
            help[i++] = arr[leftPointer++];
        }
        while (rightPointer <= r) {
            help[i++] = arr[rightPointer++];
        }
        // 赋值使原数组有序
        for (int j = 0; j < help.length; j++) {
            arr[l + j] = help[j];
        }
    }

    // 4.1 使用循环实现非递归版的归并排序
    public static void mergeUseCycle(int[] arr) {
        if (arr == null || arr.length < 2) return;
        int k = 2; // 每相邻k个元素外排即合并
        while (true) {
            for (int left = 0; left < arr.length; ) {
                if (arr.length - left <= k / 2) // 剩下的数组只有一个子数组，无需合并，如 k = 2 剩余：[1]。k=4，剩余：[2,3]等
                    break;
                int right = left + k - 1;
                int mid = left + (right - left) / 2;
                if (right >= arr.length) {// 说明此段元素不够k个， k=4,剩余：[1,2,-1]，两个有序子数组:[1,2]，[-1]
                    right = arr.length - 1;
                    mid = left + k / 2 - 1;
                }
                merge(arr, left, mid, right);
                left += k;
            }
            if (k > arr.length)
                break;
            k *= 2;
        }
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generateRandomArray(100, 20);
//        int[] arr = {2, -10, -5, 9, 2, 4, 8, 1, 2, 2, -3};
        int[] arrCopy = arr.clone();

        System.out.println("before algorithm.sort:");
        System.out.println(Arrays.toString(arr));
        mergeSort(arr);
        System.out.println("after algorithm.sort:");
        System.out.println(Arrays.toString(arr));

        System.out.println("before arrCopy:" + Arrays.toString(arrCopy));
        mergeUseCycle(arrCopy);
        System.out.println("after arrCopy:" + Arrays.toString(arrCopy));

        System.out.println(Arrays.equals(arr, arrCopy));
    }
}
