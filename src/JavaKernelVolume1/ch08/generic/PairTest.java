package JavaKernelVolume1.ch08.generic;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

/*
* 虚拟机中没有泛型，只有普通的类和方法。
* */
public class PairTest {
    @SuppressWarnings("unchcked")
    public void configureSlider(){
        Dictionary<Integer, Component> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel(new ImageIcon("nine.gif")));
        labelTable.put(20, new JLabel(new ImageIcon("ten.gif")));
        JSlider slider = new JSlider();
        slider.setLabelTable(labelTable);

        // 添加注解，抑制警告。也可以在方法前添加，抑制整个方法中的编译警告
        @SuppressWarnings("unchecked")
        Dictionary<Integer, Component> tmp = slider.getLabelTable();
    }

    public static void main(String[] args) {
        String[] words = {"Mary", "had", "a", "little", "lamb"};
        Pair<String> mm = ArrayAlg.minmax(words);
        System.out.println(mm.<String>getFirst());
        System.out.println(mm.getFirst());
        System.out.println("min = " + mm.getFirst());
        System.out.println("max = " + mm.getSecond());

        // 当调用一个泛型方法时,在方法名前的尖括号中放入具体的类型
        String middle = ArrayAlg.<String>getMiddle("JohnM", "Q.", "Public");
        // 大多数情况下可以省略类型参数，有编译器根据传入参数类型来确定
        middle = ArrayAlg.getMiddle("JohnM", "Q.", "Public");
        // 当从参数推导出的类型不一致时会寻找它们的公共父类
        Number mi = ArrayAlg.getMiddle(3.14, 123, 0);
        System.out.println(mi);

        LocalDate[] birthdays =
                {
                        LocalDate.of(1906, 12, 9), // C. Hopper
                        LocalDate.of(1815, 12, 10), // A. Lovelace
                        LocalDate.of(1903, 12, 3), // 3 . von Neumann
                        LocalDate.of(1910, 6, 22), // K. Zuse
                };
        Pair<LocalDate> mmD = ArrayAlg.minmax(birthdays);
        System.out.println("min = " + mmD.getFirst());
        System.out.println("max = " + mmD.getSecond());

        DateInterval dateInterval = new DateInterval();
        dateInterval.setFirst(LocalDate.of(1906, 12, 9));
        LocalDate second = LocalDate.of(2006, 12, 9);
        Pair<LocalDate> pair = dateInterval;
        pair.setSecond(second);

    }
}

class ArrayAlg {
    // 限定类型变量
    public static <T extends Comparable> Pair<T> minmax(T[] a) {
        if (a == null || a.length == 0) return null;
        T min = a[0];
        T max = a[0];
        for (T str : a) {
            if (min.compareTo(str) > 0) min = str;
            if (max.compareTo(str) < 0) max = str;
        }
        return new Pair<>(min, max);
    }

    /* 1.泛型方法 */
    // 在非泛型类中中定义泛型方法
    public static <T> T getMiddle(T... a) {
        return a[a.length / 2];
    }

    /* 2.类型变量的限定
    * <T extends BoundingType> ： 表示T 应该是BoundingType的子类型（subtype)。T 和BoundingType可以是类，也可以是接口。
    * 一个类型变量或通配符可以有多个限定（至多一个类、可多个接口），例如： T extends Comparable & Serializable
    * */
    // Comparable是一个泛型接口
    public static <T extends Comparable> T min(T[] a) {
        if (a == null || a.length == 0) return null;
        T smallest = a[0];
        for (T v : a) {
            if (v.compareTo(smallest) < 0) smallest = v;
        }
        return smallest;
    }
}

class DateInterval extends Pair<LocalDate> {
    /* 擦除类型变量后Pair的方法：
    * public void setSecond(Object second) {
        this.second = second;
    }
    * 这里虽然是重写父类方法，但是请注意这里的参数类型与父类不同(按定义就不是重写方法)。这是因为类型擦除与多态发生了冲突。
    * 编译器在 DateInterval 类中生成了一个桥方法（bridge method):
    * public void setSecond(Object second) { setSecond( (Date)second ); }
    * 实际上由桥方法调用此方法，实际上也是桥方法覆盖了父类方法
    * */
    @Override
    public void setSecond(LocalDate second) {
        Logger.getGlobal().info("setSecond(LocalDate second)");
        if (second.compareTo(getFirst()) >= 0)
            super.setSecond(second);
    }

    /**
     * 编译器也会为此方法生成桥方法，那么DateInterval就会有2个同名方法（直接在代码中这样定义是不行的）：
     * LocalDate getSecond() // defined in DateInterval
     * Object getSecond() // overrides the method defined in Pair to call the first method。桥方法-为了保持多态。
     * 这样编译器可能产生两个仅返回类型不同的方法字节码，而虚拟机可以根据参数类型和返回类型确定一个方法。
     */
    @Override
    public LocalDate getSecond() {
        return super.getSecond();
    }
}