package homework;

import java.util.Scanner;

/**
 * 输入一个正整数n,求n!(即阶乘)末尾有多少个0？ 比如: n = 10; n! = 3628800,所以答案为2
 */
public class P02_zero_of_tail {
    public static int factorial(int n) {
        if (n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    /**
     *
     * @param n
     * @return
     */
    public static  int counter(int n) {
        int count = 0;
        if (n < 10) {
            int res = factorial(n);
            while (Integer.toString(res).endsWith("0")) {
                count++;
                res /= 10;
            }
        } else {
            int res = factorial(10);
            while (Integer.toString(res).endsWith("0")) {
                count++;
                res /= 10;
            }
            res = res % 1000;
            for (int i = 11; i <= n; i++) {
                res *= i;
                while (Integer.toString(res).endsWith("0")) {
                    count++;
                    res /= 10;
                }
                res = res % 1000;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        System.out.println(counter(n));
    }
}
