package data_strcuct;

/**
 * 打印两个有序链表的公共部分
 * 【题目】
 * 给定两个有序链表的头指针head1和head2，打印两个链表的公共部分。
 * <p>
 * 解：
 * 2个指针，小指针先动，相同时，同时动并打印元素，直到其中一个为null
 */
public class PrintCommonPart {
    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    public static void printCommonPart(Node head1, Node head2) {

    }

    public static void main(String[] args) {

    }
}
