package data_strcuct;

// 回文：正反序字符串相同，即从中间对称的

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
    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    public static boolean isPalindrome1(Node head) {
        return true;
    }

    public static boolean isPalindrome2(Node head) {
        return true;
    }

    public static boolean isPalindrome3(Node head) {
        return true;
    }


    public static void main(String[] args) {

    }
}
