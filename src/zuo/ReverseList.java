package zuo;

import zuo.dft.Bean;
import zuo.dft.Bean.*;

import static zuo.MyUtils.printDoubleLinkedList;
import static zuo.MyUtils.printLinkedList;

/**
 * 反转单向和双向链表
 * 【题目】
 * 分别实现反转单向链表和反转双向链表的函数。
 * 【要求】
 * 如果链表长度为N，时间复杂度要求为O(N)，额外空间复杂度要求为O(1)
 */
public class ReverseList {
    // 反转单向链表：依次改变每个节点的方向
    public static Node reverseList(Bean.Node head) {
        Node pre = null; // 指向 当前节点cur 的上一个节点
        Node next = null;
        Node cur = head; // 可以让 head 作为当前节点
        while (cur != null) {
            next = cur.next; // 当cur不为null时，才获得next节点
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    public static DoubleNode reverseList(DoubleNode head) {
        DoubleNode pre = null;
        DoubleNode next = null;
        while (head != null) { // head 作为 cur
            next = head.next;
            head.next = pre;
            head.last = next;
            pre = head;
            head = next;
        }
        return pre;
    }


    public static void main(String[] args) {
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        printLinkedList(head1);
        head1 = reverseList(head1);
        printLinkedList(head1);

        DoubleNode head2 = new DoubleNode(1);
        head2.next = new DoubleNode(2);
        head2.next.last = head2;
        head2.next.next = new DoubleNode(3);
        head2.next.next.last = head2.next;
        head2.next.next.next = new DoubleNode(4);
        head2.next.next.next.last = head2.next.next;
        printDoubleLinkedList(head2);
        printDoubleLinkedList(reverseList(head2));
    }
}
