package sort;

import dft.MyUtils;

/**
 * 冒泡排序O(N^2)
 */
public class BubbleSort {
    public static void bubbleSort(int[] arr) {
        if(arr == null || arr.length < 2)
            return;
        for(int i=arr.length-1; i >=0; i--){// i指向要存放最大值得位置
            for(int j = 0; j < i; j++){ // 遍历子数组从0~i-1，找出最大值
                if(arr[j] > arr[j+1]){
                    // 每次只需要考虑当前元素与下一个元素是否需要交换，遍历完后一定将最大值换到i所指位置
                    MyUtils.swap(arr,j,j+1);
                }
            }
        }
    }

    public static void main(String[] args) {
        int len = 10;
        int max = 20;
        int[] arr = MyUtils.generatePosRandomArray(len,max);

        System.out.println("before sort:");
        MyUtils.printArray(arr);

        bubbleSort(arr);

        System.out.println("after sort:");
        MyUtils.printArray(arr);

    }
}