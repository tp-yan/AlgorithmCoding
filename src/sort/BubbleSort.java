package sort;

import dft.MyUtils;

import java.util.Arrays;


public class BubbleSort {
    /**
     * 冒泡排序O(N^2)
     * 关键：只关心相邻元素的大小，若满足条件则交换，否则移到下个元素继续相邻比较
     * 注：冒泡排序就算是前面的数组已经排序，但还是都要遍历一下数组，故冒泡排序没有最好最差情况，或者说它们都一样
     */
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2)
            return;
        for (int i = arr.length - 1; i >= 0; i--) {// i指向要存放最大值得位置
            for (int j = 0; j < i; j++) { // 遍历子数组从0~i-1，找出最大值
                if (arr[j] > arr[j + 1]) {
                    // 每次只需要考虑当前元素与下一个元素是否需要交换，遍历完后一定将最大值换到i所指位置
                    MyUtils.swap_i_j(arr, j, j + 1);
                }
            }
        }
    }

    // ============================== 对数器 =============================

    /**
     * 对数器，保证是正确的功能实现，作为正确答案，即验证标准
     *
     * @param arr
     */
    public static void comparatorMachine(int[] arr) {
        Arrays.sort(arr);
    }

    /**
     * 随机数发生器：生成随机数组用于测试，有正有负
     *
     * @param len 随机的数组长度
     * @param max 元素最大值
     * @return
     */
    public static int[] generateRandomArray(int len, int max) {
        int[] arr = new int[(int) ((len + 1) * Math.random())]; // 长度随机
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (max + 1)) - (int) (max * Math.random());
        }
        return arr;
    }

    /**
     * 验证自己的算法和正确的算法的输出结果是否一致
     *
     * @return 被验证算法的正确性
     */
    public static boolean validateAlgorithm() {
        int testTimes = 50000;  // 测试次数
        int len = 100;  // 数组长度
        int max = 100;  // 数组最大值
        boolean succeed = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = generateRandomArray(len, max);
            int[] arr2 = MyUtils.copyArray(arr1);
            bubbleSort(arr1);
            comparatorMachine(arr2);
            if (!isEqual(arr1, arr2)) {
                succeed = false;
                break;
            }
        }
        return succeed;
    }

    /**
     * 判断2个数组元素是否完全相等
     *
     * @param arr1
     * @param arr2
     * @return
     */
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr2 == null && arr1 != null))
            return false;
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length)
            return false;
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    // ============================== 对数器 =============================

    public static void main(String[] args) {
        System.out.println("对数器：");
        if (validateAlgorithm()) {
            System.out.println("Nice！It works!");
        } else {
            System.out.println("Fucking!");
        }

        int len = 10;
        int max = 20;
        int[] arr = MyUtils.generatePosRandomArray(len, max);

        System.out.println("before sort:");
        MyUtils.printArray(arr);

        bubbleSort(arr);

        System.out.println("after sort:");
        MyUtils.printArray(arr);

    }


}