package JavaKernelVolume1.ch09.set;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/* 1.如果需要对集合进行随机访问，就使用数组或ArrayList, 而不要使用链表。
   2.在Java 程序设计语言中，所有链表实际上都是双向链表。
*  3.使用链表的唯一理由是尽可能地减少在列表中间插人或删除元素所付出的代价。
*  4.避免使用以整数索引表示链表中位置的所有方法。链表不支持快速地随机访问。如果要查看链表中第n个元素，就必须从头开始，越过个n-1元素。没有捷径可走。
   5.绝对不应该使用让人误解的随机访问方法(linkedList.get(index))来遍历链表。因为每次査找一个元素都要从列表的头部重新开始搜索。
   链表是一个有序集合，LinkedList.add 方法将对象添加到链表的尾部。由于迭代器是描述集合中位置的，所以这种依赖于位的add 方法将由迭代器负责。
* */
public class LinkedListTest {
    public static void main(String[] args) {
        List<String> a = new LinkedList<>();
        a.add("Amy");
        a.add("Carl");
        a.add("Erica");

        List<String> b = new LinkedList<>();
        b.add("Bob");
        b.add("Doug");
        b.add("Frances");
        b.add("Gloria");

        // merge the words from b into a
        // add 方法只依赖于迭代器的位置，而remove 方法依赖于迭代器的状态。
        ListIterator<String> aIter = a.listIterator(); // 继承自Iterator的迭代器，可以双向迭代链表
        Iterator<String> bIter = b.iterator();
        while (bIter.hasNext()) {
            if (aIter.hasNext()) // 移动迭代器位置
                aIter.next();
            aIter.add(bIter.next()); // LinkedList的add只能在链表末尾添加元素，而 listIterator可以在链表中间添加
        }
        System.out.println(a);

        bIter = b.iterator();
        while (bIter.hasNext()) {
            bIter.next();
            if (bIter.hasNext()) { // 每隔一个元素删除一个
                bIter.next();
                bIter.remove(); // 不能连续调用，且调用之前必须先调用next
            }
        }
        System.out.println(b);

        a.removeAll(b);
        System.out.println(a);
    }
}
