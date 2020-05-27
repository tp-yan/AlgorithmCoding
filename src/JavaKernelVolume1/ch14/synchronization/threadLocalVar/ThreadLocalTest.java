package JavaKernelVolume1.ch14.synchronization.threadLocalVar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ThreadLocal 辅助类为各个线程提供各自的实例。
 */
public class ThreadLocalTest {
    // 要为每个线程构造一个实例，可以使用以下代码
    public static final ThreadLocal<SimpleDateFormat> dateFormat =
            ThreadLocal.withInitial(()->new SimpleDateFormat("yyyy-MM-dd"));


    public static void main(String[] args) {
        // 在一个给定线程中首次调用get时，会调用initialValue方法(即withInitial指定的函数式接口)。在此之后，get 方法会返回属于当前线程的那个实例。
        String dateStamp = dateFormat.get().format(new Date());
        System.out.println(dateStamp);

        // 在多个线程中生成随机数也存在类似的问题。java..util.Rand0m 类是线程安全的。
        // 但是如果多个线程需要等待一个共享的随机数生成器，这会很低效。
        // Java SE 7 还另外提供了一个便利类。
        int random = ThreadLocalRandom.current().nextInt(100); // ThreadLocalRandom.current() 调用会返回特定于当前线程的Random 类实例。
        System.out.println("random:"+random);
    }
}
