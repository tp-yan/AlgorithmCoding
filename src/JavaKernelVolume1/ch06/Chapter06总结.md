#### 1.接口
- 接口不是类，而是对类的一组需求描述，用于被类实现。实现接口时，必须把方法声明为public; 否则，编译器将认为这个方法的访问属性是包可见性，即类的默认访问属性。
- 接口也是Class的实例。
- 接口修饰符可以是：`abstract interface Xxx`
- 接口中的方法都是`public abstract`。接口没有实例域，可以有常量(所有域被自动设为`public static final`)。
- 接口变量必须引用实现了接口的类对象。可以使用 `instanceof` 检查一个对象是否实现了某个特定的接口。
- 接口也可以被其他接口继承。
##### 1) 接口中的静态方法
在Java SE 8 中，允许在接口中增加静态方法。static方法必须被实现，static与abstract修饰符无法同用。
##### 2) 默认方法
可以为接口方法提供一个默认实现。必须用 default 修饰符标记这样一个方法。当接口中有多个抽象方法时，而实现接口的类只需要其中的一两个方法，就可以把接口中所有方法声明为默认方法，在类中只需覆盖要使用的方法即可，其他保持默认，例如接口`MouseListener`。<br/>
注：默认方法可以调用接口的任何其他方法。
```Java
public interface Comparable<T>
{
    default int compareTo(T other) { return 0; }
    // By default, all elements are the same
}
```
##### 3) 解决默认方法冲突
先在一个接口中将一个方法定义为默认方法，然后又在超类或另一个接口中定义了同样的方法，会发生什么情况？
1) 超类优先。如果超类提供了一个具体方法，同名而且有相同参数类型的默认方法会被忽略。
2) 接口冲突。如果一个超接口提供了一个默认方法，另一个接口提供了一个同名而且参数类型（不论是否是默认参数）相同的方法，程序员必须覆盖这个方法来解决冲突（只要用一个接口提供了实现，就会有冲突；若2个接口都没有默认方法，则不会冲突）。

<font color=red>警告：千万不要让一个默认方法重新定义Object 类中的某个方法。</font>

##### 4) Comparator<T>接口
```Java
public interface Comparators
{
    int compare(T first, T second);
}
```
##### 5) 对象克隆-clone方法
clone 方法是Object 的一个protected 方法，默认逐个域地进行拷贝。因为是protected方法，无法在类外直接调用。若对象域是可变对象，则必须重新定义clone 方法来建立一个深拷贝，同时克隆所有子对象。<br/>
Override clone方法：
1) 实现Cloneable 接口。
2) 重新定义clone 方法，并指定位public 访问修饰符。将返回值类型改为当前类类型。

Cloneable没有任何方法，覆盖的是Object的clone。这个接口只是作为一个标记(接口)。即使clone 的默认（浅拷贝）实现能够满足要求，还是需要实现Cloneable 接口，将clone
重新定义为public，再调用super.clone()。<br/>
深拷贝例子：
```Java
class Employee implements Cloneable
{// raise visibility level to public, change return type
    public Employee clone() throws CloneNotSupportedException
    {
        // call Object.clone()
        Employee cloned = (Employee) super.clone();
        // clone mutable fields
        cloned.hireDay = (Date) hireDay.clone();
        return cloned;
    }
}
```
注：所有数组类型都有一个public 的clone 方法，而不是protected: 可以用这个方法
建立一个新数组，包含原数组所有元素的副本。

#### 2.lambda表达式
lambda 表达式就是一个代码块。lambda 表达式总是会转换为一个函数式接口的实例（相当于实现了函数式接口唯一抽象方法的类的对象），但是最好把lambda 表达式看作是一个函数，而不是一个对象。
##### 1) 函数式接口
函数式接口必须有一个抽象方法，可以有其他非抽象方法。在Java 中，对lambda 表达式所能做的也只是能转换为函数式接口。
##### 2) 方法引用
有时，可能已有现成的方法可以完成你想要传递到其他代码的某个动作，如：`Timer t = new Timer(1000, event -> System.out.println(event));` 直接把println 方法传递到Timer 构造器更好：`Timer t = new Timer(1000, System.out::println);`<br/>
表达式`System.out::println` 是一个方法引用（method reference ), 它等价于lambda 表达式 `x -> System.out.println(x)`。<br/>
- 用`::`操作符分隔方法名与对象或类名。主要有3 种情况：
    1. object::instanceMethod
    2. Class::staticMethod
    3. Class::instanceMethod

    在前2 种情况中，方法引用等价于提供方法(作为)参数的lambda 表达式，如：`Math::pow` 等价于`(x，y) -> Math.pow(x, y)`和前面的`System.out::println`。对于第3 种情况，第1 个参数会成为方法的目标(即方法调用者)。例如，`String::compareToIgnoreCase` 等同于`(x, y) -> x.compareToIgnoreCase(y)` 
