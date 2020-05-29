package algorithm;

import algorithm.dft.Bean.*;

/**
 * 两个单链表相交的一系列问题
 * 【题目】
 * 在本题中，单链表可能有环，也可能无环。给定两个单链表的头节点
 * head1和head2，这两个链表可能相交，也可能不相交。请实现一个函数，
 * 如果两个链表相交，请返回相交的第一个节点；如果不相交，返回null即可。
 * 要求：如果链表1的长度为N，链表2的长度为M，时间复杂度请达到
 * O(N+M)，额外空间复杂度请达到O(1)。
 * <p>
 * 单链表有环：最后一个节点的 next 指针不为空，而是指向链表中某个节点。
 * 如何判断单链表有环：（假设为getLoopNode函数）
 * 1. （利用HashSet）遍历链表把节点放入 HashSet 中，若遍历过程中节点已在HashSet中，那么说明有环，若直到遍历到null，HashSet中都没有key冲突，则无环。
 * 2. 快慢指针：快指针Fast走2步，慢指针Slow走1步。若Fast最后走到null，则无环。若有环，那么Fast和Slow会在环上相遇。相遇后Fast指向开头，变成每次走一步，
 * 此时Fast和Slow一起走，则它们会在入环节点相遇。（由数学证明）
 * <p>
 * 判断2条链表是否相交：
 * 1. 若2条皆是无环单链表（getLoopNode返回都为null），若他们相交只能呈“Y”字型（因为是单链表，一个节点只有一个next指针），否则不相交。
 * 解(1)：将链表1的所有节点放入HashSet中，然后依次放入链表2的节点，若key已存在则说明相交，返回该节点即可
 * 解(2): 分别遍历链表1,2，统计它们的长度len1,len2，以及得到它们的最后一个节点end1,end2，若 end1 ≠ end2 则它们不相交，反之相交。
 * 假设len1 > len2 ，现在从2个链表头开始，链表1先走 (len1 - len2)步，然后链表1，2再同时走，当它们遍历到相同节点时，即为第一个相交节点。
 * 2. 一个有环，一个无环，不可能相交
 * 3. 2个都有环，有3种情况
 * （1）2条链表在入环之前就已相交：“>-O”这种形状。若抛开“O”部分，那么剩余部分就和前面的 1. 问题一样
 * (2) 2条链表在环上相交：“♉”。找到链表1的入环节点loop1，然后往后走，若在回到loop1之前遇到链表2入环节点loop2，则说明2链表在环上
 * 相交，此时返回loop1或loop2都对。若回到loop1时没遇到loop2，则说明它们是独立不相交的有环链表，即第(3)种情况
 * (3) "6 6"型，不相交
 */
public class FindFirstIntersectNode {
    /**
     * 判断2条单链表（也许有环）是否相交，若相交返回第一个相交节点
     *
     * @param head1
     * @param head2
     * @return
     */
    public static Node getIntersectNode(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        Node loop1 = getLoopNode(head1);
        Node loop2 = getLoopNode(head2);
        if (loop1 == null && loop2 == null) { // 2条无环单链表
            // 1. 将链表1的所有节点放入HashSet，再讲链表2节点放入，key存在则说明有环，省略
            // 2. 不使用HashSet
            return noLoop(head1, head2);
        } else if (loop1 != null && loop2 != null) { // 2条有环链表
            return bothLoop(head1, loop1, head2, loop2);
        }
        return null; // 一条有环，一条无环，不可能相交
    }

    private static Node bothLoop(Node head1, Node loop1, Node head2, Node loop2) {
        Node cur1 = head1;
        Node cur2 = head2;
        if (loop1 == loop2) {// “>-O”，去除环部分，剩余则为2无环单链表相交问题
            int n = 0;
            while (cur1 != loop1) {
                n++;
                cur1 = cur1.next;
            }
            while (cur2 != loop2) {
                n--;
                cur2 =cur2.next;
            }
            cur1 = n > 0 ? head1:head2;
            cur2 = cur1 == head1?head2:head1;
            n = Math.abs(n);
            while (n > 0) {
                n--;
                cur1 = cur1.next;
            }
            while (cur1 != cur2) {
                cur1 = cur1.next;
                cur2 = cur2.next;
            }
            return cur1;
        }else {
            cur1 = loop1.next;
            while (cur1 != loop1) {
                if (cur1 == loop2) {
                    return loop1; // 相交必返回
                }
                cur1 = cur1.next;
            }
            return null; // 不相交
        }
    }


    private static Node noLoop(Node head1, Node head2) {
        Node cur1 = head1;
        Node cur2 = head2;
        int n = 0; // 2链表节点个数差值

        // cur1 cur2分别指向2链表最后一个节点
        while (cur1.next != null) {
            n++;
            cur1 = cur1.next;
        }
        while (cur2.next != null) {
            n--;
            cur2 = cur2.next;
        }
        if (cur1 != cur2) {// 不相交
            return null;
        }

        cur1 = n > 0 ? head1 : head2;  // cur1指向长链表
        cur2 = cur1 == head1 ? head2 : head1;  // cur2 指向短链表
        n = Math.abs(n);
        while (n > 0) {
            cur1 = cur1.next;
            n--;
        }
        while (cur1 != cur2) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        return cur1;
    }


    /**
     * 判断一个单链表是否有环，若有环返回第一个入环节点，否则返回null
     *
     * @param head
     * @return
     */
    public static Node getLoopNode(Node head) {
        // 1. 使用HashSet
        /*
         HashSet<Node> hashSet = new HashSet<>();
         while (head != null) {
         if (hashSet.contains(head)) {
         return head;
         }
         hashSet.add(head);
         head = head.next;
         }
         return null;
         */

        // 2. 利用快慢指针
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        Node slow = head.next;
        Node fast = head.next.next;
        while (slow != fast) {
            if (fast.next == null || fast.next.next == null) {// 若fast最终会为null，说明无环
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        // 相遇后重置fast为head，并改为一次走一步
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        // 相遇节点为第一个入环节点
        return slow;
    }

    public static void main(String[] args) {
        // 1->2->3->4->5->6->7->null
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);

        // 0->9->8->6->7->null
        Node head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next.next.next.next.next; // 8->6
        System.out.println(getIntersectNode(head1, head2).value);

        // 1->2->3->4->5->6->7->4...
        head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);
        head1.next.next.next.next.next.next = head1.next.next.next; // 7->4

        // 0->9->8->2...
        head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next; // 8->2
        System.out.println(getIntersectNode(head1, head2).value);

        // 0->9->8->6->7->4->5->6..
        head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next.next.next.next.next; // 8->6
        System.out.println(getIntersectNode(head1, head2).value);
    }
}
