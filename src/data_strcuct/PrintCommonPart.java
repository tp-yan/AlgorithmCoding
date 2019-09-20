package data_strcuct;

import dft.Bean;
import dft.Bean.Node;

import static dft.MyUtils.printLinkedList;

/**
 * 打印两个有序链表的公共部分
 * 【题目】
 * 给定两个有序链表的头指针head1和head2，打印两个链表的公共部分。
 * 解：
 * 2个指针，小指针先动，相同时，同时动并打印元素，直到其中一个为null
 */
public class PrintCommonPart {

    public static void printCommonPart(Node head1, Node head2) {
        while (head1 != null && head2 != null) {
            if (head1.value < head2.value) {
                head1 = head1.next;
            } else if (head1.value > head2.value) {
                head2 = head2.next;
            } else {
                System.out.print(head1.value + " ");
                head1 = head1.next;
                head2 = head2.next;
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node node1 = new Node(2);
        node1.next = new Node(3);
        node1.next.next = new Node(5);
        node1.next.next.next = new Node(6);

        Node node2 = new Node(1);
        node2.next = new Node(2);
        node2.next.next = new Node(5);
        node2.next.next.next = new Node(7);
        node2.next.next.next.next = new Node(8);

        printLinkedList(node1);
        printLinkedList(node2);
        printCommonPart(node1, node2);
    }
}
