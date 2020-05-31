package JianzhiOffer.Chapter02.DataStruct;

import java.util.Arrays;

/**
 * 面试题4：二维数组中的查找
 * 题目：在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按
 * 照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个
 * 整数，判断数组中是否含有该整数。
 */
public class Q04Matrix {
    public static boolean findMatrix(int[][] matrix, int target) {
        if (matrix != null && matrix.length > 0 && matrix[0].length > 0) {
            int row = 0;
            int column = matrix[0].length - 1;
            while (row < matrix.length && column >= 0) {// 行数只能增加、列数只能减少
                // 对当前行数组使用二分查找，得到
                int pos = Arrays.binarySearch(matrix[row], 0, column + 1, target);
                if (pos < 0) {// 没找到则说明插入位置：第(-pos-1)列及之后的列，都可以去掉，去些去掉的列中的元素都比target大
                    column = (-pos - 1) - 1;
                    if (column < 0)
                        return false;
                } else {
                    System.out.println("find at:" + row + "," + column);
                    return true;
                }

                // column现在所指列的第一个元素一定比target小，故在列方向也需要查找插入位置
                while (row < matrix.length && matrix[row][column] < target) row++;
                if (row >= matrix.length) return false; // 没有找指定元素
                else if (matrix[row][column] == target) {
                    System.out.println("find at:" + row + "," + column);
                    return true;
                }// 否则 matrix[row++][column] > target
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Test1();
        Test2();
        Test3();
        Test4();
        Test5();
        Test6();
        Test7();
    }

    // ====================测试代码====================
    static void Test(String testName, int[][] matrix, int rows, int columns, int number, boolean expected) {
        if (testName != null)
            System.out.printf("%s begins: ", testName);

        boolean result = findMatrix(matrix, number);
        if (result == expected)
            System.out.printf("Passed.\n");
        else
            System.out.printf("Failed.\n");
    }

    //  1   2   8   9
//  2   4   9   12
//  4   7   10  13
//  6   8   11  15
// 要查找的数在数组中
    static void Test1() {
        int matrix[][] = {{1, 2, 8, 9}, {2, 4, 9, 12}, {4, 7, 10, 13}, {6, 8, 11, 15}};
        Test("Test1", matrix, 4, 4, 7, true);
    }

    //  1   2   8   9
//  2   4   9   12
//  4   7   10  13
//  6   8   11  15
// 要查找的数不在数组中
    static void Test2() {
        int matrix[][] = {{1, 2, 8, 9}, {2, 4, 9, 12}, {4, 7, 10, 13}, {6, 8, 11, 15}};
        Test("Test2", matrix, 4, 4, 5, false);
    }

    //  1   2   8   9
//  2   4   9   12
//  4   7   10  13
//  6   8   11  15
// 要查找的数是数组中最小的数字
    static void Test3() {
        int matrix[][] = {{1, 2, 8, 9}, {2, 4, 9, 12}, {4, 7, 10, 13}, {6, 8, 11, 15}};
        Test("Test3", matrix, 4, 4, 1, true);
    }

    //  1   2   8   9
//  2   4   9   12
//  4   7   10  13
//  6   8   11  15
// 要查找的数是数组中最大的数字
    static void Test4() {
        int matrix[][] = {{1, 2, 8, 9}, {2, 4, 9, 12}, {4, 7, 10, 13}, {6, 8, 11, 15}};
        Test("Test4", matrix, 4, 4, 15, true);
    }

    //  1   2   8   9
//  2   4   9   12
//  4   7   10  13
//  6   8   11  15
// 要查找的数比数组中最小的数字还小
    static void Test5() {
        int matrix[][] = {{1, 2, 8, 9}, {2, 4, 9, 12}, {4, 7, 10, 13}, {6, 8, 11, 15}};
        Test("Test5", matrix, 4, 4, 0, false);
    }

    //  1   2   8   9
//  2   4   9   12
//  4   7   10  13
//  6   8   11  15
// 要查找的数比数组中最大的数字还大
    static void Test6() {
        int matrix[][] = {{1, 2, 8, 9}, {2, 4, 9, 12}, {4, 7, 10, 13}, {6, 8, 11, 15}};
        Test("Test6", matrix, 4, 4, 16, false);
    }

    // 鲁棒性测试，输入空指针
    static void Test7() {
        Test("Test7", null, 0, 0, 16, false);
    }
}
