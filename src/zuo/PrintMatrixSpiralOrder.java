package zuo;

/**
 * 转圈打印矩阵
 * 【题目】
 * 给定一个整型矩阵matrix，请按照转圈的方式打印它。
 * 例如：
 * 1 2 3 4
 * 5 6 7 8
 * 9 10 11 12
 * 13 14 15 16
 * 打印结果为：1，2，3，4，8，12，16，15，14，13，9，5，6，7，11，
 * 10
 * 【要求】
 * 额外空间复杂度为O(1)。
 * <p>
 * 解：对打印的矩阵，确定矩阵的左上角A和右下角点B，假设一个遍历点C，从左上角点A开始，向右移动，直到C的列坐标与B的列坐标相等，然后C
 * 向下移动直到C的行坐标与B的行坐标相等，以此类推，C再向左移动，最后向上移动，一圈过后把矩阵最外围打印，然后A向右下角移一位，B向
 * 左上角移一位，缩小矩阵(即去除已打印外围)，重复上述过程，当A的行坐标大于B的行坐标或者A的列坐标大于B的列坐标时，停止，代表所有元素
 * 被打印
 */
public class PrintMatrixSpiralOrder {
    public static void spiralOrderPrint(int[][] matrix) {
        if (matrix != null) {
            int rA = 0;
            int cA = 0;
            int rB = matrix.length - 1;
            int cB = matrix[0].length - 1;

            // 可以写成递归版本
            while (rA <= rB && cA <= cB) {
                printEdge(matrix, rA++, cA++, rB--, cB--); // A 往右下角移一位，B往左上角移一位
            }
        }
    }

    /**
     * 打印矩阵 matrix 外围元素
     *
     * @param matrix
     * @param rA     matrix左上角点行坐标
     * @param cA     matrix左上角点列坐标
     * @param rB     同上
     * @param cB
     */
    public static void printEdge(int[][] matrix, int rA, int cA, int rB, int cB) {
        if (rA == rB) { // 矩阵为一行
            for (int i = cA; i <= cB; i++) {
                System.out.print(matrix[rA][i] + " ");
            }
        } else if (cA == cB) {// 矩阵为一列
            for (int i = rA; i <= rB; i++) {
                System.out.print(matrix[i][cA] + " ");
            }
        } else {
            int curRow = rA;
            int curCol = cA;
            while (curCol < cB) { // → 右移,末尾元素不打印
                System.out.println(matrix[curRow][curCol++]);
            }
            while (curRow < rB) { // ↓下移
                System.out.println(matrix[curRow++][curCol]);
            }
            while (curCol > cA) { // ← 左移
                System.out.println(matrix[curRow][curCol--]);
            }
            while (curRow > rA) { // ← 左移
                System.out.println(matrix[curRow--][curCol]);
            }
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12},
                {13, 14, 15, 16}};
        spiralOrderPrint(matrix);
    }
}
