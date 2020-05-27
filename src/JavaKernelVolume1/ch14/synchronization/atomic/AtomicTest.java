package JavaKernelVolume1.ch14.synchronization.atomic;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * 原子性
 * 假设对共享变量除了赋值之外并不完成其他操作，那么可以将这些共享变量声明为volatile域。
 * 对于其他操作 java.util.concurrent.atomic 包中有很多类使用了很高效的机器级指令来保证其他操作的原子性。
 */
public class AtomicTest {
    public static void main(String[] args) {
        // 1.AtomicLong
        AtomicLong nextNumber = new AtomicLong(10);
        long id = nextNumber.incrementAndGet(); // 以原子方式将AtomicLong 自增，并返回自增后的值。相当于将 n++ 的原子实现
        System.out.println(id);
        id = nextNumber.decrementAndGet();
        System.out.println(id);
        nextNumber.set(100);
        System.out.println(nextNumber.addAndGet(11));
        // 希望完成更复杂的更新，就必须使用compareAndSet 方法。
        // 以下操作非原子性，无法保证多线程安全
        AtomicLong largest = new AtomicLong();
        long observed = 22;
        largest.set(Math.max(largest.get(), observed)); // Error race condition!
        long oldValue = 0;
        long newValue = 0;
        // 尝试修改共享变量，若失败则说明其他线程也在修改那么返回false而非异常，然后继续不断尝试修改。
        do {
            oldValue = largest.get();
            newValue = Math.max(oldValue, observed);
        } while (!largest.compareAndSet(oldValue, newValue)); // compareAndSet 方法会映射到一个处理器操作，比使用锁速度更快。
        // 在Java SE 8 中,可以提供一个lambda 表达式更新变量。这2种方式内部也是使用 “循环+compareAndSet” 来实现
        largest.updateAndGet(x -> Math.max(x, observed));
        // 或
        largest.accumulateAndGet(observed, Math::max);

        // 2.如果有大量线程要访问相同的原子值，性能会大幅下降，因为乐观更新需要太多次重试。
        // Java SE 8 提供了LongAdder 和LongAccumulator 类来解决这个问题。LongAdder 包括多个变量（加数，)其总和为当前值。可
        // 以有多个线程更新不同的加数，线程个数增加时会自动提供新的加数。通常情况下，只有当所有工作都完成之后才需要总和的值.
        // 如果认为可能存在大量竞争，只需要使用LongAdder 而不是AtomicLong。
        LongAdder adder = new LongAdder();
        // 调用increment 让计数器自增，或者调用add 来增加一个量，或者调用sum 来获取总和。
        adder.increment();
        System.out.println("adder:" + adder.sum());
        adder.add(100);
        System.out.println("adder:" + adder.sum());

        // 3.LongAccumulator 将这种思想推广到任意的累加操作。
        // 要加入新的值，可以调用accumulate。调用get 来获得当前值。
        // 在内部，这个累加器包含变量a1, a2,...,an（每个线程对应一个加数变量）。每个变量初始化为零元素（这个例子中零元素为0)
        LongAccumulator accumulator = new LongAccumulator(Long::sum, 0);
        accumulator.accumulate(100); // 调用accumulate 并提供值v 时，其中一个变量会以原子方式更新为 ai = a op v, 这里op是中缀形式的累加操作。
        // 在我们这个例子中，调用accumulate 会对某个i 计算 ai = ai + v。
        // get 的结果是a1 op q2 op . . .op an 在我们的例子中，这就是累加器的总和：a1 + a2 + … +an。
        System.out.println("accumulator:" + accumulator.get());

        DoubleAccumulator doubleAccumulator = new DoubleAccumulator(Double::max, 0);
    }
}
