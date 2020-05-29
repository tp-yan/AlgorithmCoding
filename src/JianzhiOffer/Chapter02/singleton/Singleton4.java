package JianzhiOffer.Chapter02.singleton;

/**
 * 实现单例模式：多线程安全版，使用静态域并在类加载时就初始化，但是会使得内存使用效率下降。-- 推荐
 */
public class Singleton4 {
    private Singleton4() {
    }

    /* 当类加载时就会初始化类的静态域，只是比需要使用实例之前提前创建了，如果创建实例的内存消耗较大，而又没立即使用实例，
    * 则导会致内存效率低下
    * 或者在 静态块中也可以
    * static{
    *   instance = new Singleton4();
    * }
    * */
    private static Singleton4 instance = new Singleton4();


    public static Singleton4 getInstance() {
        return instance;
    }
}
