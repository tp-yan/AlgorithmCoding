package JavaKernelVolume1.ch05.abstractClasses;

/**
 * 抽象类也可以包含具体数据和具体方法，可以没有abstract方法，不能实例对象。
 */
public abstract class Person {
    private String name;
    public abstract String getDescription(); // 必须将通过方法放在父类，若放在子类中，那么编译时Person引用将无法调用此方法

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
