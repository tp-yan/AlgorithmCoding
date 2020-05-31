package JianzhiOffer.Chapter02.DataStruct;

import java.util.Stack;

/**
 * // 面试题6：从尾到头打印链表
 * // 题目：输入一个链表的头结点，从尾到头反过来打印出每个结点的值。
 */
public class Q06PrintReverseList {
    /**
     * 方法一：使用栈结果存储遍历过的节点
     *
     * @param head
     */
    public static void printReverseList1(Node head) {
        if (head == null) return;
        Stack<Integer> stack = new Stack<>();
        Node curNode = head; // 指向第一个节点
        stack.push(curNode.value);
        while (curNode.next != null) {
            curNode = curNode.next;
            stack.push(curNode.value);
        }
        while (!stack.empty()) {
            System.out.print(stack.pop() + " ");
        }
        System.out.println();
    }

    /**
     * 方法二：利用递归实现
     * 缺点：若链表很长，可能导致栈溢出
     *
     * @param head
     */
    public static void printReverseList2(Node head) {
        if (head == null) return;
        if (head.next != null)
            printReverseList2(head.next);
        System.out.print(head.value + " ");
    }

    public static void main(String[] args) {
        // 常规用例
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);

        // 0->9->8->null
        Node head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = null;

        printReverseList1(head1);
        printReverseList1(head2);
        printReverseList1(null); // 空链表
        printReverseList1(head2.next.next); // 只有一个节点的链表

        System.out.println();
        printReverseList2(head1);
        printReverseList2(head2);
        printReverseList2(null);
    }

    static class Node {
        public int value;
        Node next;

        public Node(int value) {
            this.value = value;
        }
    }
}
