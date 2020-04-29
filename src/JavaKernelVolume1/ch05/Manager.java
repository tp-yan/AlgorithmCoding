package JavaKernelVolume1.ch05;

/**
 * 经理也是属于雇员的一种。
 * 在子类中可以增加域、增加方法或覆盖超类的方法，然而绝对不能删除继承的任何域和方法。
 */
public class Manager extends Employee {
    private double bonus; // 经理特有的奖金属性

    /* 由于Manager 类的构造器不能访问Employee类的私有域，所以必须利用Employee类的构造器对这部分私有域进行初始化。 */
    public Manager(String name, double salary, int year, int month, int day) {
        super(name, salary, year, month, day); // 必须第一句话，若子类构造器没有显示调用父类构造器则默认调用父类无参构造器
        this.bonus = 0;
    }

    public double getSalary() {
        return super.getSalary() + bonus; // 覆盖父类的getSalary()。此处不能直接访问父类的private域salary，必须调用父类的
        // public getSalary()。关键字super不是父类对象的引用，只是告诉编译器这里调用父类的方法，它并不能赋值给其他变量。
        // 同时不能直接调用getSalary()，否则陷入无限调用子类getSalary循环，导致栈溢出。
    }

    public void setBonus(double b) {
        bonus = b;
    }
}
