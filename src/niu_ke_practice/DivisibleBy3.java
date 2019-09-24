package niu_ke_practice;

import java.util.Scanner;

/**
 * 题目描述
 * 小Q得到一个神奇的数列: 1, 12, 123,...12345678910,1234567891011...。
 * <p>
 * 并且小Q对于能否被3整除这个性质很感兴趣。
 * <p>
 * 小Q现在希望你能帮他计算一下从数列的第l个到第r个(包含端点)有多少个数可以被3整除。
 * <p>
 * 输入描述:
 * 输入包括两个整数l和r(1 <= l <= r <= 1e9), 表示要求解的区间两端。
 * 输出描述:
 * 输出一个整数, 表示区间内能被3整除的数字个数。
 * <p>
 * 等差数列求和公式： Sn = (a1+an)*n/2 = n*a1 + n(n-1)/2 * d
 */
public class DivisibleBy3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long l = sc.nextInt();
        long r = sc.nextInt();
        long sum = (1 + l) * l / 2;
        long count = 0;
        for (long i = l; i <= r; i++) {
            if (i != l) {
                sum += i;
            }

            if (sum % 3 == 0) {
                count++;
                sum = 0;
            }
        }
        System.out.println(count);
    }
}
