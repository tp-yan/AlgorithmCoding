package JianzhiOffer.Chapter02.singleton.relative;

/**
 * 1）子类是不能继承父类的static变量和方法的。因为这是属于类本身的。但是子类是可以访问的。
 * 2）子类和父类中同名的static变量和方法都是相互独立的，并不存在任何的重写的关系。
 */
public final class AmericaPresident extends President {

    private AmericaPresident(String name, String country) {
        super(name,country);
    }

    private static AmericaPresident instance = new AmericaPresident("ChuanPu","USA");

    public static AmericaPresident getInstance() {
        return instance;
    }
}
