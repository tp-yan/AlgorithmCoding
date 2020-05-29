package JianzhiOffer.Chapter02.singleton;

/**
 * 实现单例模式：多线程安全版，实现稍微复杂，易出错。 -- 可行解法
 */
public class Singleton3 {
    private Singleton3() {
    }

    private static Singleton3 instance;

    /* 当实例存在时不需要请求锁，只会在第一次创建实例时会请求锁 */
    public static Singleton3 getInstance() {
        if (instance == null) {
            synchronized (Singleton3.class) { // 使用“类对象”作为锁
                // 为了避免线程进入此 if块但进入synchronized块之前被中断而存在创建多个实例的可能性，故下面还需用 if 判断
                if (instance == null)
                    instance = new Singleton3();
            }
        }
        return instance;
    }
}
