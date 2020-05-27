package JavaKernelVolume1.ch14.executor.threadPool;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class ThreadPoolTest {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Enter base directory (e.g. /usr/local /jdk5.0/src): ");
            String directory = in.nextLine();
            System.out.print("Enter keyword (e.g. volatile): ");
            String keyword = in.nextLine();

            // 1.创建线程池
            ExecutorService pool = Executors.newCachedThreadPool();
            // 2.创建Runnable或Callable对象
            MatchCounter matchCounter = new MatchCounter(new File(directory), keyword, pool);
            // 3.submit Runnable或Callable对象，并返回 Future 对象用于控制线程和获取计算结果
            Future<Integer> result = pool.submit(matchCounter);
            try {
                System.out.println(result.get() + " matching files.");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            // 4. 没有新任务添加了，就shutdown，等所有线程完成任务后终止
            pool.shutdown();
            // 获取线程池的信息
            int largePoolSize = ((ThreadPoolExecutor) pool).getLargestPoolSize();
            System.out.println("largest pool size=" + largePoolSize);
        }
    }
}

class MatchCounter implements Callable<Integer> {
    private File directory;
    private String keyword;
    private ExecutorService pool; // 线程池
    private int count;

    public MatchCounter(File directory, String keyword, ExecutorService pool) {
        this.directory = directory;
        this.keyword = keyword;
        this.pool = pool;
    }

    @Override
    public Integer call() throws Exception {
        count = 0;
        File[] files = directory.listFiles();
        List<Future<Integer>> results = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory()) {
                MatchCounter matchCounter = new MatchCounter(file, keyword, pool);
                Future<Integer> result = pool.submit(matchCounter);
                results.add(result);
            } else if (search(file)) count++;
        }

        for (Future<Integer> result : results) {
            count += result.get();
        }

        return count;
    }

    public boolean search(File file) {
        try (Scanner in = new Scanner(file, "utf-8")) {
            boolean found = false;
            while (!found && in.hasNextLine()) {
                if (in.nextLine().contains(keyword)) found = true;
            }
            return found;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
