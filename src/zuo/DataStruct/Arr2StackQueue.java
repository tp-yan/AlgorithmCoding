package zuo.DataStruct;

public class Arr2StackQueue {
    /**
     * 由数组实现固定大小的栈
     */
    public class ArrayStack {
        private Integer[] arr;
        private Integer size;   // 当前元素个数，指向要填充元素的位置，即栈顶指针

        public ArrayStack(int initSize) {
            if (initSize < 0) {
                throw new IllegalArgumentException("The init size is less than 0");
            }
            this.arr = new Integer[initSize];
            this.size = 0;
        }

        public void push(int obj) {
            if (size < arr.length) {
                arr[size++] = obj;
            }
        }

        /**
         * 弹出并删除栈顶元素
         *
         * @return 若栈为空，则抛出异常
         */
        public Integer pop() {
            if (size == 0) {
                throw new ArrayIndexOutOfBoundsException("The stack is empty");
            }
            return arr[--size];
        }

        /**
         * 返回栈顶元素不删除
         *
         * @return 若栈为空，则返回null
         */
        public Integer peek() {
            if (size == 0) {
                return null;
            }
            return arr[size - 1];
        }
    }

    /**
     * 循环利用数组，但实现的并不是循环队列，队列满时不能再添加元素，即不能覆盖前面的元素，所以不是循环队列。
     */
    public static class ArrayQueue {
        private Integer[] arr;
        private Integer size; // 队列当前容量，可以解耦start和end指针，其它们互不影响
        private Integer start; // 队首指针
        private Integer end; // 队尾指针，指向要填入的位置

        public ArrayQueue(int initSize) {
            if (initSize < 0) {
                throw new IllegalArgumentException("The init size is less than 0");
            }
            this.arr = new Integer[initSize];
            this.size = 0;
            this.start = 0;
            this.end = 0;
        }

        public void push(int obj) {
            if (size >= arr.length) {
                throw new ArrayIndexOutOfBoundsException("The queue is full.");
            }
            arr[end] = obj;
            size++;
            end = end == arr.length - 1 ? 0 : end + 1; // 循环利用数组，当end指向队尾时，移到队首
        }

        /**
         * 移除并返回队首元素
         */
        public Integer poll() {
            if (size <= 0) {
                throw new ArrayIndexOutOfBoundsException("The queue is empty.");
            }
            size--;
            int tmp = start;
            start = start == arr.length - 1 ? 0 : start + 1; // 同end指针一样
            return arr[tmp];
        }

        public Integer peek() {
            if (size <= 0) {
                return null;
            }
            return arr[start];
        }

    }

    public static void main(String[] args) {

    }
}
