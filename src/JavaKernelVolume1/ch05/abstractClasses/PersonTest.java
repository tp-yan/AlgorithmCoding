package JavaKernelVolume1.ch05.abstractClasses;

public class PersonTest {

    public static void main(String[] args) {
        Person[] people = new Person[2];
        people[0] = new Employee("Wang", 5000, 1999, 12, 1);
        people[1] = new Student("Zhang", "CS");

        for (Person p : people) {
            System.out.println(p.getName() + ", " + p.getDescription());
        }
    }
}
