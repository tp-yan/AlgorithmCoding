package JavaKernelVolume1.ch06;

import JavaKernelVolume1.ch06.interfacee.Employee;

import java.util.Arrays;
import java.util.Collection;

public class Chapter06 {
    public static void main(String[] args) {
        Employee[] staff = new Employee[3];
        staff[0] = new Employee("Harry Hacker", 35000, 2012, 11, 1);
        staff[1] = new Employee("Carl Cracker", 75000, 2019, 8, 1);
        staff[2] = new Employee("Tony Tester", 38000, 2020, 2, 11);
        Arrays.sort(staff);
        // print out information about all Employee objects
        for (Employee e : staff)
            System.out.println("name=" + e.getName() + " , salary=" + e.getSalary());

        System.out.println(new Test2(){}.getClass());
        System.out.println(Test2.class);
        System.out.println(Test2.class instanceof Class);
        
    }
}

interface Test2{
}
