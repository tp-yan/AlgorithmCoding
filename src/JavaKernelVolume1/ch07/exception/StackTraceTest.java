package JavaKernelVolume1.ch07.exception;

import java.util.Arrays;
import java.util.Scanner;

public class StackTraceTest {
    /**
     * 打印方法的堆栈轨迹
     *
     * @param n
     * @return
     */
    public static int factorial(int n) {
        System.out.println("factorial(" + n + ")");
        Throwable t = new Throwable();
        // StackTraceElement 类含有能够获得文件名和当前执行的代码行号的方法，还能够获得类名和方法名
        StackTraceElement[] frames = t.getStackTrace(); // 获得构造这个对象（Throwable对象）时调用堆栈的跟踪。
        for (StackTraceElement e : frames) {
            System.out.println(e);
        }
        // 正式的方法体
        int r;
        if (n <= 1)
            r = 1;
        else r = n * factorial(n - 1);
        System.out.println("return " + r);
        return r;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter n: ");
        int n = in.nextInt();
        factorial (n);

    }
}
