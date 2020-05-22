package JavaKernelVolume1.ch09.set;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class TreeSetTest {
    public static void main(String[] args) {
        SortedSet<Item> parts = new TreeSet<>();
        parts.add(new Item("Toaster", 1234));
        parts.add(new Item("Widget", 4562));
        parts.add(new Item("Modem", 9912));
        System.out.println(parts); // 使用 Item 默认的 compareTo方法排序

        //  从JavaSE 6 起，TreeSet 类实现了NavigableSet 接口。这个接口增加了几个便于定位元素以及反向遍历的方法。
        // 外部传入比较器Comparator：根据Item的description排序
        NavigableSet<Item> sortByDescription = new TreeSet<>(Comparator.comparing(Item::getDescription));
        sortByDescription.addAll(parts);
        System.out.println(sortByDescription);
    }
}

