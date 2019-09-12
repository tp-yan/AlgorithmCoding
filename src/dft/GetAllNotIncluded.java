package dft;

import java.util.ArrayList;
import java.util.List;

import static sort.QuickSort.quickSort;

public class GetAllNotIncluded {
    /**
     * 返回B中不在A中的所有元素：因为A是有序的，故将B中每个元素在A中进行二分查找
     *
     * @param A 有序数组
     * @param B 无序数组
     * @return
     */
    public static List<Integer> getAllNotIncluded(int[] A, int[] B) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < B.length; i++) {
            int index = binarySearch(A, B[i]);
            if (index == -1) {
                res.add(B[i]);
            }
        }
        return res;
    }

    /**
     * 二分查找，返回目标所在索引或者-1：表示不存在
     *
     * @param arr    有序数组
     * @param target 查找目标
     * @return 目标所在索引或者-1
     */
    public static int binarySearch(int[] arr, int target) {
        int l = 0;
        int r = arr.length - 1;
        int mid = l + ((r - l) >> 1);   // 数组中间元素
        while (l <= r) {
            if (arr[mid] == target) {
                return mid;
            }
            if (arr[mid] < target) {// target在数组右边
                l = mid + 1;
            } else {// target在数组左边
                r = mid - 1;
            }
            mid = l + ((r - l) >> 1);
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] A = MyUtils.generateRandomArray(20, 40);
        quickSort(A);
        System.out.println("A:");
        MyUtils.printArray(A);
        int[] B = MyUtils.generateRandomArray(10, 40);
        System.out.println("B:");
        MyUtils.printArray(B);
        System.out.println("getAllNotIncluded:");
        System.out.println(getAllNotIncluded(A, B));
    }
}
