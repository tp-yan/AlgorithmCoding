package JavaKernelVolume1.ch14.concurrentSet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * 线程安全的集合能保证并发地安全修改一个数据结构，但线程安全的集合只有少数方法实现原子更新，即保证数据结构安全不保证集合内容的安全，这需要自己去保护。
 * 线程安全的集合返回弱一致性（weakly consistent) 的迭代器。这意味着迭代器不一定能反映出它们被构造之后的所有的修改，若是原来的集合类
 * 返回的是java.util 包中迭代器，若集合如果在迭代器构造之后发生改变，迭代器就会抛出并发修改异常。
 * ConcurrentHashMap 中不允许有null 值。
 */
public class ConcurrentSetTest {
    public static void main(String[] args) throws IOException {
        // 1.Map条目的原子更新
        ConcurrentHashMap<String, Long> counter = new ConcurrentHashMap<>();
        String word = "name";
        // ============== 这系列操作不是原子性的，并发修改不安全 ===============
        Long oldValue = counter.get(word);
        Long newValue = oldValue == null ? 1 : oldValue + 1;
        counter.put(word, newValue);
        /* 代替方法：传统的做法是使用replace 操作，它会以原子方式用一个新值替换原值，前提是之前没有其他线程把原值替换为其他值。
          必须一直这么做，直到replace 成功： */
        do {
            oldValue = counter.get(word);
            newValue = oldValue == null ? 1 : oldValue + 1;
        } while (!counter.replace(word, oldValue, newValue)); // replace与AtomicLong的compareAndSet一样是原子性的。

        // (1)Java SE 8 提供了一些可以更方便地完成原子更新的方法。
        // a.调用compute 方法时可以提供一个键和一个计算新值的函数。
        counter.compute(word, (k, v) -> v == null ? 1 : v + 1);
        /* (2) merge方法有一个参数表示键不存在时使用的初始值。否则，就会调用你提供的函数来结合原值与初始值（与compute不同，这个函数不处理键）
         * merge方法适合解决：首次增加一个键时需要做些特殊的处理。 */
        counter.merge(word, 1L, (existingValue, nValue) -> existingValue + nValue);
        counter.merge(word, 1L, Long::sum);

        // (3)或者可以使用一个ConcurrentHashMap<String，AtomicLong>，在Java SE 8 中，还可以使用ConcurrentHashMap<String，LongAdder>
        ConcurrentHashMap<String, LongAdder> map = new ConcurrentHashMap<>();
        // merge方法相当于同时处理下述2条
        map.putIfAbsent(word, new LongAdder());
        map.get(word).increment();
        map.putIfAbsent(word, new LongAdder()).increment(); // 合并上述2条

        /* (4) 另外还有computeIfPresent 和computeIfAbsent 方法，它们分别只在已经有原值的情况下计算新值，
            或者只有没有原值的情况下计算新值。可以如下更新一个LongAdder 计数器映射： */
        map.computeIfAbsent(word, k -> new LongAdder()).increment();

        // 2.批操作
        // (1) search：为每个键或值提供一个函数，直到函数生成一个非null 的结果。然后搜索终止，返回这个函数的结果。
        long threshold = 2;
        // 找出第一个出现次数超过1000 次的单词。需要搜索键和值：
        String result = counter.search(threshold, (k, v) -> v > 1000 ? k : null); // 当元素多于threshold时，采用多线程处理
        // (2) forEach
        // a.第一个只为各个映射条目提供一个消费者函数，例如：
        counter.forEach(threshold, (k, v) -> System.out.println(k + " -> " + v));
        // b.第二种形式还有一个转换器函数，这个函数要先提供，其结果会传递到消费者。
        // 转换器可以用作为一个过滤器。只要转换器返回null , 这个值就会被跳过。
        // 下面只打印有大值(> 1000)的条目：
        counter.forEach(threshold, (k, v) -> v > 1000 ? k + " ->" + v : null, System.out::println);
        // (3) reduce 操作用一个累加函数组合其输入
        Long sum = counter.reduceValues(threshold, Long::sum);
        // 与forEach 类似，也可以提供一个转换器函数.可以如下计算最长的键的长度：
        Integer maxLength = counter.reduceKeys(threshold,
                String::length, // 转换器，处理结果传给Integer::max
                Integer::max); // 2元操作函数。
        // 统计多少个条目的值> 1000:
        Long count = counter.reduceValues(threshold,
                v -> v > 1000 ? 1L : null,
                Long::sum); // 如果只有一个元素，则返回其转换结果，不会应用累加器。

        // 3.并行数组算法
        // (1)静态Arrays.parallelSort 方法可以对一个基本类型值或对象的数组排序。
        // 从文件直接读入字符串
        String contents = new String(Files.readAllBytes(Paths.get("D:\\Project\\JavaProjects\\AlgorithmCoding\\src\\JavaKernelVolume1\\ch14\\concurrentSet\\alice.txt")), StandardCharsets.UTF_8);
        String[] words = contents.split("[\\P{L}]+"); // 分割单词
        Arrays.parallelSort(words);
        System.out.println(Arrays.toString(words));
        // 对对象排序时，可以提供一个Comparator
        Arrays.parallelSort(words, Comparator.comparing(String::length));
        System.out.println(Arrays.toString(words));
        // (2) parallelSetAll 方法会用由一个函数计算得到的值填充一个数组。这个函数接收元素索引，然后计算相应位置上的值。
        int[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Arrays.parallelSetAll(values, index -> index * values[index]);
        System.out.println(Arrays.toString(values));
        // (3) parallelPrefix 方法，它会用对应一个给定结合操作的前缀的累加结果替换各个数组元素。
        // 考虑数组[1，2, 3, 4, . . .] 和 x 操作。执行后数组将包含：
        // [1, 1x 2, 1x 2 x 3, 1 x 2 x 3 x 4, . . .]
        int[] vs = {1, 2, 3, 4, 5};
        Arrays.parallelPrefix(vs, (x, y) -> x * y);
        System.out.println(Arrays.toString(vs));
    }
}
