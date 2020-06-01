package zuo;

import zuo.HandWriteCode02.PalindromeList.Node;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class HandWriteCode02 {
    // 1.使用固定大小数组实现栈和队列
    public static class ArrayStack {
        private int[] arr;
        private int size; // 栈的大小，同时也是指向栈顶的指针

        public ArrayStack(int initSize) {
            if (initSize < 0) throw new IllegalArgumentException("size must >= 0");
            arr = new int[initSize];
            this.size = 0;
        }

        public void push(int value) {
            if (size == arr.length) throw new ArrayIndexOutOfBoundsException("stack is full.");
            arr[size++] = value;
        }

        public int peek() {
            if (size == 0) throw new ArrayIndexOutOfBoundsException("stack is empty.");
            return arr[size - 1];
        }

        public int poll() {
            if (size == 0) throw new ArrayIndexOutOfBoundsException("stack is empty.");
            return arr[--size];
        }
    }

    /**
     * 循环利用固定大小的数组，但实现的并不是循环队列，队列满时不能再添加元素，即不能覆盖前面的元素，所以不是循环队列。
     */
    public static class ArrayQueue {
        private int[] arr;
        private int size; // 指示队列大小，同时将 start和end指针解耦合，指针之间毫无联系，指针移动只看size即可
        private int start; // 指向队首，即获取首元素的位置
        private int end; // 指向队尾，即新插入元素的位置

        public ArrayQueue(int initSize) {
            if (initSize < 0) throw new IllegalArgumentException("size must >= 0.");
            this.size = 0;
            start = end = 0; // 初始都指向0位置
        }

        public void push(int value) {
            if (size == arr.length) throw new ArrayIndexOutOfBoundsException("Queue is full.");
            size++;
            arr[end] = value;
            end = end == arr.length - 1 ? 0 : end + 1; // end是否已指向数组末尾
        }

        public int peek() {
            if (size == 0) throw new ArrayIndexOutOfBoundsException("Queue is empty.");
            return arr[start];
        }

        public int poll() {
            if (size == 0) throw new ArrayIndexOutOfBoundsException("Queue is empty.");
            size--;
            int tmp = arr[start];
            start = start == arr.length - 1 ? 0 : start + 1;
            return tmp;
        }
    }

    // 2.特殊栈：实现栈的基本功能外，还要求在O(1)时间内返回栈内最小值
    /* 实现1：使用2个栈：一个存数据(数据栈)，另一个存当前栈内最小值(min栈)。这2个栈同步压栈、出栈。
       当数据栈压入新值时，若比min栈栈顶元素小，则同时压入min栈，否则将min栈顶元素再次压入min栈。*/
    public static class MinStack {
        private Stack<Integer> stackData;
        private Stack<Integer> stackMin;

        public MinStack() {
            stackData = new Stack<>();
            stackMin = new Stack<>();
        }

        public void push(int value) {
            if (stackMin.empty())
                stackMin.push(value);
            else if (value < getMin()) stackMin.push(value);
            else stackMin.push(stackMin.peek());
            stackData.push(value);
        }

        public int peek() {
            if (stackMin.empty())
                throw new RuntimeException("stack is empty.");
            return stackData.peek();
        }

        public int poll() {
            if (stackMin.empty())
                throw new RuntimeException("stack is empty.");
            stackMin.pop();
            return stackData.pop();
        }

        public int getMin() {
            if (stackMin.empty())
                throw new RuntimeException("stack is empty.");
            return stackMin.peek();
        }
    }

    /* 实现2(判断比实现1复杂，但节约点空间)：使用2个栈：一个存数据，另一个同步存当前栈内最小值。
    只有当数据栈中压入 <= min栈顶元素时才将其压入min栈，否则min栈不压入。  */
    public static class MinStack2 {
        private Stack<Integer> stackData;
        private Stack<Integer> stackMin;

        public MinStack2() {
            stackData = new Stack<>();
            stackMin = new Stack<>();
        }

        public void push(int value) {
            if (stackMin.empty()) {
                stackMin.push(value);
            } else if (value <= getMin()) stackMin.push(value);
            stackData.push(value);
        }

        public int peek() {
            if (stackMin.empty())
                throw new RuntimeException("stack is empty.");
            return stackData.peek();
        }

        public int poll() {
            if (stackMin.empty())
                throw new RuntimeException("stack is empty.");
            if (stackMin.peek().equals(stackData.peek()))
                stackMin.pop();
            stackMin.pop();
            return stackData.pop();
        }

        public int getMin() {
            if (stackMin.empty())
                throw new RuntimeException("stack is empty.");
            return stackMin.peek();
        }
    }

    // 3.栈和队列相互实现
    /* 需要2个队列实现栈结构。由于队列只能先进先出，栈只能先进后出，假设queue存放数据，当进行栈peek和pop操作时，可以将queue的前n-1个元素出队，
     * 再入队到help队列中，然后得到queue最后一个元素n，若是peek操作，则返回n后，再将n入队help，若是pop操作则不同，
     * 最后交换队列queue，help的引用，这样可以简化代码逻辑。
     */
    public static class QueueStack {
        private Queue<Integer> data;
        private Queue<Integer> help;

        public QueueStack() {
            data = new LinkedList<>();  // LinkedList实现Deque<E>接口，所有可以当做双向队列使用
            help = new LinkedList<>();
        }

        public void push(int value) {
            data.add(value);
        }

        public int peek() {
            if (data.isEmpty())
                throw new RuntimeException("stack is empty.");
            // 导出前n-1个元素
            while (data.size() != 1) {
                help.add(data.poll());
            }
            int res = data.poll();
            help.add(res);
            swap();
            return res;
        }

        public int poll() {
            if (data.isEmpty())
                throw new RuntimeException("stack is empty.");
            while (data.size() > 1)
                help.add(data.poll());
            int res = data.poll();
            swap();
            return res;
        }

        // 交换数据后，同时交换引用，让data始终指向数据队列，help只是过渡工具
        private void swap() {
            Queue<Integer> tmp = data;
            data = help;
            help = data;
        }
    }

    /*
    使用栈实现队列：
    使用2个栈：poll栈和push栈。元素只能从poll栈弹出、压入push栈。同时必须遵守2条原则：
    1. 当poll栈有元素时不能从push导入元素
    2. push栈导出元素时，必须一次性全部导完
    另外，可以在任何时刻慈从push导入poll栈，但必须遵守2条原则
     */
    public static class StackQueue {
        private Stack<Integer> pollStack;
        private Stack<Integer> pushStack;

        public StackQueue() {
            pollStack = new Stack<>();
            pushStack = new Stack<>();
        }

        public void push(int value) {
            pushStack.push(value);
            dump();
        }

        public int peek() {
            if (pollStack.empty() && pushStack.empty())
                throw new RuntimeException("queue is empty.");
            dump();
            return pollStack.peek();
        }

        public int poll() {
            if (pollStack.empty() && pushStack.empty())
                throw new RuntimeException("queue is empty.");
            dump();
            return pollStack.pop();
        }

        // 将元素一次性从push导到poll
        public void dump() {
            // 原则一：导入时poll不能有元素
            if (pollStack.isEmpty()) {
                // 原则二：push栈一次性导出
                while (!pushStack.empty()) {
                    pollStack.push(pushStack.pop());
                }
            }
        }
    }

    // 4.实现猫狗队列
    /*
     * 实现一种狗猫队列的结构，要求如下：
     * 用户可以调用add方法将cat类或dog类的实例放入队列中；
     * 用户可以调用pollAll方法，将队列中所有的实例按照进队列的先后顺序依次弹出；
     * 用户可以调用pollDog方法，将队列中dog类的实例按照进队列的先后顺序依次弹出；
     * 用户可以调用pollCat方法，将队列中cat类的实例按照进队列的先后顺序依次弹出；
     * 用户可以调用isEmpty方法，检查队列中是否还有dog或cat的实例；
     * 用户可以调用isDogEmpty方法，检查队列中是否有dog类的实例；
     * 用户可以调用isCatEmpty方法，检查队列中是否有cat类的实例。
     *
     * 解：将dog和cat分别依次放在dog queue 和cat queue，概念上这2个队列形成一个队列PetQueue，将每个pet打上一个时间戳rank代表其加入
     * PetQueue的先后顺序，当要让PetQueue队首元素出列时，将dog queue 和cat queue中队首元素rank最小的出队即可
     */
    // 源码部分不能修改
    public static class Pet {
        private String type;

        public Pet(String type) {
            this.type = type;
        }

        public String getPetType() {
            return this.type;
        }

        @Override
        public String toString() {
            return getClass().getName() + "{" +
                    "type='" + type + '\'' +
                    '}';
        }
    }

    public static class Dog extends Pet {
        public Dog() {
            super("dog");
        }
    }

    public static class Cat extends Pet {
        public Cat() {
            super("cat");
        }
    }
    // 源码部分不能修改

    // 为入队的Pet封装时间戳(实际是入队的先后顺序)
    public static class PetQueueItem {
        private Pet pet;
        private long index;

        public PetQueueItem(Pet pet, long index) {
            this.pet = pet;
            this.index = index;
        }

        public Pet getPet() {
            return pet;
        }

        public long getIndex() {
            return index;
        }

        public String getType() {
            return pet.getPetType();
        }
    }

    // PetQueue维护CatQueue和DogQueue
    public static class PetQueue {
        private Queue<PetQueueItem> catQueue;
        private Queue<PetQueueItem> dogQueue;
        private long index = 0; // 时间戳

        public PetQueue() {
            catQueue = new LinkedList<>();
            dogQueue = new LinkedList<>();
        }

        public void add(Pet pet) {
            if (pet.getPetType().toLowerCase().equals("cat")) {
                catQueue.add(new PetQueueItem(pet, index++));
            } else if (pet.getPetType().toLowerCase().equals("dog")) {
                dogQueue.add(new PetQueueItem(pet, index++));
            } else throw new RuntimeException("Error pet type.");
        }

        // 从“整体的队列”中按顺序弹出队首Pet
        public Pet pollAll() {
            if (isCatEmpty() && isDogEmpty()) throw new RuntimeException("Pet queue is empty.");
            // 至少一个队列不空
            if (isCatEmpty()) {
                return dogQueue.poll().getPet();
            } else if (isDogEmpty()) {
                return catQueue.poll().getPet();
            } else {// 同不空
                assert catQueue.peek() != null;
                if (catQueue.peek().getIndex() < dogQueue.peek().getIndex()) return catQueue.poll().getPet();
                else return dogQueue.poll().getPet();
            }
        }

        public Cat pollCat() {
            if (catQueue.isEmpty())
                throw new RuntimeException("Cat queue is empty.");
            return (Cat) catQueue.poll().getPet();
        }

        public Dog pollDog() {
            if (dogQueue.isEmpty())
                throw new RuntimeException("Dog queue is empty.");
            return (Dog) dogQueue.poll().getPet();
        }

        public boolean isEmpty() {
            return isCatEmpty() && isDogEmpty();
        }

        public boolean isCatEmpty() {
            return catQueue.isEmpty();
        }

        public boolean isDogEmpty() {
            return dogQueue.isEmpty();
        }
    }

    // 5.设计RandomPool
    /*
     * 设计RandomPool结构
     * 【题目】
     * 设计一种结构，在该结构中有如下三个功能：
     * insert(key)：将某个key加入到该结构，做到不重复加入。
     * delete(key)：将原本在结构中的某个key移除。
     * getRandom()：等概率随机返回结构中的任何一个key。
     * 【要求】
     * Insert、delete和getRandom方法的时间复杂度都是O(1)。
     * <p>
     * 解：使用2个HashMap实现，一个存HashMap<Key,Integer> A，另一个存HashMap<Integer，Key> B。
     * 使用一个计算器 counter=0 ，当将key插入A时，同时counter作为value插入，而B同步插入 <counter,key>，然后counter++。
     * 要做到随机返回key，只需要保证key的counter是连续一段整数，那么直接取这段整数的随机值，从B中取出Key即可。
     * 但若中间删除了某个key，那么可能导致所有key的counter就不是连续整数段。
     * 若要删除的是中间的key，就把最后一个<K,counter>改成被删key的counter值即可，A、B同步修改。
     *
     * 因为要求getRandom为O(1)，所以不能生成随机数再去遍历 HaspMap.keySet()！！
     */
    public static class RandomPool {
        private HashMap<String, Integer> mapA;
        private HashMap<Integer, String> mapB;
        private int counter; // 作为key的id，同时指示key的个数

        public RandomPool() {
            mapA = new HashMap<>();
            mapB = new HashMap<>();
            counter = 0;
        }

        public void insert(String key) {
            if (!mapA.containsKey(key)) {// 必须防止重复插入覆盖id，保证id从0开始连续
                mapA.put(key, counter);
                mapB.put(counter++, key);
            }
        }

        public void delete(String key) {
            if (!mapA.containsKey(key))
                throw new RuntimeException("there is no key:" + key);
            int value = mapA.get(key); // 被删元素的id
            if (value == (counter - 1)) {// 被删元素的id是最后一个id则直接删除即可
                mapA.remove(key);
                mapB.remove(value);
            } else {// 删除中间元素
                String lastKey = mapB.get(counter - 1);
                mapA.remove(key);
                mapA.put(lastKey, value);
                mapB.put(value, lastKey);
                mapB.remove(counter - 1);
            }
            counter--;
        }

        public String getRandom() {
            if (counter == 0)
                return null;
            // 连续整数段：0~counter-1
            int random = (int) (Math.random() * counter);
            return mapB.get(random);
        }
    }

    // 6.转圈打印矩阵，详见：zuo.DataStruct.PrintMatrixSpiralOrder
    public static class PrintMatrixSpiralOrder {
        public static void printMatrix(int[][] matrix) {
            if (matrix == null) return;
            // A：矩阵左上角，B：矩阵右下角点
            int rowA = 0;
            int colA = 0;
            int rowB = matrix.length - 1;
            int colB = matrix[0].length - 1;

            // 每次打印A,B矩阵的外围，然后A右下移，B左上移，指向下一个矩阵
            while (rowA <= rowB && colA <= colB) {
                printEdge(matrix, rowA++, colA++, rowB--, colB--);
            }
        }

        public static void printEdge(int[][] matrix, int rowA, int colA, int rowB, int colB) {
            // 特殊矩阵
            if (rowA == rowB) {
                while (colA <= colB)
                    System.out.print(matrix[rowA][colA++] + " ");
            } else if (colA == colB) {
                while (rowA <= rowB)
                    System.out.print(matrix[rowA++][colA] + " ");
            } else {
                int curRow = rowA;
                int curCol = colA;
                while (curCol < colB)  // → 右移,末尾元素不打印
                    System.out.print(matrix[curRow][curCol++] + " ");
                while (curRow < rowB) // ↓下移,末尾元素不打印
                    System.out.print(matrix[curRow++][curCol] + " ");
                while (colA < curCol) // ← 左移,末尾元素不打印
                    System.out.print(matrix[curRow][curCol--] + " ");
                while (rowA < curRow) // ↑ 上移
                    System.out.print(matrix[curRow--][curCol] + " ");
            }
        }
    }

    // 7.“之”字型打印矩阵，详见 zuo.DataStruct.ZigZagPrintMatrix
    public static class PrintZigZagMatrix {
        public static void printMatrix(int[][] matrix) {
            if (matrix == null || matrix.length < 1) return;
            // 初始都指向矩阵左上角位置
            int rowA = 0;
            int colA = 0;
            int rowB = 0;
            int colB = 0;
            int endRow = matrix.length - 1;
            int endCol = matrix[0].length - 1;
            boolean fromUp = false; // 是否斜下方向

            while (rowA <= endRow && colB <= endCol) {// 因为AB点是同步移动，且开始都在左上角，最后结束时一定都在矩阵右下角。
                printDialog(matrix, rowA, colA, rowB, colB, fromUp);
                // A点先右移到边界再下移
                rowA = colA == endCol ? rowA + 1 : rowA;
                colA = colA == endCol ? colA : colA + 1;
                // B点先下移到边界再右移
                // 千万要注意：因为这是以 rowB作为判断条件，一定要在后面才修改rowB，否则colB无法得到正确值！！！
                colB = rowB == endRow ? colB + 1 : colB;
                rowB = rowB == endRow ? rowB : rowB + 1;

                fromUp = !fromUp;
            }
        }

        // 打印 (rowA,colA)和(rowB,colB)所指示的对角线元素
        private static void printDialog(int[][] matrix, int rowA, int colA, int rowB, int colB, boolean fromUp) {
            if (fromUp) {// 斜下
                while (rowA <= rowB) {
                    System.out.print(matrix[rowA++][colA--] + " ");
                }
            } else { // 斜上
                while (colB <= colA)
                    System.out.print(matrix[rowB--][colB++] + " ");
            }
            System.out.println();
        }
    }

    // 8.判断一个链表是否为回文结构，详见zuo.DataStruct.IsPalindromeList
    // 不限空间，可以使用栈结构，限空间O(1)，只能先修改链表结构再还原
    public static class PalindromeList {
        static class Node {
            int value;
            Node next;

            public Node(int value) {
                this.value = value;
            }
        }

        // (1)快慢指针 + 修改链表
        public static boolean isPalindromeList1(Node head) {
            if (head == null) return false;
            if (head.next == null) return true;
            Node fast = head;
            Node slow = head;
            // 找到链表中间位置
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            // 后段链表逆序
            Node cur = slow.next;
            Node pre = slow;
            slow.next = null;
            while (cur != null) {
                slow = cur.next; // slow充当临时指针
                cur.next = pre;
                pre = cur;
                cur = slow;
            }// 最后 cur == null，pre指向末尾元素
            slow = pre; // 记住末尾元素，用于后续还原链表

            // 两端对比
            boolean res = true;
            cur = head;
            while (cur != null && pre != null) {
                if (cur.value != pre.value) {
                    res = false;
                    break;
                }
                cur = cur.next;
                pre = pre.next;
            }

            // 还原链表
            cur = slow;
            pre = slow.next;
            cur.next = null;
            while (pre != null) {
                slow = pre.next;
                pre.next = cur;
                cur = pre;
                pre = slow;
            }
            return res;
        }

        // (2)使用栈
        public static boolean isPalindromeList2(Node head) {
            if (head == null) return false;
            if (head.next == null) return true;

            Node cur = head;
            Stack<Integer> stack = new Stack<>();
            while (cur != null) {
                stack.push(cur.value);
                cur = cur.next;
            }
            while (head != null) {
                if (head.value != stack.pop())
                    return false;
                head = head.next;
            }
            return true;
        }

    }


    public static void main(String[] args) {
        testPalindromeList();
    }

    public static void testCatDogQueue() {
        PetQueue test = new PetQueue();
        Pet dog1 = new Dog();
        Pet dog2 = new Dog();
        Pet dog3 = new Dog();
        Pet cat1 = new Cat();
        Pet cat2 = new Cat();
        Pet cat3 = new Cat();

        test.add(dog1);
        test.add(cat1);

        test.add(dog2);
        test.add(cat2);

        test.add(dog3);
        test.add(cat3);

        test.add(dog1);
        test.add(cat1);
        test.add(dog2);
        test.add(cat2);
        test.add(dog3);
        test.add(cat3);

        test.add(dog1);
        test.add(cat1);
        test.add(dog2);
        test.add(cat2);
        test.add(dog3);
        test.add(cat3);

        System.out.println(test.pollAll());
        System.out.println(test.pollAll());

        while (!test.isDogEmpty()) {
            System.out.println(test.pollDog().getPetType());
        }
        while (!test.isEmpty()) {
            System.out.println(test.pollAll().getPetType());
        }
    }
    public static void testRandomPool() {
        RandomPool pool = new RandomPool();
        pool.insert("zhang");
        pool.insert("san");
        pool.insert("wu");
        pool.insert("li");
        pool.insert("zhao");
        pool.insert("wang");
        pool.insert("xu");
        pool.insert("chen");
        pool.insert("liu");
        pool.insert("tang");
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
    }
    public static void testPrintZigZagMatrix() {
        int[][] matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        PrintZigZagMatrix.printMatrix(matrix);
    }

    public static void testPalindromeList() {
        PalindromeList.Node head = null;
        printLinkedList(head);
        System.out.print(PalindromeList.isPalindromeList1(head) + " | ");
        System.out.println(PalindromeList.isPalindromeList2(head) + " | ");

        printLinkedList(head);
        System.out.println("=========================");

        head = new PalindromeList.Node(1);
        printLinkedList(head);
        System.out.print(PalindromeList.isPalindromeList1(head) + " | ");
        System.out.println(PalindromeList.isPalindromeList2(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new PalindromeList.Node(1);
        head.next = new PalindromeList.Node(2);
        printLinkedList(head);
        System.out.print(PalindromeList.isPalindromeList1(head) + " | ");
        System.out.println(PalindromeList.isPalindromeList2(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new PalindromeList.Node(1);
        head.next = new PalindromeList.Node(1);
        printLinkedList(head);
        System.out.print(PalindromeList.isPalindromeList1(head) + " | ");
        System.out.println(PalindromeList.isPalindromeList2(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new PalindromeList.Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        printLinkedList(head);
        System.out.print(PalindromeList.isPalindromeList1(head) + " | ");
        System.out.println(PalindromeList.isPalindromeList2(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(PalindromeList.isPalindromeList1(head) + " | ");
        System.out.println(PalindromeList.isPalindromeList2(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(PalindromeList.isPalindromeList1(head) + " | ");
        System.out.println(PalindromeList.isPalindromeList2(head) + "" +
                " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(2);
        head.next.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(PalindromeList.isPalindromeList1(head) + " | ");
        System.out.println(PalindromeList.isPalindromeList2(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(2);
        head.next.next.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(PalindromeList.isPalindromeList1(head) + " | ");
        System.out.println(PalindromeList.isPalindromeList2(head) + " | ");
        printLinkedList(head);
        System.out.println("=========================");
    }

    public static void printLinkedList(Node head) {
        System.out.print("Linked List: ");
        while (head != null) {
            System.out.print(head.value + " ");
            head = head.next;
        }
        System.out.println();
    }
}
