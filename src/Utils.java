public class Utils {
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
}
