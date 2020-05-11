package JavaKernelVolume1.ch05.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodTableTest {
    public static void main(String[] args) throws NoSuchMethodException {
        // Method对象相当于方法指针
        Method square = MethodTableTest.class.getMethod("square",double.class);
        Method sqrt = Math.class.getMethod("sqrt",double.class);
        printTable(1,10,10,square);
        printTable(1,10,10,sqrt);
    }

    public static double square(double x) {
        return x * x;
    }

    public static void printTable(double from, double to, int n, Method f) {
        System.out.println(f);
        double dx = (to - from) / (n - 1);
        for (double x = from; x <= to; x += dx) {
            try {
                double y = (Double) f.invoke(null, x); // 调用静态方法，第一个参数传入null
                System.out.printf("%10.4f | %10.4f\n", x, y);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
