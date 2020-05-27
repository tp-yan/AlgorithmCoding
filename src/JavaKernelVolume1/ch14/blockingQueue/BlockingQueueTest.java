package JavaKernelVolume1.ch14.blockingQueue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * 几种阻塞队列实现：基于链表实现的无限队列LinkedBlockingQueue、LinkedBlockingDeque 是一个双端版本；
 * ArrayBlockingQueue 在构造时需要指定容量，并且有一个可选的参数来指定是否需要公平性。
 * PriorityBlockingQueue 是一个带优先级的队列，而不是先进先出队列。元素按照它们的优先级顺序被移出。该队列是没有容量上限
 * <p>
 * 使用阻塞队列实现生产消费者问题：生产者线程向队列插入元素，消费者线程则取出它们。使用队列，可以安全地从一个线程向另一个线程传递数据。
 * 使用阻塞队列来控制一组线程（当队列存为满、取为空时就会阻塞线程）。程序在一个目录及它的所有子目录下搜索所有文件，打印出包含指定关键字的行。
 */
public class BlockingQueueTest {
    private static final int FILE_QUEUE_SIZE = 10;
    private static final int SEARCH_THREADS = 100;
    private static final File DUMMY = new File("");
    private static BlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);

    /* Hashtable的put和get方法都是 synchronized修饰的 */
    private static Hashtable<File, Integer> counters = new Hashtable<>();
    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

    public static void main(String[] args) throws InterruptedException {

        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Enter base directory (e.g. /opt/jdk1.8.0/src): ");
            String directory = in.nextLine();
            System.out.print("Enter keyword (e.g. volatile): ");
            String keyword = in.nextLine();

            final CountDownLatch latch = new CountDownLatch(SEARCH_THREADS + 1);

            // 1.生产者线程(1个)：负责put文件到队列
            Runnable enumerator = () -> {
                try {
                    // 递归地遍历文件夹，将文件放入队列中
                    enumerate(new File(directory));
                    queue.put(DUMMY);
                    latch.countDown(); // 让 latch 数值减一
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            new Thread(enumerator).start();


            // 2.消费者线程(多个):负责从队列take文件， 然后打开文件查找关键字
            for (int i = 1; i <= SEARCH_THREADS; i++) {
                Runnable searcher = () -> {
                    try {
                        /*使用一个小技巧在工作结束后终止这个应用程序（当所有线程终止后结束）。
                        为了发出完成信号，枚举线程放置一个虚拟对象到队列中（这就像在行李输送带上放一个写着“ 最后一个包” 的虚拟包。)
                        当搜索线程取到这个虚拟对象时，将其放回并终止。
                        */
                        boolean done = false;
                        while (!done) {
                            File file = queue.take();
                            if (file == DUMMY) {
                                queue.put(file); // 因为要保证所有线程都take到DUMMY后终止，程序才能终止。
                                done = true;
                            } else search(file, keyword);
                        }
                    } catch (InterruptedException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                };
                new Thread(searcher).start();
            }

            // 或者在主线中调用子线程的join方法： childThread.join()
            latch.await(); // 阻塞当前线程，直到latch为0
            int[] sum = new int[1];
            counters.forEach((k, v) -> {
                sum[0] += v;
            });
            System.out.println("all files number:" + sum[0]);
        }
    }

    /**
     * Recursively enumerates all files in a given directory and its subdirectories.
     *
     * @param directory
     */
    public static void enumerate(File directory) throws InterruptedException {
        File[] files = directory.listFiles();
        for (File f : files) {
            if (f.isDirectory()) enumerate(f);
            else queue.put(f);
        }
    }

    /**
     * Searches a file for a given keyword and prints all matching lines.
     *
     * @param file
     * @param keyword
     * @throws FileNotFoundException
     */
    public static void search(File file, String keyword) throws FileNotFoundException {
        try (Scanner in = new Scanner(file, "utf-8")) {
            int lineNumber = 0;
            while (in.hasNext()) {
                lineNumber++;
                String line = in.nextLine();
                if (line.contains(keyword)) {
                    // ％n：始终输出正确的跨平台特定的行分隔符，即换行符
                    System.out.printf("%s:%d:%s%n", file.getPath(), lineNumber, line);
                    counters.put(file, 1);
                }
            }
        }
    }
}
