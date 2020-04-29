package JavaKernelVolume1.ch05;

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

        /* 2. */

    }
}
