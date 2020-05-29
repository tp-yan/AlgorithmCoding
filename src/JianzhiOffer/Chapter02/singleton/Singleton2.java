package JianzhiOffer.Chapter02.singleton;

/**
 * 实现单例模式：多线程安全版，但效率低。 -- 可行但不推荐
 */
public class Singleton2 {
    private Singleton2(){}

    private static Singleton2 instance;

    /* 因为使用 synchronized 锁住了整个方法，所以读取实例也需要去请求锁，而锁的开销很大！
    * 其实只有在创建实例的那一刻才需要锁保护！ 改进在 Singleton3.java
    *  */
    public static synchronized Singleton2 getInstance() {
        if (instance == null)
            instance = new Singleton2();
        return instance;
    }
}
