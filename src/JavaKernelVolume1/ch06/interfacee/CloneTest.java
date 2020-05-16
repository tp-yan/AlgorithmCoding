package JavaKernelVolume1.ch06.interfacee;

public class CloneTest {
    public static void main(String[] args) {
        Employee original = new Employee("Zhangsan",5000);
        original.setHireDay(2020,11,4);
        try {
            Employee copy = original.clone();
            copy.raiseSalary(10);
            copy.setHireDay(2019,10,12);
            System.out.println("original:"+original);
            System.out.println("copy:"+copy);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
