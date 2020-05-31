package JianzhiOffer.Chapter02.singleton;

import java.util.HashMap;

/**
 * 实现单例模式：多线程安全版，使用静态内部类实现。-- 推荐
 * 补充：外部类和内部类可以访问对方的所有成员和方法（包括私有）。
 */
public class Singleton5 {
    private Singleton5() {
//        new InnerClass().test(); // 测试
    }

    // 只有在需要使用实例时，才会加载InnerClass类，然后会初始化其静态域 instance
    public static Singleton5 getInstance() {
        return InnerClass.outInstance;
    }

    /**
     * private: 只能在Singleton5内部内访问
     */
    private static class InnerClass{
        //  静态域初始化 只会在加载时调用而且只被调用一次。故final可用可不用
        private static final Singleton5 outInstance = new Singleton5();

        private void test(){} // 用于测试
    }

    public static void main(String[] args) {
        Singleton5 singleton = Singleton5.getInstance();
        InnerClass innerClass = new Singleton5.InnerClass();
        innerClass = new InnerClass();

    }
}
