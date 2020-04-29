package JavaKernelVolume1.ch04;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Random;

public class Chapter04 {
    /* 1.LocalDate类 */
    // Date类用来得到、表示时间点，而LocalDate适合用来表示、计算日期
    private void printThisMonth() {
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int today = date.getDayOfMonth();
        date = date.minusDays(today - 1); // 设为月初
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int week = dayOfWeek.getValue(); // 当前日期是星期几

        // 打印表头
        System.out.println("Mon Tus Wed Thu Fri Sat Sun");
        for (int i = 1; i < week; i++) {
            System.out.print("    ");
        }
        // 打印日历
        while (date.getMonthValue() == month) {
            System.out.printf("%3d", date.getDayOfMonth());
            if (today == date.getDayOfMonth()) {
                System.out.print("*");
            } else {
                System.out.print(" ");
            }
            date = date.plusDays(1);
            if (date.getDayOfWeek().getValue() == 1) {
                System.out.println();
            }
        }
        if (date.getDayOfWeek().getValue() != 1)
            System.out.println();
        System.out.println(date);
    }


    public static void main(String[] args) {
        Chapter04 c04 = new Chapter04();
        c04.printThisMonth();

        // fill the staff array with three Employee objects
        Employee[] staff = new Employee[3];
        staff[0] = new Employee("Harry", 40000);
        staff[1] = new Employee(60000);
        staff[2] = new Employee();
        // print out information about all Employee objects
        for (Employee e : staff)
            System.out.println("name=" + e.getName() + ",id=" + e.getId() + ",salary:"
                    + e.getSalary());
    }
}

/* 2.类的域初始化步骤 + final 和 static 域 */
// 非public和private类，默认为包访问权限，即同包下的其他类可以导入此类，且可访问没有修饰符的域（这种域很危险，破坏类的了封装性）
class Employee {
    /**
     * 静态域在类加载时进行初始化，实例域在对象创建时必须初始化。
     * <p>
     * 实例域初始化顺序：
     * 1 ) 所有数据域被初始化为默认值（0、false 或null 。)
     * 2 ) 按照在类声明中出现的次序，依次执行所有域初始化语句和初始化块。
     * 3 ) 如果构造器第一行调用了第二个构造器，则执行第二个构造器主体
     * 4 ) 执行这个构造器的主体.
     * <p>
     * 在类第一次加载的时候，将会进行静态域的初始化。
     * 与实例域一样，除非将它们显式地设置成其他值，否则默认的初始值是0、false 或null。
     * 所有的静态初始化语句以及静态初始化块都将依照类定义的顺序执行。
     */

    private static int nextId;

    private final int id; // final实例域，必须确保在构造方法执行后被初始化，且后面不能被修改
    private String name;
    private double salary = 0.0; // 实例域定义时初始化

    // 静态初始化块
    static {
        Random r = new Random();
        nextId = r.nextInt(10000);
    }

    // 对象初始化块
    {
        id = nextId;
        nextId++;
    }

    public Employee(double aSalary) {
        // this() 必须是第一行代码才能调用其他构造方法
        this("Employee #" + nextId, aSalary);
    }

    public Employee(String aName, double aSalary) {
        name = aName;
        salary = aSalary;
    }

    public Employee() {// 若重载了构造方法，则无参构造方法只能自己手动添加，系统不会添加
        // 函数体为空，那么所有的实例域使用默认值初始化:0、false、null
    }

    // 类的每个方法都包含：隐式参数(即调用此方法的对象，也就是this)+显示参数(方法签名中的参数，可无)
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
}
