package JavaKernelVolume1.ch09.map;

import JavaKernelVolume1.ch05.equalsAndhashCode.Employee;

import java.util.*;

enum Weekday {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};

public class MapTest {
    public static void main(String[] args) {
        // 1.基本映射操作
        Map<String, Employee> staff = new HashMap<>();
        staff.put("144-25-5464", new Employee("Amy Lee"));
        staff.put("567-24-2546", new Employee("Harry Hacker"));
        staff.put("157-62-7935", new Employee("Cary Cooper"));
        staff.put("456-62-5527", new Employee("Francesca Cruz"));
        System.out.println(staff);

        staff.remove("567-24-2546");
        staff.put("456-62-5527", new Employee("Francesca Miller"));
        System.out.println(staff.get("157-62-7935"));
        staff.forEach((K, V) -> System.out.println("key=" + K + ", value:" + V));
        System.out.println("========================================");

        // 2.更新映射项
        Map<String, Integer> counts = new HashMap<>();
        String word = "Tom";
        // (1)
        counts.put(word, counts.getOrDefault(word, 0) + 1);
        // (2)
        counts.putIfAbsent(word, 0);
        counts.put(word, counts.get(word) + 1);
        // (3) 将把word 与1 关联，否则使用Integer::sum 函数组合原值和1 (也就是将原值与1 求和)。
        counts.merge(word, 1, Integer::sum);

        // 3.映射视图：视图会影响到原对象，若在视图上删除键或值，其对应的key-value都会被删除，但不能通过视图增加元素
        // 有3 种视图：键集、值集合（不是一个Set）以及键/值对集。
        Set<String> keys = staff.keySet(); // keySet不是HashSet或TreeSet，而是实现了Set 接口的另外某个类的对象。Set 接口扩展了Collection 接口。
        for (String k : keys) {
            System.out.println(k);
        }
        System.out.println("========================================");
        // 如果想同时查看键和值，可以通过枚举条目来避免查找值。现在都使用forEach 方法代替。
        for (Map.Entry<String, Employee> entry : staff.entrySet()) {
            String k = entry.getKey();
            Employee v = entry.getValue();
        }
        staff.forEach((k, v) -> System.out.println(k + "->" + v));
        System.out.println("========================================");

        // 4.链接散列集与映射
        // LinkedHashSet 和LinkedHashMap 类用来记住插人元素项的顺序。当条目插入到表中时，就会并人到双向链表中。
        // 默认记住插入条目的顺序
        Map<String, Employee> staff2 = new LinkedHashMap<>();
        staff2.put("144-25-5464", new Employee("Amy Lee"));
        staff2.put("567-24-2546", new Employee("Harry Hacker"));
        staff2.put("157-62-7935", new Employee("Gary Cooper"));
        staff2.put("456-62-5527", new Employee("Francesca Cruz"));
        Iterator<String> iter = staff2.keySet().iterator();
        while (iter.hasNext())
            System.out.println(iter.next());
        Iterator<Employee> vIter = staff2.values().iterator();
        while (vIter.hasNext())
            System.out.println(vIter.next());
        System.out.println("========================================");

        // LinkedHashMap可将条目顺序设为访问顺序，而不是插入顺序。
        // 每次调用get 或put, 受到影响的条目将从当前的位置删除，并放到条目链表的尾部。
        Map<String, String> cache = new LinkedHashMap<>(128, 0.75f, true);

        // 5.枚举集与映射
        EnumSet<Weekday> always = EnumSet.allOf(Weekday.class);
        EnumSet<Weekday> never = EnumSet.noneOf(Weekday.class);
        EnumSet<Weekday> workday = EnumSet.range(Weekday.MONDAY, Weekday.FRIDAY);
        EnumSet<Weekday> mwf = EnumSet.of(Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY);

    }
}
