package JavaKernelVolume1.ch09.viewAndWrapper;

import JavaKernelVolume1.ch05.equalsAndhashCode.Employee;

import java.util.*;

import static java.util.Collections.singleton;

/*
 * keySet 方法返回一个实现Set接口的类对象，这个类的方法对原映射进行操作。这种集合称为视图。
 *
 */
public class ViewTest {
    public static void main(String[] args) {
        // 1.轻 量 级 集 合 包 装 器
        /* Arrays 类的静态方法asList 将返回一个包装了普通Java 数组的List 包装器。
          调用视图改变数组大小的所有方法（例如，与迭代器相关的add 和remove 方法）都会抛出一个Unsupported OperationException 异常。
         */
        Integer[] values = {1, 2, 3, 4, 5};
        List<Integer> intList = Arrays.asList(values); // 返回的是一个视图对象，实现了List接口，具有List的所有方法。
        intList.set(0, 0); // 可以设置元素，但不能改变其包装的数组大小。
        System.out.println(intList);
        List<String> names = Arrays.asList("Amy", "Bob", "Carl");
        System.out.println(names);
        List<String> nObj = Collections.nCopies(10, "Zhang"); // 返回一个实现了List 接口的不可修改的对象
        System.out.println(nObj);
        //  Collections 类包含很多实用方法，这些方法的参数和返回值都是集合。
        Set<Employee> e = Collections.singleton(new Employee("wang")); // 返回的对象实现了一个不可修改的单元素集（视图）
        System.out.println(e);

        // 2. 子 范 围
        /* 可以将任何操作应用于子范围，并且能够自动地反映整个列表的情况。 */
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(10);
        arrayList.add(11);
        arrayList.add(12);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        System.out.println(arrayList);
        List<Integer> subrange = arrayList.subList(1, 4);
        subrange.clear();
        System.out.println(arrayList);

        // 3.不可修改的视图
        /* Collections 还有几个方法，用于产生集合的不可修改视图。
        这些视图对现有集合增加了一个运行时的检查。如果发现试图对集合进行修改，就抛出一个异常，同时这个集合将保持未修改的状态。 */
        List<Integer> unList = Collections.unmodifiableList(arrayList);
        System.out.println(unList);
//        unList.clear(); // Error

        // 4.同步视图
        /* 类库的设计者使用视图机制来确保常规集合的线程安全，而不是实现线程安全的集合类。例如，Collections 类的静态
        synchronizedMap 方法可以将任何一个映射表转换成具有同步访问方法的Map: */
        // 现在，就可以由多线程访问map 对象了
        // return a synchronized view of the specified map.
        Map<String, Employee> map = Collections.synchronizedMap(new HashMap<String, Employee>());

        // 5.受查视图：视图的add 方法将检测插入的对象是否属于给定的类。
        ArrayList<String> strings = new ArrayList<>();
        ArrayList rawList = strings; // warning only, not an error, for compatibility with legacy code
        rawList.add(new Date()); // now strings contains a Date object!
        List<String> safeStrings = Collections.checkedList(strings, String.class);
        List rawList2 = safeStrings;
        rawList2.add(new Date()); // 运行到此步抛出异常
    }
}
