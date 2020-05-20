#### 1.泛型类与泛型方法
类型参数（type parameters)的魅力在于：使得程序具有更好的可读性和安全性。
#### 泛型类
一个泛型类（generic class) 就是具有一个或多个类型变量的类。<br/>
`Pair`类引入了一个类型变量`T`，用尖括号(`< >`)括起来，并放在类名的后面。泛型类可以有多个类型变量：
```Java
public class Pair<T, U> { 
    private T first; // uses the type variable
    private U second; // uses the type variable
    public T getFirst() { return first; } // 这种不是泛型方法
    public U getSecond() { return second; }
    ...
 }
```
- 类定义中的类型变量可以用来指定方法的返回类型以及域和局部变量的类型。
- 类型变量使用大写形式，且比较短，这是很常见的。在Java 库中，使用变量`E`表示集合的元素类型，`K` 和`V` 分别表示表的关键字与值的类型。`T`(需要时还可以用临近的字母`U`和`S`) 表示“任意类型”。
#### 泛型方法
类型变量放在修饰符（这里是`public static`) 的后面，返回类型的前面。定义一个带有类型参数的泛型方法：
```Java
class ArrayAlg{
    public static <T> T getMiddle(T... a){
        return a[a.length / 2];
    }
}
```
- 必须使用类型变量(如`<T>`)定义的才是泛型方法！ 
- 泛型方法可以定义在普通类中，也可以定义在泛型类中。

#### 2.类型变量的限定
有时类或方法需要对类型变量加以约束。
- `<T extends BoundingType>`:表示T 应该是`BoundingType`的子类型（subtype)。T 和`BoundingType`可以是类，也可以是接口。
- 一个类型变量或通配符可以有多个限定，限定中至多有一个类。如果用一个类作为限定，它必须是限定列表中的第一个。`T extends Comparable & Serializable`

#### 3.泛型代码和虚拟机
虚拟机没有泛型类型对象——所有对象都属于普通类，因为JVM会擦除（erased) 类型变M, 并替换为第一个限定类型（无限定的变量用Object）。
##### 翻译泛型表达式
当程序调用泛型方法时， 如果擦除返回类型， 编译器插入强制类型转换。
```Java
Pair<Employee> buddies = ...;
Employee buddy = buddies.getFirst(); // 擦除 getFirst 的返回类型后将 返回 Object 类型。编译器自动插入 Employee 的强制类型转换，即 (Employee)buddies.getFirst();
```
当存取一个泛型域时也要插入强制类型转换。也会在结果字节码中插入强制类型转换。
##### 翻译泛型方法
- 类型擦除与多态发生了冲突。为了解决这个问题，编译器会在类中生成一个桥方法（bridge method)保持类的多态性。
- 在虚拟机中，用参数类型和返回类型确定一个方法。

#### 4.泛型的约束与局限性
大多数限制都是由类型擦除引起的。
- 运行时类型查询只适用于原始类型。`getClass` 方法总是返回原始类型。可以通过尝试强制类型转换来判断是哪种泛型类型。对于泛型类型不能使用`instanceof`。
- 不能创建参数化类型的数组：`new Pair<String>[10]; // Error`，但可以声明`Pair<String>[]`。可以声明通配类型的数组，然后进行类型转换：`Pair<String>[] table = (Pair<String>[]) new Pair<?>[10];`
- 如果需要收集参数化类型对象，只有一种安全而有效的方法：使用`ArrayList:ArrayList<Pair<String>()`
- 可以将对象传给可变参数的泛型类型：`T... ts`
- 可以使用@SafeVarargs 标注来消除创建泛型数组的有关限制.
- 不能实例化类型变量：不能有`new T(...，)newT[...] 或T.class` 这样的表达式。解决方法：1.让调用者提供一个构造器表达式，比如`String::new` 2.传统方法使用反射调用`Class.newInstance` 方法来构造泛型对象。
- 不能构造泛型数组，`T[] mm = new T[2];  // Error`。同上，有2种解决方式：1.传入数组构造器，比如`String[]::new` 2.传统方法使用反射+`Array.newlnstance()` 方法来构造泛型数组对象。
- 不能在静态域或方法中引用类型变量。
- 不能抛出或捕获泛型类的实例。
- 可以消除对受查异常的检查。

#### 5.泛型类型的继承规则
- 无论S 与T 有什么联系，通常`Pair<S>` 与`Pair<T>`没有什么联系。
- 永远可以将参数化类型转换为一个原始类型。如`Pair<Employee>` 是原始类型`Pair` 的一个子类型。
- 泛型类可以扩展或实现其他的泛型类。

#### 6.通配符类型
通配符类型中，允许类型参数变化。
`Pair<? extends Employee>`，类型`Pair<Manager>` 是`Pair<? extends Employee>` 的子类型。
##### 通配符的超类型限定
通配符限定与<font color=red>类型变量限定</font>十分类似，但是，还有一个附加的能力，即可以指定一个超类型限定（supertypebound)：`? super Manager`，这个通配符限制为Manager 的所有超类型。

直观地讲，带有超类型限定的通配符可以向泛型对象写人，带有子类型限定的通配符可以从泛型对象读取。

#### 7.反射和泛型
如果对象是泛型类的实例，关于泛型类型参数则得不到太多信息，因为它们会被擦除。通过反射，可以获得泛型类的方法的签名及返回值类型等信息。