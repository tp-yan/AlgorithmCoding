package JavaKernelVolume1.ch06.interfacee;

import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

public class Employee implements Comparable<Employee>, Cloneable {// 重写Object的clone方法必须实现Cloneable接口，该接口作为
    // 标记接口没有任何方法

    private Date hireDay;
    private String name;
    private double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
        this.hireDay = new Date();
    }

    public Employee(String name, double salary, int year, int month, int day) {
        this.hireDay = new Date(year,month,day);
        this.name = name;
        this.salary = salary;
    }

    public void raiseSalary(double byPercent) {
        salary += salary * byPercent / 100;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public void setHireDay(int year, int month, int day) {
        Date newHireDay = new GregorianCalendar(year, month - 1, day).getTime();
        hireDay.setTime(newHireDay.getTime());
    }

    @Override
    public int compareTo(Employee o) {
        return Double.compare(this.salary, o.salary); // 比较浮点数时，一定要使用Double.compareTo()，不要直接使用比较运算符
    }

    // 覆盖Object原先的逐域赋值的clone()，修饰符改为public，否则只能自身clone。
    // 注意将返回类型设为当前类
    @Override
    public Employee clone() throws CloneNotSupportedException {
        Employee cloned = (Employee) super.clone(); // 调用Object的clone，对域逐一复制
        // 可变对象域需要单独处理(覆盖)
        cloned.hireDay = (Date) hireDay.clone();
        return cloned;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "hireDay=" + hireDay +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
