package JavaKernelVolume1.ch05;

import java.time.LocalDate;
import java.util.Random;

public class Employee {

    private LocalDate hireDay;
    private String name;
    private double salary;

    public Employee(String name, double salary,int year,int month, int day) {
        this.name = name;
        this.salary = salary;
        this.hireDay = LocalDate.of(year,month,day);
    }

    public void raiseSalary(double byPercent) {
        salary += salary * byPercent/100;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDay() {
        return hireDay; // 引用指向不可变对象，可直接返回引用
    }
}
