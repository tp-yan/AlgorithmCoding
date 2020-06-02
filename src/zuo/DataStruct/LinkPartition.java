package zuo.DataStruct;

import zuo.MyUtils;
import zuo.dft.Bean;
import zuo.dft.Bean.*;

import static zuo.MyUtils.printLinkedList;

/**
 * 将单向链表按某值划分成左边小、中间相等、右边大的形式
 * 【题目】
 * 给定一个单向链表的头节点head，节点的值类型是整型，再给定一个整
 * 数pivot。实现一个调整链表的函数，将链表调整为左部分都是值小于
 * pivot的节点，中间部分都是值等于pivot的节点，右部分都是值大于
 * pivot的节点。除这个要求外，对调整后的节点顺序没有更多的要求。
 * 例如：链表9->0->4->5->1，pivot=3。
 * 调整后链表可以是1->0->4->9->5，也可以是0->1->9->5->4。总之，满
 * 足左部分都是小于3的节点，中间部分都是等于3的节点（本例中这个部
 * 分为空），右部分都是大于3的节点即可。对某部分内部的节点顺序不做
 * 要求。
 *
 * 进阶：
 * 在原问题的要求之上再增加如下两个要求。
 * 在左、中、右三个部分的内部也做顺序要求，要求每部分里的节点从左
 * 到右的顺序与原链表中节点的先后次序一致。
 * 例如：链表9->0->4->5->1，pivot=3。调整后的链表是0->1->9->4->5。
 * 在满足原问题要求的同时，左部分节点从左到右为0、1。在原链表中也
 * 是先出现0，后出现1；中间部分在本例中为空，不再讨论；右部分节点
 * 从左到右为9、4、5。在原链表中也是先出现9，然后出现4，最后出现5。
 * 如果链表长度为N，时间复杂度请达到O(N)，额外空间复杂度请达到O(1)。
 *
 * <p>
 * 解1：
 * 使用辅助空间，将所有节点放在一个数组中，然后根据其 value 进行数组的划分过程，额外空间O(n)
 * <p>
 * 解2：(进阶）
 * 要求额外空间O(1)
 * 1. 遍历一次链表，分别找出小于、等于、大于 pivot 的第一个节点，创建指向他们的指针
 * 2. 再遍历一次链表，将链表中小于、等于、大于 pivot 的节点分别挂在三个新链表的后面（跳过链表头结点）
 * 3. 将小于，等于，大于 的3个链表依次相连，则得到 < == > 的三部分
 */
public class LinkPartition {
    // 解法1：借助数组完成划分
    public static Bean.Node listPartition1(Node head, int pivot) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        int num = 0;    // 统计节点个数
        while (cur != null) {
            num++;
            cur = cur.next;
        }

        // 拷贝节点数组
        Node[] nodeArr = new Node[num];
        cur = head;
        for (int i = 0; i < nodeArr.length; i++) {
            nodeArr[i] = cur;
            cur = cur.next;
        }

        // 数组划分
        arrPartition(nodeArr, pivot);
        // 还原链表
        for (int i = 1; i < nodeArr.length; i++) {// 防止 i+1 越界，故从后一节点往前看
            nodeArr[i - 1].next = nodeArr[i];
        }
        nodeArr[nodeArr.length - 1].next = null;
        return nodeArr[0];  // 新头结点
    }

    public static void arrPartition(Node[] nodeArr, int pivot) {
        int small = -1;
        int more = nodeArr.length;
        int index = 0;

        while (index != more) { // 遍历指针遇到 大于区域的左边界时停止
            if (nodeArr[index].value < pivot) {
                MyUtils.swap_node(nodeArr, ++small, index++); // 小于区域右扩一位，再与当前元素交换
            } else if (nodeArr[index].value > pivot) {
                MyUtils.swap_node(nodeArr, --more, index); // 这里 index不能 +1
            } else { // 等于区域
                index++;
            }
        }
    }

    // 解法2：额外空间为O(1)
    public static Node listPartition2(Node head, int pivot) {
        Node smallH = null; // 小于区域的首尾节点指针
        Node smallT = null;
        Node equalH = null;
        Node equalT = null;
        Node moreH = null;
        Node moreT = null;

        Node next;

        while (head != null) {
            next = head.next; // 每次把头结点取出来
            head.next = null;

            if (head.value < pivot) {
                if (smallH == null) {// 链表还没有元素，则该节点为头结点
                    smallH = smallT = head;
                } else {// 链表有元素，则将节点连接到末尾
                    smallT.next = head;
                    smallT = head;
                }
            } else if (head.value == pivot) {
                if (equalH == null) {
                    equalH = equalT = head;
                } else {
                    equalT.next = head;
                    equalT = head;
                }
            } else {
                if (moreH == null) {
                    moreH = moreT = head;
                } else {
                    moreT.next = head;
                    moreT = head;
                }
            }
            head = next;    // 指向剩下的链表
        }

        // 连接 小于和等于区域
        if (smallT != null) {
            smallT.next = equalH;
            // 若等于区域为空，则连接后，链表尾巴为 小于区域的尾部节点
            equalT = equalT == null ? smallT : equalT;
        }

        // 前面的链表不为空的话，连接大于区域链表
        if (equalT != null) {
            equalT.next = moreH;
        }

        return smallH != null ? smallH : equalH != null ? equalH : moreH;  // 也许不存在小于或等于区域
    }

    public static void main(String[] args) {
        Node head1 = new Node(7);
        head1.next = new Node(9);
        head1.next.next = new Node(1);
        head1.next.next.next = new Node(8);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(2);
        head1.next.next.next.next.next.next = new Node(5);
        printLinkedList(head1);
//         head1 = listPartition1(head1, 4);
        head1 = listPartition2(head1, 5);
        printLinkedList(head1);
    }
}
