package JavaKernelVolume1.ch09.algorithm;

import java.util.AbstractCollection;
import java.util.BitSet;
import java.util.Iterator;

/* BitSet 类用于存放一个位序列。
 * “Eratosthenes 筛子” 算法查找素数：
 * 将已知素数的倍数所对应的位都置为“ 关” 状态。经过这个操作保留下来的位对应的就是素数。
 * 计算2 - 2 000 000 之间的所有素数.
 * */
public class Sieve {
    public static void main(String[] args) {
        int n = 200000;
        long start = System.currentTimeMillis();
        BitSet b = new BitSet(n + 1);
        int count = 0;
        int i;
        for (i = 2; i <= n; i++) {
            b.set(i); // 全部位设为 开
        }
        i = 2;
        while (i * i <= n) {
            if (b.get(i)) {
                count++; // 累积素数个数
                int k = 2 * i; // 将素数'i'的所有倍数去除
                while (k <= n) {
                    b.clear(k);
                    k += i;
                }
            }
            i++;
        }
        while (i <= n) {
            if (b.get(i)) count++;
            i++;
        }
        long end = System.currentTimeMillis();
        System.out.println(count + " primes.");
        System.out.println((end - start) + " milliseconds.");

        new AbstractCollection<String>(){

            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }
}
