package dft;

/**
 * 问题：求一个数组的最大子段和
 * 解法：如下三步算法解释（非动态规划视角），假设答案法：先假设得到了一个解，然后去分析这个解满足的一些性质，再反过来根据这些性质
 * 来抓出该解。
 * 假设我们的解：1)和最大 2)该解是所有解中长度最长的。因为可能存在多个解，且解的长度相同或者不同
 * 设该解为第i个元素开头至第j个元素结束，由于该解有如上两条性质，所以能推导出如下规则：
 * (1) 设k为i~j中任何一个元素，那么从i~k的子段和一定不会 ＜ 0，即 ≥ 0
 * (2) 以i前面的元素i-1为后缀的任何一个子段和都 ＜ 0
 * 由(2)可知，当cur遍历到i-1时，cur一定 < 0，遍历到 i 时，cur重置为0。由(2)知，遍历i~j，cur始终 ≥ 0，所以cur在i~j上的累加值，
 * 一定能被max捕获到，该值也就是最大子段和
 */
public class MaxSubArraySum {

    public static int maxSum(int[] arr) {
        int len = arr.length;

        int max = Integer.MIN_VALUE;  // 保存最大子段和，初始最小整数值
        int cur = 0;    // 当前子段累计值

        // 从左往右遍历数组元素
        for (int i = 0; i < len; i++) {
            // 第一步： cur += arr[i]
            cur += arr[i];
            // 第二步：比较 cur 与 max 的大小
            max = Math.max(max, cur);
            // 第三步：是否重置 cur
            cur = cur < 0 ? 0 : cur;
        }
        return max;
    }

    public static void main(String[] args) {
        int n = 10;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * 20 - 10);
        }
        MyUtils.printArray(arr);
        System.out.println("Max sum:");
        System.out.println(maxSum(arr));
    }
}
