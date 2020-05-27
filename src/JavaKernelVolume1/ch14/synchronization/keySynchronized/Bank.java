package JavaKernelVolume1.ch14.synchronization.keySynchronized;

import java.util.Arrays;

/**
 * A bank with a number of bank accounts that uses synchronization primitives.
 * Java中的每一个对象都有一个内部锁，且内部对象锁只有一个相关条件。
 * 如果一个方法用synchronized 关键字声明，那么对象的锁将保护整个方法。也就是说，要调用该方法，线程必须获得内部的对象锁。
 * <p>
 * 注：
 * 1. 最好既不使用Lock/Condition 也不使用synchronized 关键字。在许多情况下你可以使用java.util.concurrent 包中的一种机制，它会为你处理所有的加锁。
 * 2. 如果synchronized 关键字适合你的程序，那么请尽量使用它，这样可以减少编写的代码数量，减少出错的几率。
 */
public class Bank {
    private final double[] accounts;

    public Bank(int n, double initBalance) {
        accounts = new double[n];
        Arrays.fill(accounts, initBalance);
    }

    // 使用 synchronized 来获得内部锁，跟显示声明一个锁对象类似的，只是不需要try-finally
    public synchronized void transfer(int from, int to, double amount) throws InterruptedException {
        while (accounts[from] < amount) // 等价于 sufficientFunds.await();
            wait(); // 当前线程现在被阻塞了，并放弃了锁。

        System.out.print(Thread.currentThread());
        accounts[from] -= amount;
        System.out.printf(" %10.2f from %d to %d", amount, from, to);
        accounts[to] += amount;
        System.out.printf(" Total Balance: %10.2f \n", getTotalBalance());
        notifyAll();  // 等价于 sufficientFunds.signalAll();
    }

    public synchronized double getTotalBalance() {
        double sum = 0;
        for (double a : accounts) {
            sum += a;
        }
        return sum;
    }

    public int size() {
        return accounts.length;
    }
}
