package JavaKernelVolume1.ch08.restriction;

import JavaKernelVolume1.ch06.interfacee.Employee;
import JavaKernelVolume1.ch08.generic.Pair;
import sun.rmi.runtime.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestrictionTest {
    public static void main(String[] args) {
        /* 1.运行时类型查询只适用于原始类型
         * instanceof不能用于泛型类型 if (a instanceof Pair<T>) // Error
         * */
        // 可以通过尝试强制类型转换，然后捕获是否发生异常来判断是否制定泛型类型
        Object a = new Object();
        try {
            Pair<String> p = (Pair<String>) a;
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, " a is not Pair<String>", e);
        }
        Pair<String> stringPair = new Pair<>();
        Pair<Employee> employeePair = new Pair<>();
        if (stringPair.getClass() == employeePair.getClass()) {
            Logger.getGlobal().info("Pair<String>.getClass() == Pair<Employee>.getClass()");
        }

        /* 2.不能创建参数化类型的数组
        * Pair<String>[] table = new Pair<String>[10]; // Error
         声明类型为Pair<String>[] 的变量仍是合法的。
         提示：如果需要收集参数化类型对象，只有一种安全而有效的方法：使用ArrayList:ArrayList<Pair<String>>
        * */
        // 可以声明通配类型的数组，然后进行类型转换：
        Pair<String>[] table = (Pair<String>[]) new Pair<?>[10];
        Logger.getGlobal().info(table.toString() + "," + table.length);

        /* 3.Varargs 警告 */
        Pair<String> pair1 = new Pair<>();
        Pair<String> pair2 = new Pair<>();
        Pair<String>[] table2 = array(pair1, pair2);

        Object[] objArray = table;
        objArray[0] = new Pair<Employee>(); // 这里赋值不会出现ArrayStoreException 异常（因为数组存储只会检查擦除后的类型)，
        // 但在处理table[0] 时会得到一个异常。

        /* 4.不能实例化类型变量
         * 不能使用像new T(...，)newT[...] 或T.class 这样的表达式中的类型变量
         * */
        // (1)在Java SE 8 之后，最好的解决办法是让调用者提供一个构造器表达式。例如：
        Pair<String> p = Pair.makePair(String::new);
        Logger.getGlobal().info(p.getFirst());
        Logger.getGlobal().info(p.getSecond());
        // (2)传统的解决方法是通过反射调用Class.newInstance 方法来构造泛型对象
        Pair<String> p2 = Pair.makePair(String.class);
        assert p2 != null;
        Logger.getGlobal().info(p2.getFirst());
        Logger.getGlobal().info(p2.getSecond());

        /* 5.不能构造泛型数组 T[]
        * 注意：泛型数组T[]与参数化类型的数组Pair<String>[]的区别，两者不是一回事儿！
        * 可以声明T[]，但不能直接使用new T[n]。如同参数化数组一样，可以通过其他方式创建T[]
        *  */
        // (1) 使用构造器表达式生成T[]
        String[] ss = ArrayAlg.minmax((IntFunction<String[]>) String[]::new,"Tom", "Dick", "Harry");
        Logger.getGlobal().info("min:"+ss[0]);
        Logger.getGlobal().info("max:"+ss[1]);
        // (2) 使用反射...(传统方法)
        String[] ss2 = ArrayAlg.minmax("Tom", "Dick", "Harry");
        Logger.getGlobal().info("min:"+ss2[0]);
        Logger.getGlobal().info("max:"+ss2[1]);
        // (3) 使用ArrayList.toArray()
        String[] ss3 = (String[]) new ArrayList<String>().toArray();

    }

    /* 3.Varargs 警告 */
    // @SafeVarargs抑制，向参数个数可变的方法传递一个泛型类型的实例时的编译警告
    // 对于只需要读取参数数组元素的所有方法，都可以使用这个注解，这仅限于最常见的用例。
    @SafeVarargs
    public static <T> void addAll(Collection<T> coll, T... ts) {
        for (T t : ts) {
            coll.add(t);
        }
    }

    // 可以使用@SafeVarargs 标注来消除创建泛型数组的有关限制
    @SafeVarargs
    static <E> E[] array(E... array) {// 将传入参数组成 泛型数组返回
        return array;
    }
}

class ArrayAlg {
    // (1) 直接返回泛型数组，不使用包装类Pair<T>
    // 比对Pair类中的 makePair(Supplier<T> constr)方法中的Supplier<T>接口，返回的是一个String对象，这里是String数组
    public static <T extends Comparable<T>> T[] minmax(IntFunction<T[]> constr, T... a) {
        // 这里的2会传递给 函数String::new，并将其函数结果返回
        T[] mm = constr.apply(2); // 构造器表达式String[]::new 指示一个函数，给定所需的长度，会构造一个指定长度的 String数组。
        mm[0] = mm[1] = a[0];
        for (T t : a) {
            if (t.compareTo(mm[0]) < 0) mm[0] = t;
            if (t.compareTo(mm[1]) > 0) mm[1] = t;
        }
        return mm;
    }

    // (2)比较老式的方法是利用反射，调用Array.newInstance
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T extends Comparable> T[] minmax(T... a) {
        T[] mm = (T[]) Array.newInstance(a.getClass().getComponentType(),2);
        for (T t : a) {
            if (t.compareTo(mm[0]) < 0) mm[0] = t;
            if (t.compareTo(mm[1]) > 0) mm[1] = t;
        }
        return mm;
    }
}