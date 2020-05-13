package JavaKernelVolume1.ch05;

import java.util.ArrayList;
import java.util.Scanner;

public class Chapter05 {

    public static void main(String[] args) {
        /* 1.继承、覆盖方法、子类构造器 */
        // construct a Manager object
        Manager boss = new Manager("Carl Cracker", 80000, 1987, 12, 15);
        boss.setBonus(5000);

        Employee[] staff = new Employee[3];
        // fill the staff array with Manager and Employee objects
        /* 虽然boss和staff[0]都指向同一个对象，但是编译器将staff[0]看成Employee 对象。
        * 这意味着，可以这样调用：boss.setBonus(5000); // OK
        * 但不能这样调用：staff[0].setBonus(5000); // Error
        * 这是因为staff[0] 声明的类型是Employee, 在编译器看来setBonus不是Employee类的方法。
        * 所以编译器看的是变量声明类型，而JVM看的是对象的实际类型。
        */
        staff[0] = boss;
        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff[2] = new Employee("Tommy Tester", 40000, 1990, 3, 15);

        for (Employee e : staff) {
            // 一个对象变量（例如，变量e ) 可以指示多种实际类型的现象被称为多态（polymorphism)。
            // 在运行时能够自动地选择调用哪个方法的现象称为动态绑定（dynamic binding）。
            // 对于重载的方法：e.getSalary()，JVM会根据e指向的实际类型来调用子类Manger的getSalary还是父类Employee的getSalary。
            System.out.println("name=" + e.getName() + ",salary=" + e.getSalary());
        }

        /* 2.自动装箱、拆箱 */
        Integer n = 3; // 所有包装类都是final类，且域value也是final的即，初始化后不可改变
        System.out.println(n);
        n++; // 自动拆箱后又自动装箱
        System.out.println(n);
        Integer a = 1000;
        Integer b = 1000;
        Integer c = 100;
        Integer d = 100;
        /*
         * 自动装箱规范要求 boolean、byte、 char 127，介于 -128 ~ 127 之间的short 和 int 被包装到固定的对象中。
         * 在此范围内的包装类对象使用 == 比较与其数值使用 == 结果一致。此范围外用 equals比较大小
         */
        System.out.println("a == b? : "+(a == b));
        System.out.println("a.equals(b):"+a.equals(b));
        System.out.println("c == d? : "+(c == d));
        System.out.println("c.equals(d):"+c.equals(d));


        /* 3.参数数量可变 */
        maxDouble(1.2,1.4,-9.0); // 1.2,1.4,-9.0：自动装箱为 new double[]{1.2,1.4,-9.0}传给maxDouble。

        /* 4.枚举类 */
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a size: (SMALL, MEDIUM, LARGE, EXTRA_LARGE)");
        String input = in.next().toUpperCase();
        Size size = Enum.valueOf(Size.class,input); // 第一个参数也是泛型参数 Enum<Size>的简写
        System.out.println("Size ="+size); // toString():打印枚举常量全名
        System.out.println("Abbreviation="+size.getAbbreviation());
        System.out.println("Ordinal="+size.ordinal());

        Integer mint = 10;
        triple(mint);
        System.out.println(mint);

    }

    public static double maxDouble(double... values) {
        double largest = Double.NEGATIVE_INFINITY;
        for (double v : values) {
            if (largest < v)
                largest = v;
        }
        return largest;
    }

    public static void triple(Integer x) {
        x = 3*x;
        System.out.println(x);
    }
}

/* 4.定义枚举常量实际是定义枚举类，枚举类都继承自Enum */
enum Size{
    //  构造器只是在构造枚举常量的时候被调用:
    SMALL("S"),MEDIUM("M"),LARGE("L"),EXTRA_LARGE("XL");

    // 枚举类可以有构造方法、域和其他方法
    private String abbreviation;
    private Size(String abbreviation){
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
