package JavaKernelVolume1.ch05.equalsAndhashCode;

public class EqualsTest {
    public static void main(String[] args) {
        Employee eA = new Employee("Zhang", 1000, 2010, 10, 1);
        Employee eB = new Employee("Zhang", 1000, 2010, 10, 1);
        Employee eC = new Employee("Li", 1000, 2010, 10, 1);
        System.out.println("eA.equals eB:" + eA.equals(eB));
        System.out.println("eA.equals eC:" + eA.equals(eC));

        Manager mA = new Manager("Li", 1000, 2010, 10, 1);
        mA.setBonus(1000);
        Manager mB = new Manager("Li", 1000, 2010, 10, 1);
        mB.setBonus(1000);
        Manager mC = new Manager("Li", 1000, 2010, 10, 1);
        mC.setBonus(2000);
        Manager mD = new Manager("Zhang", 1000, 2010, 10, 1);
        mD.setBonus(2000);

        System.out.println("mA.equals mB:" + mA.equals(mB));
        System.out.println("mA.equals mC:" + mA.equals(mC));

        System.out.println("mD.equals eA:" + mD.equals(eA));
        System.out.println("eA.equals mD:" + eA.equals(mD));

        System.out.println(eA.getClass());
        System.out.println(mA.getClass());

        eA.printSuperClass();
        mA.printSuperClass();

        System.out.println("============================================");
        Employee alicel = new Employee("Alice Adams ", 75000, 1987, 12, 15);
        Employee alice2 = alicel;
        Employee alice3 = new Employee("Alice Adams", 75000, 1987, 12, 15);
        Employee bob = new Employee("Bob Brandson", 50000, 1989, 10, 1);
        System.out.println("alicel == alice2: " + (alicel == alice2));
        System.out.println("alicel == alice3: " + (alicel == alice3));
        System.out.println("alicel.equals(alice3): " + alicel.equals(alice3));
        System.out.println("alicel.equals(bob): " + alicel.equals(bob));
        System.out.println("bob.toString()：" + bob);
        Manager carl = new Manager("Carl Cracker", 80000, 1987, 12, 15);
        Manager boss = new Manager("Carl Cracker",80000, 1987, 12, 15);
        boss.setBonus(5000);
        System.out.println("boss .toString():" + boss);
        System.out.println("carl .equals(boss): " + carl.equals(boss));
        System.out.println("alicel.hashCode(): " + alicel.hashCode());
        System.out.println("alice3 .hashCode(): " + alice3.hashCode());
        System.out.println("bob.hashCode()：" + bob.hashCode());
        System.out.println("carl.hashCode(): " + carl.hashCode());
        System.out.println("============================================");

    }
}
