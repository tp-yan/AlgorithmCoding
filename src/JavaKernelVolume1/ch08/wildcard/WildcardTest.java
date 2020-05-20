package JavaKernelVolume1.ch08.wildcard;

import JavaKernelVolume1.ch05.equalsAndhashCode.Employee;
import JavaKernelVolume1.ch05.equalsAndhashCode.Manager;
import JavaKernelVolume1.ch08.A;
import JavaKernelVolume1.ch08.generic.Pair;

import java.util.ArrayList;
import java.util.function.Predicate;

public class WildcardTest {
    /* 1.通配符
     * 通配符不是类型变量，因此，不能在编写代码中使用 "?" 作为一种类型。
     * 类型Pair<?> 有以下方法：
        ? getFirst()
        void setFirst(?)
        getFirst 的返回值只能赋给一个Object。setFirst 方法不能被调用，甚至不能用Object 调用。可以调用setFirst(null).
        Pair<?> 和Pair 本质的不同在于：可以用任意Object 对象调用原始Pair 类的setObject方法。
     *  */
    // 缺点：不能将Pair<Manager> 传递给这个方法
    public static void printBuddies(Pair<Employee> ps) {
        Employee first = ps.getFirst();
        Employee second = ps.getSecond();
        System.out.println(first.getName() + " and " + second.getName() + " are buddies.");
    }

    /* Pair<? extends Employee>:表示任何泛型Pair 类型，它的类型参数是Employee 的子类
    Pair<Manager>和Pair<Employee> 都是Pair<? extends Employee> 的子类型
    */
    public static void newPrintBuddies(Pair<? extends Employee> ps) {
        Employee first = ps.getFirst();
        Employee second = ps.getSecond();
        System.out.println(first.getName() + " and " + second.getName() + " are buddies.");
    }

    /* 2.通配符的超类型限定 */
    // 超类型限定应用(1)
    public static void minmaxBonus(Manager[] a, Pair<? super Manager> result) {
        if (a == null || a.length == 0) return;
        Manager min, max;
        min = max = a[0];
        for (Manager m : a) {
            if (min.getBonus() > m.getBonus()) min = m;
            if (max.getBonus() < m.getBonus()) max = m;
        }
        // 只能传入Manager或其子类对象
        result.setFirst(min);
        result.setSecond(max);
    }

    public static void maxminBonus(Manager[] a, Pair<? super Manager> result) {
        minmaxBonus(a,result);
        PairAlg.swapHelper(result); // 在这里，通配符捕获机制是不可避免的
    }

    /* 超类型限定应用(2)：
    Comparable接口原型：
    public interface Comparable<T>
    {
        public int compareTo(T other);
    }
    使用下述超类型限定后，其中的 compareTo方法变成：
    public int compareTo(? super T other);

     */
    public static <T extends Comparable<? super T>> T min(T[] a) {
        if (a == null || a.length <= 0) return null;
        T m = a[0];
        for (T t : a) {
            if (m.compareTo(t) < 0) m = t;
        }
        return m;
    }

    /*
    * 子类型限定的另一个常见的用法是作为一个函数式接口的参数类型。例如,
        Collection 接口有一个方法：
        default boolean removeIf(Predicated<? super E> filter)
    * */
    public static void removeElement() {
        ArrayList<Employee> staff = new ArrayList<>();
        Predicate<Object> oddHashCode = obj -> obj.hashCode() % 2 != 0;
        staff.removeIf(oddHashCode);
    }


    public static void main(String[] args) {
        Manager ceo = new Manager("Zhangjianlin", 2000, 2019, 9, 1);
        ceo.setBonus(1000);
        Manager cfo = new Manager("Wangshuang", 50000, 2019, 2, 1);
        cfo.setBonus(5000);
        Pair<Manager> managerBuddies = new Pair<>(ceo, cfo);
        Employee e1 = new Employee("Zhaosi", 1000, 2020, 2, 11);
        Employee e2 = new Employee("Lilin", 500, 2018, 10, 11);
        Pair<Employee> employeePair = new Pair<>(e1, e2);
        printBuddies(employeePair);
        newPrintBuddies(managerBuddies);
        newPrintBuddies(employeePair);

        Pair<? extends Employee> wildcardPair = managerBuddies;
//        wildcardPair.setFirst(); // 这样不能调用setFirst 方法。编译器只知道需要某个Employee 的子类型，但不知道体是什么类型。它拒绝传递任何特定的类型。
        Employee a = wildcardPair.getFirst(); // 同理调用getFirst也只能返回值一个Employee引用

        Manager[] managers = {ceo, cfo};
        Pair<Employee> result = new Pair<>();
        minmaxBonus(managers, result);
        System.out.println("first: " + result.getFirst().getName()
                + ", second: " + result.getSecond().getName());
        maxminBonus(managers, result);
        System.out.println("first: " + result.getFirst().getName()
                + ", second: " + result.getSecond().getName());

    }
}

class PairAlg {
    public static boolean hasNulls(Pair<?> pair) {
        return pair.getFirst() == null || pair.getSecond() == null;
    }

    // swapHelper 是一个泛型方法，而swap 不是，它具有固定的Pair<?> 类型的参数。
    public static void swap(Pair<?> p) {
        swapHelper(p); // 在这种情况下，swapHelper 方法的参数T 捕获通配符。
    }

    public static <T> void swapHelper(Pair<T> pair) {
        T t = pair.getFirst();
        pair.setFirst(pair.getSecond());
        pair.setSecond(t);
    }
}


