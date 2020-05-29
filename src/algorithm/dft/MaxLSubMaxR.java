package algorithm.dft;

/**
 * 问题：将一个数组arr划分成左右两段，使得 max左 - max右 绝对值最大
 * 解法：对于数组中的最大值Max，无论如何划分，它只能在左或右其中的一段，且它是该段的最大值，那么原问题就变成了求 Max-?的最大值，
 * ‘？’：代表另一段的最大值
 * 1）若Max被划分到左段，则‘？’代表右端的最大值，要使 Max-?最大，则右端只能包含最右端的元素，若右端还包含其他元素，则‘？’可能变大。
 * 2）若Max被划分到右段，则‘？’代表左端的最大值，同理，左段也只能包含第一个元素，因为 max左 >= arr[0]
 * 故解就是： Max - min(arr[0],arr[-1])
 */
public class MaxLSubMaxR {

    public static int maxDifference() {
        int len = 10;
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (20 * Math.random());
            System.out.print(arr[i] + "\t");
        }
        System.out.println();

        int max = arr[0];   // 数组最大值
        for (int i = 1; i < len; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }

        return max - Math.min(arr[0], arr[len - 1]);
    }

    public static void main(String[] args) {
        int max = maxDifference();
        System.out.println("Max difference:" + max);
    }

}
