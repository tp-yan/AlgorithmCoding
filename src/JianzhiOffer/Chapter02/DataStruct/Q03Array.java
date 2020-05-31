package JianzhiOffer.Chapter02.DataStruct;

import JianzhiOffer.Utilities.ArrayUtils;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 面试题3：
 */
public class Q03Array {
    /*
    面试题3（一）：找出数组中重复的数字
    题目：在一个长度为n的数组里的所有数字都在0到n-1的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，
    也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。例如，如果输入长度为7的数组{2, 3, 1, 0, 2, 5, 3}，
    那么对应的输出是重复的数字2或者3。
     */

    /* 方法一：使用数组的快速索引特性
        时间复杂度O(n),空间复杂度O(1)
     */
    public static boolean duplicate1(int[] arr) {
        if (arr == null || arr.length < 2) return false;
        for (int i = 0; i < arr.length; i++) {
            // 处理错误输入
            if (arr[i] < 0 || arr[i] >= (arr.length)) return false;
        }
        for (int index = 0; index < arr.length; ) {
            if (arr[index] == index) {// 当前元素值与坐标索引一致，那么指针后移
                index++;
            } else {
                if (arr[index] == arr[arr[index]]) { // 如果该值所在索引已有相等元素，则说明重复
                    System.out.println("duplicate element:" + arr[index]);
                    return true;
                } else { // 否则交换2个元素位置，将值为i的元素放在第个i位置。然后继续判断当前位置
                    ArrayUtils.swap(arr, index, arr[index]);
                }
            }
        }
        return false;
    }

    /* 方法二：使用HashMap
    时间复杂度O(n),空间复杂度O(n)
     */
    public static boolean duplicate2(int[] arr) {
        if (arr == null || arr.length < 2) return false;
        for (int i = 0; i < arr.length; i++) {
            // 处理错误输入
            if (arr[i] < 0 || arr[i] >= (arr.length)) return false;
        }
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int v : arr) {
            if (!hashMap.containsKey(v)) {
                hashMap.put(v, v);
            } else {
                System.out.println("duplicate element:" + v);
                return true;
            }
        }
        return false;
    }

    /* 方法三：由于数组本身就是散列表的底层实现基础，可以把数组当散列表来使用
         时间复杂度O(n),空间复杂度O(n)
     */
    public static boolean duplicate3(int[] arr) {
        if (arr == null || arr.length < 2) return false;
        for (int i = 0; i < arr.length; i++) {
            // 处理错误输入
            if (arr[i] < 0 || arr[i] >= (arr.length)) return false;
        }
        int[] help = new int[arr.length];
        Arrays.fill(help, -1);

        for (int a : arr) {
            if (help[a] == a) {
                System.out.println("duplicate element:" + a);
                return true;
            } else {
                help[a] = a;
            }
        }
        return false;
    }

    /*
     面试题3（二）：不修改数组找出重复的数字
     题目：在一个长度为n+1的数组里的所有数字都在1到n的范围内，所以数组中至
     少有一个数字是重复的。请找出数组中任意一个重复的数字，但不能修改输入的
     数组。例如，如果输入长度为8的数组{2, 3, 5, 4, 3, 2, 6, 7}，那么对应的
     输出是重复的数字2或者3。

     方法一：使用辅助数组遍历，时间复杂度O(n),空间复杂度O(n)
     */

    /* 方法二：要求空间复杂度小于O(n)，即无法使用辅助数组
       使用逻辑区域，将1~n，按中间值m划分为2段：1~m,m+1~n。然后统计数组中有多少个元素在1~m内，若count > m说明，在1~m中的某些数肯定
       出现了不止一次，否 count <= m，则说明重复的数应该属于 m+1~n。每次这样二分，直到区域缩小到1时，统计单个元素出现次数时得到重复元素。
       注：此方法不能保证找到所有重复元素
     */
    public static int duplicateNoModify(int[] arr) {
        if (arr == null || arr.length < 2) return -1;
        // 1~n
        int left = 1;
        int right = arr.length - 1;
        while (right >= left) {
            int mid = left + ((right - left) >> 1);
            int count = countRangeNumber(arr, left, mid);
            if (left == right) {// 说明统一到具体的某个元素出现的次数
                if (count > 1) return left;
                else break;
            }
            if (count > (mid - left) + 1) // 说明重复的数属于 left~mid
                right = mid;
            else left = mid + 1; // 说明重复的数属于 mid+1~right
        }
        return -1;
    }

    // 统计数组中有多少个元素在left~right范围内
    public static int countRangeNumber(int[] arr, int left, int right) {
        int count = 0;
        for (int v : arr) {
            if (v >= left && v <= right) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        int[] arr = ArrayUtils.genPosRandArr(10, 9);
        System.out.println(Arrays.toString(arr));
        System.out.println(duplicate1(arr));
        System.out.println(duplicate2(arr));
        System.out.println(duplicate3(arr));
        int[] arr2 = {2, 3, 4, 5, 3, 2, 6, 7};
        int[] arr3 = {2, 3, 4, 5, 1, 0, 6, 7};
        System.out.println("重复元素：" + duplicateNoModify(arr2));
        System.out.println("重复元素：" + duplicateNoModify(arr3));
    }
}
