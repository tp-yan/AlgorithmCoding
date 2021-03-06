package zuo.dft;

/**
 * 存放需复用的类定义
 */
public class Bean {
    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    // 双向节点
    public static class DoubleNode {
        public int value;
        public DoubleNode next;
        public DoubleNode last;

        public DoubleNode(int value) {
            this.value = value;
        }
    }

    // 带随机指针的节点
    public static class NodeWithRandom {
        public int value;
        public NodeWithRandom next;
        public NodeWithRandom rand;

        public NodeWithRandom(int data) {
            this.value = data;
        }
    }
}
