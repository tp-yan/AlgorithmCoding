package data_strcuct;

/**
 * “之”字形打印矩阵
 * 【题目】
 * 给定一个矩阵matrix，按照“之”字形的方式打印这个矩阵，例如：
 * 1 2 3 4
 * 5 6 7 8
 * 9 10 11 12
 * “之”字形打印的结果为：1，2，5，9，6，3，4，7，10，11，8，12
 * 【要求】
 * 额外空间复杂度为O(1)。
 *
 * 解：使用2个指针点AB，初始都指向矩阵左上角点。A每次向右移一位，遇到矩阵右边界时向下移一位；B每次向下移一位，遇到下边界时向右移一位；
 * AB同时移动每次移一位，最后在矩阵右下角点相遇。AB每移一次，AB就形成了一条对角线，这条对角线上的元素就是要按照ZigZag顺序打印的元素
 * 初始打印时方向是从左下往上即从B → A，下一次就从右上往左下即 A → B，打印一次AB移一次，直到AB相遇
 */
public class ZigZagPrintMatrix {

    public static void printMatrixZigZag(int[][] m) {
        if (m != null) {
            int rA = 0;
            int cA = 0;
            int rB = 0;
            int cB = 0;
            int row = m.length - 1;
            int col = m[0].length - 1;
            boolean up2down = false; // 初始方向为从下向上

            while (rA <= row && cB <= col) {
                printDiagonalLine(m, rA, cA, rB, cB, up2down);
                rA = cA == col ? rA + 1 : rA; // 右移
                cA = cA == col ? cA : cA + 1; // 下移
                cB = rB == row ? cB + 1 : cB; // 下移
                rB = rB == row ? rB : rB + 1; // 右移

                up2down = !up2down;
            }
            System.out.println();
        }
    }

    /**
     * 打印对角线元素，up2down控制方向
     *
     * @param m
     * @param rA      A点在上
     * @param cA
     * @param rB      B点在下
     * @param cB
     * @param up2down 是否从右上往左下打印
     */
    public static void printDiagonalLine(int[][] m, int rA, int cA, int rB, int cB, boolean up2down) {
        if (up2down) {// 从A → B，从右上→左下
            while (rA <= rB) {
                System.out.print(m[rA++][cA--] + " ");
            }
        } else {// 从 B → A，从左下→右上
            while (cB <= cA) {
                System.out.print(m[rB--][cB++] + " ");
            }
        }
    }


    public static void main(String[] args) {
        int[][] matrix = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 } };
        printMatrixZigZag(matrix);
    }
}
