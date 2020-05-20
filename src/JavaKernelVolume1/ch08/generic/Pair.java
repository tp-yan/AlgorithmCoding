package JavaKernelVolume1.ch08.generic;

import java.util.function.Supplier;

/**
 * 一个泛型类（generic class) 就是具有一个或多个类型变量的类。
 * 类定义中的类型变量可指定 方法的返回类型以及域和局部变量的类型。
 */
public class Pair<T> implements Cloneable { // 类型变量 T，用尖括号(< >) 括起来，并放在类名的后面 。
    private T first;
    private T second;

    public Pair() {
    }

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // Supplier<T> 只有一个返回T的get方法的函数式接口
    public static <T> Pair<T> makePair(Supplier<T> constr) {
        return new Pair<>(constr.get(), constr.get());  // constr.get()返回的是 new String()的结果，即一个String对象
    }

    // 传统的解决方法是通过反射调用Class.newInstance 方法来构造泛型对象
    public static <T> Pair<T> makePair(Class<T> cl) {
        try {
            return new Pair<>(cl.newInstance(), cl.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* 当泛型被擦除后，此方法变成equals(Object obj) 跟Object的equals有冲突
    public boolean equals(T obj) {
        return super.equals(obj);
    }
     */
}
