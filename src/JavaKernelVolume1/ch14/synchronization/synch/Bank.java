package JavaKernelVolume1.ch14.synchronization.synch;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A bank with a number of bank accounts that uses locks for serializing access.
 */
public class Bank {
    private final double[] accounts;
    private Lock banLock;
    // 通常，线程进入临界区，却发现在某一条件满足之后它才能执行。要使用一个条件对象来管理那些已经获得了一个锁但是却不能做有用工作的线程。
    private Condition sufficientFunds; // 锁的条件对象

    public Bank(int n, double initBalance) {
        accounts = new double[n];
        Arrays.fill(accounts, initBalance);
        banLock = new ReentrantLock();
        sufficientFunds = banLock.newCondition();
    }

    public void transfer(int from, int to, double amount) throws InterruptedException {
        banLock.lock();
        try {
            /*
              一旦一个线程调用await方法，它进入该条件的等待集。当锁可用时，该线程不能马上解除阻塞。相反，它处于阻塞状态，
              直到另一个线程调用同一条件上的signalAll 方法时为止。由于无法确保该条件被满足——signalAll 方法仅仅是通知正在等待的线程，
              所以一旦锁成为可用的，某个线程从await 调用返回，获得该锁并从被阻塞的地方继续执行时，应该再次检测该条件，
              所以通常对await 的调用应该在如下形式的循环体中：
                while(!(ok to proceed))
                    condition.await();
            * */
            while (accounts[from] < amount) // 必须确保没有其他线程在本检査余额与转账活动之间修改余额。
                sufficientFunds.await(); // 当前线程现在被阻塞了，并放弃了锁。

            System.out.print(Thread.currentThread());
            accounts[from] -= amount;
            System.out.printf(" %10.2f from %d to %d", amount, from, to);
            accounts[to] += amount;
            /*
             * 锁是可重入的，因为线程可以重复地获得已经持有的锁。锁保持一个持有计数（hold count ) 来跟踪对lock 方法的嵌套调用。
             * 被一个锁保护的代码可以调用另一个使用相同的锁的方法。
             * */
            System.out.printf(" Total Balance: %10.2f /n", getTotalBalance());
            sufficientFunds.signalAll(); // 这一调用重新激活因为这一条件而等待的所有线程。激活后，线程自己去竞争CPU时间片
        } finally {
            banLock.unlock();
        }
    }

    public double getTotalBalance() {
        banLock.lock();
        try {
            double sum = 0;
            for (double a : accounts) {
                sum += a;
            }
            return sum;
        } finally {
            banLock.unlock();
        }
    }

    public int size() {
        return accounts.length;
    }
}
