package JianzhiOffer.Chapter02.Algorithm;

/*
// 面试题11：旋转数组的最小数字
// 题目：把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。
// 输入一个递增排序的数组的一个旋转，输出旋转数组的最小元素。例如数组
// {3, 4, 5, 1, 2}为{1, 2, 3, 4, 5}的一个旋转，该数组的最小值为1。

注：若数组有序，或者部分有序一定要先考虑使用二分查找！
 */
public class Q11MinNumberInRotatedArray {
    public static int min(int[] arr) {
        if (arr == null || arr.length < 1) throw new RuntimeException("invalid input.");
        if (arr.length == 1) return arr[0];
        int index1 = 0;
        int index2 = arr.length - 1;
        int indexMid = index1; // indexMid指向最小的元素。若旋转后数组还是有序的(0个元素移到数组末尾)，那么最小元素就是第一个元素，无法进入下面的循环。
        while (arr[index1] >= arr[index2]) {
            if (index2 - index1 == 1) {// 相邻时，index2所指元素即为最小元素
                indexMid = index2;
                break;
            }
            indexMid = index1 + ((index2 - index1) >> 1);
            // 当3个指针所指元素相等时，无法确定indexMid是在前面数组还是后面数组，此时只能在index1~index2范围上顺序最小值
            if (arr[index1] == arr[index2] && arr[index1] == arr[indexMid]) {
                return minOrder(arr, index1, index2);
            } else if (arr[indexMid] >= arr[index1]) {// indexMid位于前数组
                index1 = indexMid;
            } else if (arr[indexMid] <= arr[index2]) {// indexMid位于后数组
                index2 = indexMid;
            }
        }
        return arr[indexMid];
    }

    public static int minOrder(int[] arr, int index1, int index2) {
        int res = arr[index1];
        for (int i = index1 + 1; i <= index2; i++) {
            res = Math.min(res, arr[i]);
        }
        return res;
    }

    public static void main(String[] args) {
        // 典型输入，单调升序的数组的一个旋转
        int array1[] = {3, 4, 5, 1, 2};
        Test(array1, 0,1);

        // 有重复数字，并且重复的数字刚好的最小的数字
        int array2[] = {3, 4, 5, 1, 1, 2};
        Test(array2, 0,1);

        // 有重复数字，但重复的数字不是第一个数字和最后一个数字
        int array3[] = {3, 4, 5, 1, 2, 2};
        Test(array3, 0,1);

        // 有重复的数字，并且重复的数字刚好是第一个数字和最后一个数字
        int array4[] = {1, 0, 1, 1, 1};
        Test(array4, 0,0);

        // 单调升序数组，旋转0个元素，也就是单调升序数组本身
        int array5[] = {1, 2, 3, 4, 5};
        Test(array5, 0,1);

        // 数组中只有一个数字
        int array6[] = {2};
        Test(array6, 0,2);

        // 输入null
        Test(null, 0, 0);
    }

    // ====================测试代码====================
    static void Test(int[] numbers, int length, int expected) {
        int result = 0;
        try {
            result = min(numbers);

            for (int i = 0; i < length; ++i)
                System.out.printf("%d ", numbers[i]);

            if (result == expected)
                System.out.printf("\tpassed\n");
            else
                System.out.printf("\tfailed\n");
        } catch (Exception e) {
            if (numbers == null)
                System.out.printf("Test passed.\n");
            else
                System.out.printf("Test failed.\n");
        }
    }

}
