package dft;

public class MyUtils {

    /**
     * 深层复制一个数组副本
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
     * 生成随机正数数组
     */
    public static int[] generatePosRandomArray(int len,int max){
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int)(Math.random()*(max+1));
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
     * 交换数组arr第i,j上的元素
     */
    public static void swap(int[] arr,int i,int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 将一个数组逆序
     * @param arr
     */
    public static void reverseArray(int[] arr){
        int len = arr.length;
        for (int i = 0; i < len/2; i++) {
            swap(arr,i,len-i-1);    // 交换数组前后对称元素
        }
    }

}
