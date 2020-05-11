package JavaKernelVolume1.ch05.abstractClasses;

public class Student extends Person{
    private String major;


    public Student(String name, String major) {
        super(name);
        this.major = major;
    }

    @Override
    public String getDescription() {
        return "a student majoring in "+major;
    }
}
