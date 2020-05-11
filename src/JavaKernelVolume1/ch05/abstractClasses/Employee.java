package JavaKernelVolume1.ch05.abstractClasses;

import java.time.LocalDate;

public class Employee extends Person {
    private LocalDate hireDay;
    private double salary;


    public Employee(String name, double salary,int year,int month, int day) {
        super(name);
        this.hireDay = LocalDate.of(year,month,day);
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDay() {
        return hireDay;
    }

    @Override
    public String getDescription() {
        return String.format("an employee with a salary of $%.2f",salary);
    }

    public void raiseSalary(double byPercent) {
        salary += salary * byPercent/100;
    }

}
