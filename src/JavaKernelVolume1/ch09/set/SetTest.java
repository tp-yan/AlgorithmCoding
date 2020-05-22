package JavaKernelVolume1.ch09.set;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/*
 * 0.在Java 中，散列表(HashTable)用链表数组实现。每个列表被称为桶（bucket)，每个桶存放一个链表 。
 * 1.HashSet类，它实现了基于散列表的集。
 * 2.散列集迭代器将依次访问所有的桶。由于散列将元素分散在表的各个位置上，所以访问它们的顺序几乎是随机的。
 * 3.在JavaSE 8 中，桶满时会从链表变为平衡二叉树。
 * 4.警告：在更改集中的元素时要格外小心。如果元素的散列码发生了改变，元素在数据结构中的位置也会发生变化。
 * */
public class SetTest {
    public static void main(String[] args) {
        Set<String> words = new HashSet<>();
        long totalTime = 0;
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNext()) {
                String word = in.next();
                long callTime = System.currentTimeMillis();
                words.add(word); // 计算添加对象的 HashCode，确定其存放的桶位置
                callTime = System.currentTimeMillis() - callTime;
                totalTime += callTime;
            }
        }

        Iterator<String> iter = words.iterator();
        for (int i = 1; i <= 20 && iter.hasNext(); i++) {
            System.out.println(iter.next());
        }
        System.out.println("...");
        System.out.println(words.size() + " distinct words. " + totalTime + " milliseconds.");
    }
}
