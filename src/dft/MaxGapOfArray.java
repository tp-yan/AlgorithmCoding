package dft;

import algorithm.MyUtils;

import java.util.Arrays;

import static algorithm.sort.QuickSort.quickSort;

public class MaxGapOfArray {
    /**
     * 求一个无序数组，其排序后相邻元素之间的最大差值。
     * 解：利用桶的概念求解。设数组有n个元素，其元素值范围：min~max。则准备n+1个桶，将min~max这段范围均分，即第一个桶表示范围：
     * min~min+d，第二个桶：min+d+1~min+2d+1,...,最后一个桶 max-d~max,d:每个桶的宽度，也就是均分到的区间宽度。接着，将元素根据其
     * 值划分到对应桶中，因为有n个元素，n+1个桶，且 min、max分别在第一个桶和最后一个桶，那么肯定中间某个桶O是没有元素的。
     * 现在先明确一个问题：设桶O前面的第一个非空桶设为A，O后面第一个非空桶设为B，那么A的最大元素Amax和B中最小元素Bmin之间肯定是
     * 相邻的(排序后)，因为AB之间的桶是空桶，所以Amax、Bmin之间没有元素。而 Bmin-Amax > d，则说明 Bmin与Amax的距离肯定是大于桶
     * 的宽度的，所以我们要求的最大差值，肯定不是一个桶内元素的之间的差值，而应该是2个桶中元素的差值。
     * 另外，有一点需要注意：这个最大差值并不一定是空桶相邻的2个桶中元素差值，比如A的范围：0~9，其最大值Amax=9，B：20~29，Bmin=20
     * 那么 Bmin-Amax = 11。另一种情况，AB相邻，A:0~9,B:10~19，其中Amax=0,Bmin=19, Bmin-Amax = 19 > 11，则说明最大差值也可能
     * 存在2个相邻桶，而其中间并非一定要有空桶。
     * 故有了上述分析，则求解：首先标记一个桶是否为空桶，然后记录其max，min。我们需要记录的是每2个相邻桶元素之间的最大差值：即Bmin-Amax，
     * 桶之间的空桶可以忽略掉，然后求其最大值即为相邻元素最大差值。对于数组元素，我们只需遍历一遍，找到其所属的桶，然后记录桶的max，min
     * 无需对数组进行排序。
     * 时间复杂度O(n)，空间复杂度O(n)
     *
     * @param arr
     * @return
     */
    public static int maxGap(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            min = Math.min(min, arr[i]);
            max = Math.max(max, arr[i]);
        }
        if (min == max)
            return 0;

        boolean[] hasNum = new boolean[len + 1]; // 每个桶是否有元素
        int[] mins = new int[len + 1];
        int[] maxs = new int[len + 1];
        for (int i = 0; i < len; i++) {
            int bid = bucketId(arr[i], min, max, len);
            mins[bid] = hasNum[bid] ? Math.min(mins[bid], arr[i]) : arr[i]; // 若还没有元素，则当前是最小元素，否则比较入桶的元素
            maxs[bid] = hasNum[bid] ? Math.max(maxs[bid], arr[i]) : arr[i];
            hasNum[bid] = true;
        }

        int res = 0;
        int lastMax = maxs[0];
        for (int i = 1; i < len + 1; i++) { // 从第二个桶开始，减去前面相邻桶的最大值(跳过空桶)
            if (hasNum[i]) {
                res = Math.max(res, mins[i] - lastMax); // 当前桶min - 前一个桶的max
                lastMax = maxs[i];
            }
        }

        return res;
    }

    /**
     * 根据元素值将其划分到对应范围的桶，返回桶的id，即桶索引
     *
     * @param value 元素
     * @param min   均分范围下界
     * @param max   均分范围上界
     * @param len   数组长度
     * @return 桶的id，即桶索引
     */
    public static int bucketId(int value, int min, int max, int len) {
        return (int) ((value - min) * len / (max - min));
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generatePosRandomArray(5, 20);
        System.out.println(Arrays.toString(arr));
        System.out.println("after algorithm.sort:");
        quickSort(arr); // 为了便于验证，实际不用排序
        System.out.println(Arrays.toString(arr));
        System.out.println("max gap:");
        System.out.println(maxGap(arr));
    }
}