- 可以在方法引用中使用`this` 参数。例如，`this::equals` 等同于`x -> this.equals(x)`。使用`super` 也是合法的。
注：类似于lambda 表达式，方法引用不能独立存在，总是会转换为函数式接口的实例。
##### 3) 构造器引用
构造器引用与方法引用很类似，只不过方法名为new。例如，`Person::new` 是Person 构造器的一个引用。<br/>
`int[]::new`是一个构造器引用，它有一个参数：即数组的长度。这等价于lambda 表达式`x -> new int[x]`。<br/>
利用构造器引用解决构造泛型数组new T[n]：
```Java
// toArray 方法调用这个构造器来得到一个正确类型的数组。然后填充这个数组并返回
Person[] people = stream.toArray(Person[]::new); 
```
##### 4) 变量作用域
- lambda 表达式中捕获的变量必须实际上是最终变量( effectively final)。实际上的最终变量是指，这个变量初始化之后就不会再为它赋新值。
- 在一个lambda 表达式中使用this 关键字时，是指创建这个lambda 表达式的方法的this
参数。
##### 5) 接收(处理)lambda 表达式
- 要编写接收lambda 表达式的方法，首先需要选择（偶尔可能需要提供）一个函数式接口。一般使用Java API 中提供的最重要的函数式接口就够了。然后将函数式接口作为方法参数即可。
- 如果设计你自己的接口，其中只有一个抽象方法，可以用@FunctionalInterface 注解来标记这个接口。并不是必须使用注解根据定义，任何有一个抽象方法的接口都是函数式接口。

#### 3.内部类
使用内部类的原因：
- 内部类方法可以访问该类定义所在的作用域中的数据，包括私有的数据。
- 内部类可以对同一个包中的其他类隐藏起来。
- 当想要定义一个回调函数且不想编写大量代码时，使用匿名（anonymous) 内部类比较
便捷。

1. 内部类的对象总有一个隐式引用，它指向了创建它的外部类对象。编译器修改了所有的内部类的构造器，添加一个外围类引用的参数。
2. 只有内部类可以是私有类，而常规类只可以具有包可见性，或公有可见性。内部类可以是：public、protected、默认、private，私有内部类只能由外部类的方法实例化对象。在外围类的作用域之外，可以这样引用非private内部类：`OuterClass.InnerClass`。
3. 内部类中声明的所有静态域都必须是final。除了静态内部类外，内部类不能有static 方法(只能访问外围类的静态域和方法)。
4. 内部类是一种编译器现象，与虚拟机无关。编译器将会把内部类翻译成用$ ( 美元符号）分隔外部类名与内部类名的<font color=red>常规类文件</font>。在虚拟机中不存在私有类。

##### 1) 局部内部类
- 在外部类的方法中定义局部类。局部类不能用public 或private 访问说明符进行声明。它的作用域被限定在声明这个局部类的块中。
- 局部类不仅能够访问包含它们的外部类，还可以访问局部变量。不过，那些局部变量必须事实上为final。
- 编译器会检测局部类对局部变量的访问，为每一个变量建立相应的数据域，并将局部变量拷贝到内部类的构造器中，以便将这些数据域初始化为局部变量的副本。
- **当需要在局部类中修改局部变量时，应该将该变量放于一个数组中。**
##### 2) 匿名内部类
若只创建这个类的一个对象，就不必命名了：
```Java
new SuperType(construction parameters)
{
    inner class methods and data
}
```
SuperType既可以是接口也可以是类。匿名类不能有构造器，取而代之的是，将构造器参数传递给超类（superclass) 构造器。尤其是在内部类实现接口的时候，不能有任何构造参数。<br/>
<font color=red>注：Java 程序员习惯的做法是用匿名内部类实现事件监听器和其他回调。如今最好还是使用lambda 表达式。</font>

##### 3) 静态内部类
若只是为了把一个类隐藏在另外一个类的内部，并不需要内部类引用外围类对象。为此，可以将内部类声明为static, 以便取消产生的外围类对象引用。
- 只有内部类可以声明为 static。
- 在内部类不需要访问外围类对象的时候，应该使用静态内部类。
- 声明在接口中的内部类自动成为static 和public 类。

#### 4.代理
暂不懂

#### 其他
- 比较浮点数时，一定要使用Double.compareTo()，不要直接使用比较运算符。对于int值的比较，若担心计算溢出，则使用Integer.compare 方法。
- compareTO涉及不同类：如果子类之间的比较含义不一样，那就属于不同类对象的非法比较。每个compareTo 方法都应该在开始时进行下列检测(类似`equals`)：
`if (getClass() != other.getClass()) throw new ClassCastException();`<br/>
如果存在这样一种通用算法，它能够对两个不同的子类对象进行比较，则应该在超
类中提供一个compareTo 方法，并将这个方法声明为final。

