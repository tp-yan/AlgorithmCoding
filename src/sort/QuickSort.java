package sort;

import dft.MyUtils;

public class QuickSort {

    /**
     * 随机快排使得期望时间复杂度为 O(N*logN)，因为每次选择的划分元素是任意的，所以选中最差情况是一种概率事件
     * 空间复杂度：因为每次划分后需要记录划分位置，最好就是 logN 次划分，即二分，故为O(logN)
     * @param arr
     */
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        quickSort(arr, 0, arr.length - 1);
    }

    public static void quickSort(int[] arr, int l, int r) {
        if (l < r) {// 递归 终止条件
            // 第一步：为了实现 随机快速排序，先随机选一个数组元素与最后一个交换。
            // 经典快速排序就是每次都选最后一个作为划分元素，存在最坏情况 O(N^2)
            MyUtils.swap_i_j(arr, l + (int) ((r - l + 1) * Math.random()), r);
            // 第二步：划分，找到分割2个子数组的边界
            int[] split = partition(arr, l, r); // 将 arr 完成划分后，返回中间与划分元素相等的子数组左右界
            // 第三步：子数组继续快排
            quickSort(arr, l, split[0] - 1);    // 左边小于数组
            quickSort(arr, split[1] + 1, r);      // 右边大于数组
        }
    }

    /**
     * 经典快速排序的划分过程：将数组 arr 最右端的元素按作为划分元素 s，进行划分，使其左边元素 <= 它，右边元素 >= 它
     * 解法：假设有2个子区间，左区间存放小于 s 的元素，初始位置在l的前面；右区间存放大于 s 的元素，初始包含数组最右端元素，
     * 即s，初始位置指向 s 的位置。
     * 1.若当前指针(l)所指元素小于s，则将其放到左边的区间：先将左区间指针右移一位，再将该元素与左区间最后一个元素交换位置，
     * 则该元素被纳入左区间，最后当前遍历指针右移一位
     * 2.若当前指针(l)所指元素大于s，则将其放到由边的区间：先将右区间指针左移一位，再将该元素与右区间第一个元素交换位置，
     * 则该元素被纳入右区间，但当前遍历指针不同
     * 一次划分后：[less <s ][s== equal][ >s more]
     * @param arr
     * @param l
     * @param r
     * @return 返回中间相等部分的左右界(包含)
     */
    public static int[] partition(int[] arr, int l, int r) {
        int less = l - 1; // 左区间指针  less) [l,..., (r/more]，左括号代表左区间，右括号代表右区间
        int more = r;   // 右区间指针
        while (l < more) {  // l：遍历指针
            if (arr[l] < arr[r]) {// 1.若当前指针(l)所指元素小于s，则将其放到左边的区间：先将左区间指针右移一位，再将该元素
                // 与左区间最后一个元素交换位置，则该元素被纳入左区间，最后当前遍历指针右移一位
                MyUtils.swap_i_j(arr, ++less, l++);
            } else if (arr[l] > arr[r]) {// 2.若当前指针(l)所指元素大于s，则将其放到由边的区间：先将右区间指针左移一位，
                // 再将该元素与右区间第一个元素交换位置，则该元素被纳入右区间，但当前遍历指针不同
                MyUtils.swap_i_j(arr, --more, l);
            } else {
                l++;    // 相等只需移动当前指针
            }
        }
        // 当排序完后，需要将 划分元素 s == arr[r] 与 右区间第一个元素交换，才算结束
        MyUtils.swap_i_j(arr, r, more);

        return new int[]{less + 1, more};  // more指向划分元素
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generateRandomArray(20, 20);
        System.out.println("before sort:");
        MyUtils.printArray(arr);

        quickSort(arr);

        System.out.println("after sort:");
        MyUtils.printArray(arr);
    }
}
