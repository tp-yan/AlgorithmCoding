package algorithm;

import algorithm.dft.Bean;
import algorithm.dft.Bean.*;

import java.util.HashMap;

/**
 * 复制含有随机指针节点的链表
 * 【题目】
 * 一种特殊的链表节点类描述如下：
 * public class Node {
 * public int value;
 * public Node next;
 * public Node rand;
 * public Node(int data) {
 * this.value = data;
 * }
 * }
 * Node类中的value是节点值，next指针和正常单链表中next指针的意义一
 * 样，都指向下一个节点，rand指针是Node类中新增的指针，这个指针可
 * 能指向链表中的任意一个节点，也可能指向null。
 * 给定一个由Node节点类型组成的无环单链表的头节点head，请实现一个
 * 函数完成这个链表中所有结构的复制，并返回复制的新链表的头节点。
 * 进阶：不使用额外的数据结构，只用有限几个变量，且在时间复杂度为
 * O(N)内完成原问题要实现的函数。
 * <p>
 * 解1：
 * 利用HashMap，将原链表节点作为key，其克隆节点作为value，存入HashMap中，这样就增加了原节点和克隆节点之间的连接关系!!!
 * 利用原节点就可以找到克隆节点。
 * 复制过程：依次遍历HashMap中的key，取出其value，先将key的next指针复制给value节点的next指针，然后根据key的random指针，从HashMap
 * 中取出其对应的节点node，而以node为key的value就是node的克隆节点，将前者的value的random指向node的value即可完成random指针的复制。
 * <p>
 * 解2：
 * 遍历原链表，在每个原节点后面插入其复制节点，新构成的链表中每个原节点的下一个是其克隆节点，这样就像HashMap一样有了原节点到新节点的映射关系。
 * 然后类似基于HashMap的方法，复制指针指向，最后再分离新老链表
 */

public class CopyListWithRandom {
    // 方法一
    public static Bean.NodeWithRandom copyListWithRand1(NodeWithRandom head) {
        HashMap<NodeWithRandom, NodeWithRandom> hashMap = new HashMap<>();
        NodeWithRandom cur = head;
        while (cur != null) {
            hashMap.put(cur, new NodeWithRandom(cur.value));
            cur = cur.next;
        }
        cur = head;
        while (cur != null) {
            NodeWithRandom clone = hashMap.get(cur);
            clone.next = hashMap.get(cur.next);
            clone.rand = hashMap.get(cur.rand);
            cur = cur.next;
        }
        return hashMap.get(head);
    }

    // 方法二
    public static NodeWithRandom copyListWithRand2(NodeWithRandom head) {
        if (head == null) {
            return null;
        }
        NodeWithRandom cur = head;
        NodeWithRandom next = null;
        while (cur != null) { // 复制链表节点
            next = cur.next;
            NodeWithRandom node = new NodeWithRandom(cur.value);
            cur.next = node;
            node.next = next;
            cur = next;
        }
        cur = head;
        NodeWithRandom curCopy = null;
        while (cur != null) {// 复制 rand 指针
            next = cur.next.next;
            curCopy = cur.next;
            curCopy.rand = cur.rand != null ? cur.rand.next : null;
            cur = next;
        }
        NodeWithRandom newHead = head.next;
        cur = head;
        while (cur != null) {
            next = cur.next.next;
            curCopy = cur.next;
            cur.next = next;
            curCopy.next = next != null ? next.next : null;
            cur = next;
        }
        return newHead;
    }

    public static void printRandLinkedList(NodeWithRandom head) {
        NodeWithRandom cur = head;
        System.out.print("order: ");
        while (cur != null) {
            System.out.print(cur.value + " ");
            cur = cur.next;
        }
        System.out.println();
        cur = head;
        System.out.print("rand:  ");
        while (cur != null) {
            System.out.print(cur.rand == null ? "- " : cur.rand.value + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        NodeWithRandom head = null;
        NodeWithRandom res1 = null;
        NodeWithRandom res2 = null;
        printRandLinkedList(head);
        res1 = copyListWithRand1(head);
        printRandLinkedList(res1);
        res2 = copyListWithRand2(head);
        printRandLinkedList(res2);
        printRandLinkedList(head);
        System.out.println("=========================");

        head = new NodeWithRandom(1);
        head.next = new NodeWithRandom(2);
        head.next.next = new NodeWithRandom(3);
        head.next.next.next = new NodeWithRandom(4);
        head.next.next.next.next = new NodeWithRandom(5);
        head.next.next.next.next.next = new NodeWithRandom(6);

        head.rand = head.next.next.next.next.next; // 1 -> 6
        head.next.rand = head.next.next.next.next.next; // 2 -> 6
        head.next.next.rand = head.next.next.next.next; // 3 -> 5
        head.next.next.next.rand = head.next.next; // 4 -> 3
        head.next.next.next.next.rand = null; // 5 -> null
        head.next.next.next.next.next.rand = head.next.next.next; // 6 -> 4

        printRandLinkedList(head);
        res1 = copyListWithRand1(head);
        printRandLinkedList(res1);
        res2 = copyListWithRand2(head);
        printRandLinkedList(res2);
        printRandLinkedList(head);
        System.out.println("=========================");

    }

}
