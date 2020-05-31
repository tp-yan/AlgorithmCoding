package JianzhiOffer.Chapter02.singleton.relative;

public class President {
    protected String name;
    protected String country;

    // 父类构造函数不能是 private，否则子类无法调用
    protected President(String name, String country) {
        this.name = name;
        this.country = country;
    }

    // 既然 构造器都是protected，那么父类就不可能是单例模式了
//    private static President instance = new President(null,null);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return getClass().getName()+"{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
