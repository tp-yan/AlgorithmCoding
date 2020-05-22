package JavaKernelVolume1.ch09.algorithm;


import JavaKernelVolume1.ch06.interfacee.Employee;

import java.util.*;

public class ShuffleTest {
    public static void main(String[] args) {
        // 1.排序与混排
        /* 对列表进行随机访问的效率很低，一般使用归并排序对列表进行高效的排序。
            归并排序有一个主要的优点：稳定，即不需要交换相同的元素。
        * 然而，Java 程序设计语言并不是这样实现的。它直接将所有元素转入一个数组，对数组进行排序，然后，再将排序后的序列复制回列表。
        * 传递给排序算法的列表：必须是可修改的，但不必是可以改变大小的。
          •如果列表支持set 方法，则是可修改的。
          •如果列表支持add 和remove 方法，则是可改变大小的。
         */
        List<Employee> staff = new LinkedList<>();
        Collections.sort(staff); // 这个方法要求列表元素实现了Comparable 接口
        // comparingDouble:传入一个double键提取函数
        staff.sort(Comparator.comparingDouble(Employee::getSalary)); // 采用其他方式对列表进行排序
        // 根据元素类型的compareTo 方法给定排序顺序，按照逆序对列表staff 进行排序
        staff.sort(Comparator.reverseOrder()); // 返回一个比较器，比较器则返回 b.compareTo(a)。
        // 同样地，按薪资降序
        staff.sort(Comparator.comparingDouble(Employee::getSalary).reversed());


        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 49; i++) {
            numbers.add(i);
        }

        ArrayList<Integer> nums = (ArrayList<Integer>) ((ArrayList<Integer>) numbers).clone();

        /* 如果提供的列表没有实现RandomAccess 接口，shuffle 方法将元素复制到数组中，然后打乱数组元素的顺序，最后再将打乱顺序后的元素复制回列表。 */
        Collections.shuffle(numbers);
        List<Integer> winningCombination = numbers.subList(0, 6);
        Collections.sort(winningCombination);
        System.out.println(winningCombination);

        /* 2.二分查找
        Collections 类的binarySearch 方法，必须是实现了List接口的有序集合。
        如果为binarySearch 算法提供一个链表，它将自动地变为线性查找。
        只有采用随机访问，二分査找才有意义。
         */
        int index = Collections.binarySearch(nums, 10);
        System.out.println(index);
        index = Collections.binarySearch(nums, 60);
        // 可以利用返回值计算应该将element 插人到集合的哪个位置，以保持集合的有序性。插入的位置是：insertionPoint = -i - 1;
        System.out.println(index + " insertion pos=" + (-index - 1));

        // 3.其他简单算法
        System.out.println("max:"+Collections.max(nums));
        // 注意：new ArrayList<Integer>(20); 只是说这个列表初始有存储20个元素的能力，若不填充元素，则其大小为0。
        System.out.println(new ArrayList<Integer>(10).size());
        ArrayList<Integer> copyList = (ArrayList<Integer>) nums.clone();
        System.out.println("copyList:"+copyList);
        Collections.reverse(copyList);
        Collections.replaceAll(copyList,10,20);
        System.out.println("frequency:"+Collections.frequency(copyList,20));
        Collections.rotate(copyList,5);
        System.out.println("rotate:"+copyList);
        copyList.removeIf(v -> v > 30);
        System.out.println("copyList:"+copyList);

        // 4.集合与数组的转换
        // 1)Arrays.asList 包装器：数组->集合
        String[] values = {"Li","Zhang","Wang"};
        HashSet<String> names = new HashSet<>(Arrays.asList(values));
        // 2) 从集合得到数组会更困难一些。当然，可以使用toArray 方法：
        Object[] vs = names.toArray(); // 返回的数组是一个Object[] 数组，不能改变它的类型
        // 必须使用toArray 方法的一个变体形式
        String[] nvs = names.toArray(new String[0]); // 传入长度为0的数组，则自动创建一样长度的新数组；若传入指定大小的
        String[] nvs2 = names.toArray(new String[10]); // 若传入指定大小的数组，则不新建
        System.out.println(Arrays.toString(nvs));
        System.out.println(Arrays.toString(nvs2));
    }

    // 使用一个方法计算链表、数组列表或数组中最大元素了.
    public static <T extends Comparable> T max(Collection<T> c) {
        if (c.isEmpty())
            throw new NoSuchElementException();
        Iterator<T> iter = c.iterator();
        T largest = iter.next();
        while (iter.hasNext()) {
            T next = iter.next();
            if (largest.compareTo(next) < 0)
                largest = next;
        }
        return largest;
    }
}
