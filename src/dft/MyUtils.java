package dft;

public class MyUtils {

    /**
     * 深层复制一个数组副本
     *
     * @param source 原数组
     * @return 数组副本
     */
    public static int[] copyArray(int[] source) {
        if (source == null)
            return null;
        int[] copy = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i];
        }
        return copy;
    }

    /**
     * 随机数发生器：生成随机数组用于测试，有正有负
     *
     * @param len 随机的数组长度
     * @param max 最大值
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
     * 生成随机正数数组
     */
    public static int[] generatePosRandomArray(int len, int max) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * (max + 1));
        }
        return arr;
    }

    /**
     * 打印数组
     *
     * @param arr
     */
    public static void printArray(int[] arr) {
        for (int value : arr) {
            System.out.print(value + "\t");
        }
        System.out.println();
    }

    /**
     * 打印矩阵
     *
     * @param matrix
     */
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 交换数组arr第i,j上的元素，使用中一个间变量
     *
     * @param arr
     * @param i
     * @param j
     */
    public static void swap_i_j(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 交换数组arr第i,j上的元素，不使用中间变量
     * 亲测有效！
     *
     * @param arr
     * @param i
     * @param j
     */
    public static void swap_i_j_direct(int[] arr, int i, int j) {
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    /**
     * 将一个数组逆序
     *
     * @param arr
     */
    public static void reverseArray(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len / 2; i++) {
            swap_i_j(arr, i, len - i - 1);    // 交换数组前后对称元素
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 2};
        swap_i_j_direct(arr, 1, 2);
        printArray(arr);
    }
}
