package JianzhiOffer.Chapter02.DataStruct;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 如何仅用队列结构实现栈结构？
 * 如何仅用栈结构实现队列结构?
 */
public class Q09QueueWith2Stacks {
    /**
     * 使用队列实现栈：只能运用已有的队列结构，而不能修改原队列。
     * 需要2个队列实现栈结构。由于队列只能先进先出，栈只能先进后出，假设queue存放数据，当进行栈peek和pop操作时，可以将queue的前n-1个元素出队，
     * 再入队到help队列中，然后得到queue最后一个元素n，若是peek操作，则返回n后，再将n入队help，若是pop操作则不同，
     * 最后交换队列queue，help的引用，这样可以简化代码逻辑。
     */
    public static class TwoQueueStack {
        private Queue<Integer> queue; // 数据队列
        private Queue<Integer> help; // 辅助队列

        public TwoQueueStack() {
            queue = new LinkedList<>(); // LinkedList实现Deque<E>接口，所有可以当做双向队列使用
            help = new LinkedList<>();
        }

        public void push(int obj) {
            queue.add(obj);
        }

        public int pop() {
            if (queue.isEmpty()) {
                throw new RuntimeException("Stack is empty");
            }
            while (queue.size() != 1) {
                help.add(queue.poll());
            }
            int res = queue.poll();
            swap();
            return res;
        }

        public int peek() {
            if (queue.isEmpty()) {
                throw new RuntimeException("Stack is empty");
            }
            while (queue.size() != 1) {
                help.add(queue.poll());
            }
            int res = queue.poll();
            help.add(res);
            swap();
            return res;
        }

        private void swap() {
            Queue<Integer> tmp = help;
            help = queue;
            queue = tmp;
        }

    }

    /**
     * 2个已有栈实现队列
     * stackPush栈用于保存最近压栈而没有倒出的元素，用于入队操作
     * stackPop栈存放从stackPush栈倒出的元素，用于出队操作
     * stackPush倒出元素到stackPop需要满足2条原则：
     * 1)当stackPop还有元素时不能倒出
     * 2)stackPush倒出时需一次性全部倒出
     * 另外，可以在任何时刻慈从push导入poll栈，但必须遵守2条原则
     */
    public static class TwoStackQueue {
        private Stack<Integer> stackPush;
        private Stack<Integer> stackPop;

        public TwoStackQueue() {
            stackPop = new Stack<>();
            stackPush = new Stack<>();
        }

        public void push(int obj) {
            stackPush.push(obj);
        }

        public int poll() {
            // stackPop为空时，stackPush不一定为空，只有两者皆为空时队列才为空
            if (stackPop.empty() && stackPush.empty()) {
                throw new RuntimeException("Your queue is empty");
            }
            push2pop(); // 有可能 stackPush 有元素但没倒出则stackPop为空，故需 倒出一下
            return stackPop.pop();  // 栈顶元素即为队首元素
        }

        public int peek() {
            if (stackPop.empty() && stackPush.empty()) {
                throw new RuntimeException("Your queue is empty");
            }
            push2pop(); // 只有 stackPop 为空时才会倒出
            return stackPop.peek();
        }

        /**
         * 倒出 stackPush 元素到 stackPop，按照出队入队操作实现。
         * 需要满足2条原则：
         * * 1)当stackPop还有元素时不能倒出
         * * 2)stackPush倒出时需一次性全部倒出
         */
        public void push2pop() {
            if (stackPop.isEmpty()) {
                while (!stackPush.empty()) {
                    stackPop.push(stackPush.pop());
                }
            }
        }
    }

        public static void main(String[] args) {

        }
    }
