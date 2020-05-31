package zuo.DataStruct;


import java.util.Stack;

/**
 * 实现一个特殊的栈，在实现栈的基本功能的基础上，再实现返回栈中最小元素的操作。
 * 【要求】
 * 1．pop、push、getMin操作的时间复杂度都是O(1)。
 * 2．设计的栈类型可以使用现成的栈结构。
 * 求解：不能使用一个额外变量记录最小值，因为当栈中最小值被删除后，剩余元素中的最小值就不知道了。
 */
public class GetMinStack {
    /**
     * 实现一：
     * 准备2个栈结构，一个栈A用于正常存，另一个栈B存 当A中每个元素作为栈顶元素时，此时栈A中的最小值。
     * A，B 的大小是同步的且是一一对应的
     */
    public static class MinStack1 {
        private Stack<Integer> stackData;
        private Stack<Integer> stackMin;

        public MinStack1() {
            this.stackData = new Stack<>();
            this.stackMin = new Stack<>();
        }

        public void push(int obj) {
            if (stackMin.empty()) {
                stackMin.push(obj);
            } else if (obj < this.getMin()) { // 若新push的元素比之前栈的最小值还小，则此时栈最小值为它，同时stackMin记录这个
                // 栈情况的最小值
                stackMin.push(obj);
            } else {
                stackMin.push(stackMin.peek()); // 否则当前栈情况的最小值还是之前的最小值
            }
            stackData.push(obj);
        }

        public int pop() {
            if (stackData.empty()) {
                throw new RuntimeException("Your stack is empty");
            }
            // 2个栈同时pop，必须保持2个栈同步
            stackMin.pop();
            return stackData.pop();
        }

        /**
         * 返回stackMin堆顶元素
         *
         * @return
         */
        public int getMin() {
            if (stackMin.empty()) {
                throw new RuntimeException("Your stack is empty");
            }
            return stackMin.peek();
        }

        public int peek() {
            if (stackData.empty()) {
                throw new RuntimeException("Your stack is empty");
            }
            return stackData.peek();
        }
    }

    /**
     * 实现二：
     * A，B栈不同步，B中只记录当A中新加元素 <= B中栈顶元素时，即A中栈顶元素 <= 之前A中最小元素时。
     * 此方法比一稍微节约点空间
     */
    public static class MinStack2 {
        private Stack<Integer> stackData;
        private Stack<Integer> stackMin;

        public MinStack2() {
            this.stackData = new Stack<>();
            this.stackMin = new Stack<>();
        }

        public void push(int obj) {
            if (stackMin.empty()) {
                stackMin.push(obj);
            } else if (obj <= stackMin.peek()) { // 取相等是为了删除方便，对于相同最小值，有多少个就压多少个，删除时就不用
                // 考虑是否还有相同最小值
                stackMin.push(obj);
            }
            stackData.push(obj);
        }

        public int pop() {
            if (stackData.empty()) {
                throw new RuntimeException("Your stack is empty");
            }
            int value = stackData.pop();
            if (value == getMin()) {// 在什么条件下push的就在在什么条件pop
                stackMin.pop();
            }
            return value;
        }

        public int peek() {
            if (stackData.empty()) {
                throw new RuntimeException("Your stack is empty");
            }
            return stackData.peek();
        }

        public int getMin() {
            if (stackMin.empty()) {
                throw new RuntimeException("Your stack is empty");
            }
            return stackMin.peek();
        }
    }

    public static void main(String[] args) {
        MinStack1 stack1 = new MinStack1();
        stack1.push(3);
        System.out.println(stack1.getMin());
        stack1.push(4);
        System.out.println(stack1.getMin());
        stack1.push(1);
        System.out.println(stack1.getMin());
        System.out.println(stack1.pop());
        System.out.println(stack1.getMin());

        System.out.println("=============");

        MinStack2 stack2 = new MinStack2();
        stack2.push(3);
        System.out.println(stack2.getMin());
        stack2.push(4);
        System.out.println(stack2.getMin());
        stack2.push(1);
        System.out.println(stack2.getMin());
        System.out.println(stack2.pop());
        System.out.println(stack2.getMin());
    }
}
