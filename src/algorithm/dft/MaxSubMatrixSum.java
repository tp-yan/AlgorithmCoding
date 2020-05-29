package algorithm.dft;

import algorithm.MyUtils;

/**
 * 问题：求解矩阵的最大子矩阵和，子矩阵包括自身
 * 解法：以最大子段和为基础，求解最大子矩阵和。
 * 先看另一个基础问题：假设有一个矩阵 mxn，求包含其所有行的子矩阵的最大和，也就是选择该矩阵的某些连续列组成的子矩阵和最大
 * 解法：将该矩阵的各列累加，得到一个行数组，再求该数组的最大子段和，即为解
 * 从上面的基础问题出发，解最大子矩阵MxN和：对该矩阵不同行组合得到的子矩阵mxn都看成基础问题求解，在所有解中最大解的即为最大子矩阵和
 * 所有行组成的子矩阵情况：
 * 1. 必须包含第1行，然后依次逐渐添加第2、3、..、n行，形成：只有第1行的矩阵，1~2行的矩阵，...,1~n行的矩阵。共有n种情况
 * 2. 必须包含第2行，然后依次逐渐添加第3、..、n行，形成：只有第2行的矩阵，2~3行的矩阵，...,2~n行的矩阵。共有n-1种情况
 * ...
 * n. 只有第n行的矩阵。共有1种情况
 * 所以需要求解 1+2+...+n个基础问题的解，其中最大者即为最大子矩阵和
 * 时间复杂度：O(N^2*M)
 */
public class MaxSubMatrixSum {

    public static int maxSubMatrixSum(int[][] matrix) {
        int max = Integer.MIN_VALUE;
        int height = matrix.length;
        int width = matrix[0].length;

        long startTime = System.currentTimeMillis();
        // 我自己的实现：存在大量重复计算，每个子矩阵求列的和时，之前已经计算过的行，又被重新计算
        for (int i = 0; i < height; i++) {
            for (int j = i; j < height; j++) {
                // i,j确定了子矩阵的起始、终止位置
                int[] rowArr = new int[width];  // 行数组，累加当前子矩阵的各列。默认初始为0
                for (int k = i; k <= j; k++) {  // 遍历i~j的每一行
                    for (int l = 0; l < width; l++) {
                        rowArr[l] += matrix[k][l];
                    }
                }
                max = Math.max(MaxSubArraySum.maxSum(rowArr), max);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Max:" + max);
        System.out.println("My coding time:" + (endTime-startTime)  + "ms");

        startTime = System.currentTimeMillis();
        // 更高效的实现：解决重复计算问题，记住上个矩阵的列求和得到的行数组，比如 上一个矩阵是1,2行，计算了各列累加和得到 数组 v，
        // 现在新矩阵是 1,2,3行，求该矩阵的各列累加和，只需要 v' = v + 第3行即可
        // 关键：矩阵累加
        max = Integer.MIN_VALUE;
        int cur = 0;
        int[] s = null; // 上个矩阵的咧累加和数组
        for (int i = 0; i < height; i++) {
            s = new int[width];
            for (int j = i; j < height; j++) {// 确定了i,j就确定了当前的矩阵
                cur = 0;
                // 将当前矩阵的最后一行，即第j行，累加到 s 上，成为当前矩阵的列累加和
                for (int k = 0; k < width; k++) {
                    s[k] += matrix[j][k];
                    // 在 数组 s 上应用最大子段和算法即可
                    cur += s[k];
                    max = Math.max(max, cur);
                    cur = cur < 0 ? 0 : cur;
                }
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println("Max:" + max);
        System.out.println("Best coding time:" + (endTime-startTime)  + "ms");

        return max;
    }

    public static void main(String[] args) {
        int m = 50;
        int n = 50;
        int[][] matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = (int) (Math.random() * 20 - 10);
            }
        }
        MyUtils.printMatrix(matrix);

        System.out.println("max sub-matrix sum:");
        System.out.println(maxSubMatrixSum(matrix));
    }
}
