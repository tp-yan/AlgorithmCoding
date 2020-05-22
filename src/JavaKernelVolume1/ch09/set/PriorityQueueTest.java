package JavaKernelVolume1.ch09.set;

import java.time.LocalDate;
import java.util.PriorityQueue;

/**
 * 1.优先级队列（priority queue) 中的元素可以按照任意的顺序插人，却总是按照排序的顺序进行检索。也就是说，无论何时调用remove 方法，总会获得当前优先级队列中最小的元素。
 * 2.优先级队列并没有对所有的元素进行排序。因为优先级队列使用堆结构(自我调整的二叉树)实现的，堆只规定了：子节点值必须全小于/大于父节点值。
 * 3.迭代优先级队列中的元素是无序的，但逐一删除优先级队列中的元素是有序的，即总是删除最小的元素。
 * 4.使用优先级队列的典型示例是任务调度。每当启动一个新的任务时，都将优先级最高的任务从队列中删除（由于习惯上将1 设为“ 最高” 优先级，
 * 所以会将最小的元素删除)。
 */
public class PriorityQueueTest {
    public static void main(String[] args) {
        PriorityQueue<LocalDate> pq = new PriorityQueue<>();
        // 与TreeSet一样,PriorityQueue中的对象必须实现了Comparable或者构造时提供比较器Comparator
        pq.add(LocalDate.of(1906, 12, 9)); // C. Hopper
        pq.add(LocalDate.of(1815, 12, 10)); // A. Lovelace
        pq.add(LocalDate.of(1903, 12, 3)); // ]. von Neumann
        pq.add(LocalDate.of(1910, 6, 22)); // K. Zuse

        // 与TreeSet（基于红黑树实现的全序） 中的迭代不同，这里的迭代并不是按照元素的排列顺序访问的。
        // 而删除却总是删掉剩余元素中优先级数最小的那个元素。
        System.out.println("Iterating over elements...");
        for (LocalDate date : pq) {
            System.out.println(date);
        }
        System.out.println("Removing elements...");
        while (!pq.isEmpty()) {
            System.out.println(pq.remove());
        }
    }
}
