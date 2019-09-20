package data_strcuct;

// 回文：正反序字符串相同，即从中间对称的

import dft.Bean;
import dft.Bean.Node;

import java.util.Stack;

import static dft.MyUtils.printLinkedList;

/**
 * 判断一个链表是否为回文结构
 * 【题目】
 * 给定一个链表的头节点head，请判断该链表是否为回文结构。
 * 例如：
 * 1->2->1，返回true。
 * 1->2->2->1，返回true。
 * 15->6->15，返回true。
 * 1->2->3，返回false。
 * 进阶：
 * 如果链表长度为N，时间复杂度达到O(N)，额外空间复杂度达到O(1)。
 * <p>
 * 解：
 * 1）不限空间复杂度的解：
 * 1. 将链表所有元素入栈（出栈过程形成了链表的逆序），再遍历链表元素依次与弹出的栈顶元素比较，若中间有一个不相等，则不是回文
 * 2. 使用快慢指针，初始都指向链表头结点，每次快指针先走两步，慢指针再走一步，当快指针越界时，慢指针立刻停止，此时慢指针
 * 在链表中间位置（注意奇偶长度不同），将慢指针后面的元素入栈，再依次从链表头遍历元素与出栈元素比较，...同上，此时
 * 实际只使用了 1 中一半的空间
 * 2）额外空间复杂度达到O(1)：
 * 使用快慢指针，初始都指向链表头结点，每次快指针先走两步，慢指针再走一步，当快指针越界时，慢指针立刻停止，此时慢指针
 * 在链表中间位置（注意奇偶长度不同）。将慢指针后面的元素指针方向逆序即变为： 1 → 2 → 3 ← 2 ← 1，其中 3 → null，然后从
 * 两头开始往中间遍历，当中间有元素不相同时停止，或者直到其中一个指针为null时停止，比如偶数个时：1 → 2 ← 2 ← 1，其中
 * 第一个 2 → null，当左指针为null时就停止
 */
public class IsPalindromeList {

    /**
     * 需要额外O(n)的空间
     */
    public static boolean isPalindrome1(Node head) {
        Stack<Node> stack = new Stack<>();
        Node cur = head;
        while (cur != null) {
            stack.push(cur); // node入栈
            cur = cur.next;
        }

        while (head != null) {
            if (head.value != stack.pop().value) {
                return false;
            }
            head = head.next;
        }

        return true;
    }

    /**
     * 需要额外O(n/2)的空间
     *
     * @param head
     * @return
     */
    public static boolean isPalindrome2(Node head) {
        if (head == null || head.next == null)
            return true;

        Node fast = head;
        Node slow = head;
        Stack<Node> stack = new Stack<>();

        // fast最后指向链表倒数第一个（奇数个）或者倒数第二个元素（偶数个元素），此时停止，slow指针指向链表中间位置
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;  // fast指针走两步，slow走一步
            slow = slow.next;
        }

        // slow后面的指针入栈
        slow = slow.next;
        while (slow != null) {
            stack.push(slow);
            slow = slow.next;
        }

        while (!stack.empty()) {
            if (head.value != stack.pop().value) {
                return false;
            }
            head = head.next;
        }

        return true;
    }

    /**
     * 额外O(1)的空间
     *
     * @param head
     * @return
     */
    public static boolean isPalindrome3(Node head) {
        if (head == null || head.next == null)
            return true;

        Node fast = head;
        Node slow = head;
        // fast最后指向链表倒数第一个（奇数个）或者倒数第二个元素（偶数个元素），此时停止，slow指针指向链表中间位置
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;  // fast指针走两步，slow走一步
            slow = slow.next;
        }

        // 将slow以后的链表指向反向
        fast = slow.next; // 复用 fast指针，指向右部分开始节点
        slow.next = null; // 中间元素 → null
        Node t = null;

        while (fast != null) {// fast指向当前要改变指向的元素，t指向fast的下一个元素
            t = fast.next; // 注意:不要将这段代码放在循环外执行，避免 fast == null 时，在循环内还执行 t = fast.next
            fast.next = slow;
            slow = fast;
            fast = t;
        }

        t = slow; // 最后slow指向链表末尾元素
        fast = head;
        boolean res = true;

        // 无论链表个数为奇偶，若其为回文，那么fast和slow会有先有一个指针为null
        while (fast != null && slow != null) {// fast从表头往中间移动，slow从表尾从中间移动
            if (fast.value != slow.value) {
                res = false;
                break;
            }
            fast = fast.next;
            slow = slow.next;
        }

        fast = t.next; // slow依旧为fast指针的上一个元素，此时slow在fast右边
        slow = t; // fast指向需要改变指向的元素
        t.next = null; // ！！！最后一个元素指针为null！！！

        // 必须恢复链表顺序
        while (fast != null) {
            t = fast.next;
            fast.next = slow;
            slow = fast;
            fast = t;
        }

        return res;
    }


    public static void main(String[] args) {
        Node head = null;
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");

        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(2);
        head.next.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(2);
        head.next.next.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");
    }
}
