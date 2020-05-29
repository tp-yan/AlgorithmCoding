package JianzhiOffer.Chapter02.singleton;

/**Java实现单例模式必须要点：
 * 1.将构造方法用 private 修饰
 * 2.对外部声明一个获取单例的static方法，因为外部没有通过对象调用方法获得，只能通过类方法获取单例，必须是static
 * 3.声明一个static域用于引用单例
 * 4.初始化static域
 *
 * 实现单例模式：只适用于单线程版。-- 不推荐
 */
public final class Singleton1 {
    // 首先必须将构造方法声明为 private
    private Singleton1(){}
    private static Singleton1 instance;

    public static Singleton1 getInstance() {
        if (instance == null)
            instance = new Singleton1(); // 在多线程下，若多个线程执行到 创建实例 这行代码，就会创建不止一个实例，那么就不是单例模式了
        return instance;
    }
}
