package dft;

import algorithm.MyUtils;

import java.util.Arrays;

/**
 * 求小和：将数组中的每个元素左边比他小的元素求和再一起累加
 * 思想：从另一视角考虑，不考虑每个元素左边比它小的元素和，而是考虑每个元素右边比它大的元素个数（即该元素会被累加的次数），
 * 个数乘以它本身，再累加，会得到一样的值。
 * 解法：利用上述思想，再结合归并排序，就可求解：在每次合并的过程中去计算每个元素会被累加的次数，具体地，合并2个有序数组时，
 * 对左数组的每个元素，判断右数组中有多少个元素比它大，则得到该元素在本次合并时被累加的次数。直到合并为最终的数组。
 * 注意每次合并时，左边的元素只会考虑右数组中比它大的元素，合并后，这些元素归为一组，后面再合并时同组元素是不会被计数的，
 * 故保证了不会漏记和多记。
 * 实质上就是在归并排序的过程中，在合并时计算小和，并累加
 */
public class SmallSum {
    public static int smallSum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        return mergeSort(arr, 0, arr.length - 1);
    }

    // 还是归并排序过程
    public static int mergeSort(int[] arr, int l, int r) {
        // 递归终止条件
        if (l == r) {
            return 0;
        }
        int mid = l + ((r - l) >> 1);

        return mergeSort(arr, l, mid) +   // 左数组求小和
                mergeSort(arr, mid + 1, r) + // 右数组求小和
                merge(arr, l, mid, r); // 本次合并时求小和
    }

    public static int merge(int[] arr, int l, int mid, int r) {
        int[] help = new int[r - l + 1];
        int i = 0;
        int leftP = l;
        int rightP = mid + 1;

        int res = 0;    // 小和
        while (leftP <= mid && rightP <= r) {
            // 并合并过程中，若左边的元素比右边小，则求小和，否则小和为0
            res += arr[leftP] < arr[rightP] ? (r - rightP + 1) * arr[leftP] : 0;
            help[i++] = arr[leftP] < arr[rightP] ? arr[leftP++] : arr[rightP++];
        }
        while (leftP <= mid) {
            help[i++] = arr[leftP++];
        }
        while (rightP <= r) {
            help[i++] = arr[rightP++];
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generateRandomArray(5, 20);
        System.out.println(Arrays.toString(arr));

        int sum = smallSum(arr);

        System.out.println("small sum:" + sum);
    }

}
