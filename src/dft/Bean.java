package dft;

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
}
