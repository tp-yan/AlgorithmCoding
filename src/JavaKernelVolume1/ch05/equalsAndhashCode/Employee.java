package JavaKernelVolume1.ch05.equalsAndhashCode;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 除了基本类型外，其他所有对象都是Object的子类，包括数组对象。
 */
public class Employee {
    private LocalDate hireDay;
    private String name;
    private double salary;

    public Employee(String name, double salary, int year, int month, int day) {
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


    @Override // 标注：让编译器去检测此方法是否覆盖了父类中的方法，若没有则报错
    public boolean equals(Object obj) {// 1.参数必须是Object类型，否则不是override父类的equals
        // 2.引用指向同一对象，必然相等
        if (this == obj) return true;
        // 3.this肯定不为null
        if (obj == null) return false;
        // 4.getClass：返回对象的实际类型，即运行时类型，所以不影响子类之间的比较
        if (getClass()!= obj.getClass()) {
            return false;
        }

        // 5.
        Employee other = (Employee)obj;
        // 6. 比较各域是否相等。基本类型用==，对象域使用equals。
        // 为了防止name、hireDay域为null，需要使用Objects.equals()，数组对象使用Arrays.equals
        return Objects.equals(name,other.name) && salary == other.salary && Objects.equals(hireDay,other.hireDay);
    }

    /**
    若重定义了equals，就必须重写hashCode，而且equals()和hashCode()结果必须一致。
     字符串的equals和hashCode都重写过，hashCode是根据字符串内容计算出的
     */
    @Override
    public int hashCode() {// Object默认返回对象的存储地址，故对象的存储位置是由hash得到的随机位置
        /*
         * return 7*Objects.hashCode(name) // 对null安全的hashCode计算方式：null返回0，否则返回 参数.hashCode()
         *      + 11*Double.hashCode(salary)
         *      + 13*Objects.hashCode(hireDay);
         *
         * Arrays.hashCode()：计算一个数组对象的散列值（散列值：可以是正负数）
         */
        // 用于计算的域必须和equals中使用的域一样
        return Objects.hash(name,salary,hireDay); // 上述方式的优化简写，可以组合多个域的散列值
    }

    @Override
    public String toString() {
        return getClass().getName()+"{" + // 最好使用 getClass().getName()：不要直接写类名，方便子类复用
                "hireDay=" + hireDay +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }

    public void printSuperClass() {
        // getClass():返回的是类，类是Class的实例对象
        System.out.println(getClass().getSuperclass());
    }
}
