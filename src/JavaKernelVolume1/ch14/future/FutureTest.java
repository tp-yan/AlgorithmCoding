package JavaKernelVolume1.ch14.future;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Callable 与Future
 * Runnable 封装一个异步运行的任务，可以把它想象成为一个没有参数和返回值的异步方法。Callable 与Runnable 类似，但是有返回值。
 * Callable 接口的参数化类型指示方法返回类型；
 * Future 保存异步计算的结果。可以启动一个计算，将Future 对象交给某个线程，Future 对象的所有者在结果计算好之后就可以获得它。
 * <p>
 * FutureTask 包装器是一种非常便利的机制，可将Callable 转换成Future 和Runnable, 它 同时实现二者的接口。
 */
public class FutureTest {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Enter base directory (e.g. /usr/local/jdk5.0/src): ");
            String directory = in.nextLine();
            System.out.print("Enter keyword (e.g. volatile): ");
            String keyword = in.nextLine();

            MatchCounter matchCounter = new MatchCounter(new File(directory), keyword);
            FutureTask<Integer> task = new FutureTask<>(matchCounter);
            new Thread(task).start();
            System.out.println(task.get() + " matching files.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

/**
 * This task counts the files in a directory and its subdirectories that contain a given keyword.
 */
class MatchCounter implements Callable<Integer> {
    private File directory;
    private String keyword;

    public MatchCounter(File directory, String keyword) {
        this.directory = directory;
        this.keyword = keyword;
    }

    @Override
    public Integer call() throws Exception {
        int count = 0;
        File[] files = directory.listFiles();
        List<Future<Integer>> results = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {// 对每个子目录进行相同操作：即开启子线程去递归遍历。
                MatchCounter matchCounter = new MatchCounter(file, keyword);
                FutureTask<Integer> task = new FutureTask<>(matchCounter);
                results.add(task);
                new Thread(task).start(); // task作为Runnable接口
            } else {
                if (search(file)) count++;
            }
        }
        // 统计完所有子线程计算结果后返回给父线程。
        for (Future<Integer> result : results) {
            count += result.get(); // task作为Future接口。统计子线程返回结果，若子线程还在运算则阻塞。
        }
        return count;
    }

    /**
     * Searches a file for a given keyword.
     *
     * @param file file the file to search
     * @return true if the keyword is contained in the file
     */
    public boolean search(File file) {
        try (Scanner in = new Scanner(file, "UTF-8")) {
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
