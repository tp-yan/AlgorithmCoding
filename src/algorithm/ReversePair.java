package algorithm;


import java.util.Arrays;

/* 求逆序对个数：与“最小和”同理。相当于在合并时，求右边数组小于左边数组某个元素的个数再累加。实际上就是求每个元素之前比它大的个数之和
比如序列 4 3 1 0 5 中的逆序对有：[4,3],[4,1],[4,0] [3,1] [3,0] [1,0] */
public class ReversePair {
    public static int reversePairCount(int[] arr) {
        if (arr == null || arr.length < 2) return 0;
        return reverseSortProcess(arr, 0, arr.length - 1);
    }

    public static int reverseSortProcess(int[] arr, int l, int r) {
        if (l == r) return 0;
        int mid = l + (r - l) / 2;
        return reverseSortProcess(arr, l, mid)
                + reverseSortProcess(arr, mid + 1, r)
                + reverseMerge(arr, l, mid, r);
    }

    public static int reverseMerge(int[] arr, int l, int mid, int r) {
        int[] tmp = new int[r - l + 1];
        int index = 0;
        int pL = l;
        int pR = mid + 1;
        int count = 0;

        while (pL <= mid && pR <= r) {
            // 记住：一定要比较小值，因为遇到小值就被插入到辅助数组了！
            // 一旦右边的某个数A小于左边数B，则左数组中A之后的元素都比B大
            count += arr[pR] < arr[pL] ? (mid - pL + 1) : 0; // 根据索引快速计算个数
            tmp[index++] = arr[pL] < arr[pR] ? arr[pL++] : arr[pR++];
        }
        while (pL <= mid) {
            tmp[index++] = arr[pL++];
        }
        while (pR <= r) {
            tmp[index++] = arr[pR++];
        }
        for (int i = 0; i < tmp.length; i++) {
            arr[l + i] = tmp[i];
        }
        return count;
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generateRandomArray(5, 20);
//        int[] arr = {4, 11, -5};
        System.out.println(Arrays.toString(arr));
        int count = reversePairCount(arr);
        System.out.println("reverse Pair Count:" + count);
    }
}
