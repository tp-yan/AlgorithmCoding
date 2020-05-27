package JavaKernelVolume1.ch14.executor.forkJoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.DoublePredicate;

/**
 * Java SE 7 中新引入了fork-join 框架。
 * 假设有一个处理任务，它可以很自然地分解为子任务：
 * if (problemSize < threshold)
 * solve problem directly
 * else{
 * break problem into subproblems
 * recursively solve each subproblem
 * combine the results
 * }
 * 要采用框架可用的一种方式完成这种递归计算，需要提供一个扩展RecursiveTask<T>的类（如果计算会生成一个类型为T的结果）
 * 或者提供一个扩展RecursiveAction的类（如果不生成任何结果)。
 * 再覆盖compute 方法来生成并调用子任务，然后合并其结果。
 */
public class ForkJoinTest {
    public static void main(String[] args) {
        final int SIZE = 10000000;
        double[] numbers = new double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            numbers[i] = Math.random();
        }
        Counter counter = new Counter(numbers, 0, numbers.length, x -> x > 0.5);
        ForkJoinPool pool = new ForkJoinPool(); // 用于执行ForkJoinTasks的线程池
        pool.invoke(counter);
        System.out.println(counter.join());
    }
}

// 并发统计数组中符合要求的元素个数
class Counter extends RecursiveTask<Integer> {
    public static final int THRESHOLD = 1000;
    private double[] values;
    private int from;
    private int to;
    private DoublePredicate filter;

    public Counter(double[] values, int from, int to, DoublePredicate filter) {
        this.values = values;
        this.from = from;
        this.to = to;
        this.filter = filter;
    }

    @Override
    protected Integer compute() {
        if (to - from < THRESHOLD) {// 递归结束条件
            int count = 0;
            for (int i = from; i < to; i++) {
                if (filter.test(values[i])) count++; // 统计符合特性的数组元素
            }
            return count;
        } else {
            int mid = (from + to) / 2;
            // 分裂为2个子任务
            Counter first = new Counter(values, from, mid, filter);
            Counter second = new Counter(values, mid, to, filter);
            // 父线程只用等待子线程返回结果后累加即可。
            invokeAll(first, second); // invokeAll 方法接收到很多任务并阻塞，直到所有这些任务都已经完成。
            return first.join() + second.join(); // join 方法将生成结果。每个子任务应用了join, 并返回其总和。
        }
    }
}
