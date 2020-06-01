package JianzhiOffer.Chapter02.Algorithm;

/**
 * // 面试题10：斐波那契数列
 * // 题目：写一个函数，输入n，求斐波那契（Fibonacci）数列的第n项。
 * 面试时使用递归实现不得分，时间复杂度太高，还很容易栈溢出。
 * 一般使用循环来实现。避免重复计算，时间复杂度O(n)。
 * <p>
 * 另外一种使用“矩阵乘方”来计算，时间复杂度O(logN)
 * <p>
 * <p>
 * 题目二：一只青蛙一次可以跳上1级台阶，也可以跳上2级台阶。求该青蛙跳上一个n级的台阶总共有多少种跳法？
 * 解：
 * 1)如果只有1级台阶，那显然只有一种跳法
 * 2)如果有2级台阶，那么就有2种跳法，一种是分2次跳。每次跳1级，另一种就是一次跳2级
 * 3)如果台阶级数大于2，设为n的话，这时我们把n级台阶时的跳法看成n的函数，记为f(n)。
 * 第一次跳的时候有2种不同的选择：一是第一次跳一级，此时跳法的数目等于后面剩下的n-1级台阶的跳法数目，即为f(n-1)
 * 二是第一次跳二级，此时跳法的数目等于后面剩下的n-2级台阶的跳法数目，即为f(n-2),因此n级台阶的不同跳法的总数为f(n)=f(n-1)+f(n-2)，
 * 不难看出就是斐波那契数列！
 * 比如有3个台阶，可以跳1阶，或者跳2阶，应该把当前所有可能的选择都加起来才是总共的跳法。
 */
public class Q10Fibonacci {
    public static long fibonacci(int n) {
        if (n < 0) throw new IllegalArgumentException("n must >= 0");
        if (n == 0 || n == 1) return n;
        int a = 0, b = 1;
        int tmp;
        for (int i = 2; i <= n; i++) {
            tmp = a + b;
            a = b;
            b = tmp;
        }
        return b;
    }

    public static long allSteps(int n) {
        if (n < 0) throw new IllegalArgumentException("n must >= 0");
        if (n <= 2) return n;
        return allSteps(n - 1) + allSteps(n - 2);
    }

    /**
     * 相关题目
     * 用2*1的小矩形横着或者竖着去覆盖更大的矩形。请问用n个2*1的小矩形无重叠地覆盖一个2*n的大矩形，总共有多少种方法？
     * <p>
     * 若有8个2x1的大矩阵待覆盖，使用函数表示f(8)，当用一个2x1去覆盖这8个矩阵时，有2种覆盖方式。
     * 竖着放：那么就剩下 7个2x1的矩阵需要覆盖，使用函数表示为f(7)；
     * 横着放：必须用2个2x1横着放，然后剩下6个2x1的矩阵需要覆盖使用函数表示为f(7)；
     * 故 f(8) = f(7)+f(6) 还是斐波那契数列！
     */
    public static long matrixCover(int n) {
        if (n < 0) throw new IllegalArgumentException("n must >= 0");
        if (n <= 2) return n;
        return matrixCover(n - 1) + matrixCover(n - 2);
    }


    public static void main(String[] args) {
        Test(0, 0);
        Test(1, 1);
        Test(2, 1);
        Test(3, 2);
        Test(4, 3);
        Test(5, 5);
        Test(6, 8);
        Test(7, 13);
        Test(8, 21);
        Test(9, 34);
        Test(10, 55);

        Test(40, 102334155);
    }

    // ====================测试代码====================
    static void Test(int n, int expected) {
        if (fibonacci(n) == expected)
            System.out.printf("Test for %d in solution1 passed.\n", n);
        else
            System.out.printf("Test for %d in solution1 failed.\n", n);

        if (fibonacci(n) == expected)
            System.out.printf("Test for %d in solution2 passed.\n", n);
        else
            System.out.printf("Test for %d in solution2 failed.\n", n);

        if (fibonacci(n) == expected)
            System.out.printf("Test for %d in solution3 passed.\n", n);
        else
            System.out.printf("Test for %d in solution3 failed.\n", n);
    }
}
