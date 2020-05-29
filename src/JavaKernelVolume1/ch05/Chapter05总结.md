#### 1.超类、子类
- 调用父类的方法，使用关键字super.xxx()。super不是一个对象的引用，不能将super赋给另一个对象变量，它只是一个指示编译器调用超类方法的特殊关键字。
- 子类继承父类的所有成员(包括private成员)，在子类中可以增加域、增加方法或覆盖超类的方法，然而绝对不能删除继承的任何域和方法。
- 为了初始化父类的私有域，在子类构造器中必须使用super()调用父类构造器，且必须是子类构造器的第一条语句。若子类构造器没有显示调用，则默认调用父类无参构造函数（若无，则编译报错）。
- 一个对象变量可以指示多种实际类型(运行时类型)的现象被称为多态（polymorphism)。在运行时能够自动地选择调用哪个方法的现象称为动态绑定（dynamic binding）。
- 调用方法时，编译器看对象的声明类型来判断是否合法，而运行时根据对象实际类型去决定调用哪个方法。
- 在Java 中，子类数组的引用可以转换成超类数组的引用，而不需要采用强制类型转换，若子类数组由指向父类数组对象的变量指向，操作数组元素时则可能出现异常。所有数组都要牢记创建它们的元素类型，并负责监督仅将类型兼容的引用存储到数组中，避免将父类元素放入子类数组中。
##### 方法调用
x.f(param):假设x 的实际类型是D，它是C 类的子类。
0. 覆盖方法返回值兼容性与可见性：如果在子类中定义了一个与超类签名相同的方法，那么子类中的这个方法就覆盖了超类中的这个相同签名的方法。在覆盖方法时，一定要保证返回类型的兼容性。允许子类将覆盖方法的返回类型定义为原返回类型的子类型。同时，子类方法不能低于超类方法的可见性。
1. 静态与动态绑定：如果是private 方法、static 方法、final 方法或者构造器，那么编译器将可以准确地知道应该调用哪个方法，我们将这种调用方式称为静态绑定（static binding )。若调用的方法依赖于隐式参数的实际类型，并且在运行时实现动态绑定。当程序运行，并且采用动态绑定调用方法时，虚拟机一定调用与x 所引用对象的实际类型最合适的那个类的方法。
2. 方法表：虚拟机预先为每个类创建了一个方法表（ method table), 其中列出了所有方法(包括从父类继承的方法)的签名和实际调用的方法。这样一来，在真正调用方法的时候，虚拟机仅查找这个表就行了。如果调用super.f(param), 编译器将对隐式参数超类的方法表进行搜索。

动态绑定有一个非常重要的特性：无需对现存的代码进行修改，就可以对程序进行扩展。
##### final类和方法
- final类和方法：不允许扩展的类被称为final 类。类中的特定方法也可以被声明为final。如果这样做，子类就不能覆盖这个方法（final 类中的所有方法自动地成为final 方法）。
- final域：域也可以被声明为final。对于final 域来说，构造对象之后就不允许改变它们的值了。不过，如果将一个类声明为final，只有其中的方法自动地成为final,而不包括域。
- 内联调用：虚拟机中的即时编译器可以准确地知道类之间的继承关系，并能够检测出类中是否真正地存在覆盖给定的方法。如果方法很简短、被频繁调用且没有真正地被覆盖，那么即时编译器就会将这个方法进行内联处理。例如，内联调用e.getName( ) 将被替换为访问e.name 域。
- 强制类型转换：在进行类型转换之前，先查看一下是否能够成功地转换。这个过程简单地使用`instanceof` 操作符就可以实现:
```Java
if (staff[1] instanceof Manager){ // 如果x 为null, x instanceof C 不会产生异常，只是返回false。
boss = (Manager) staff[1];
}
```
##### 抽象类
1. 包含一个或多个抽象方法(只有定义，没有实现)的类本身必须被声明为抽象的，除了抽象方法之外，抽象类还可以包含具体数据和具体方法。抽象类可以无抽象方法，但是无法实例化。抽象方法一般是子类的通用方法，只是其具体内容需要由各个子类自己去实现。可以定义一个抽象类的对象变量，但是它只能引用非抽象子类的对象。
2. 建议尽量将通用的域和方法（不管是否是抽象的）放在超类（不管是否是抽象类）中。
##### protect成员
1. 最好将类中的域标记为private, 而方法标记为public。
2. protect域：在实际应用中，要谨慎使用protected 属性。假设需要将设计的类提供给其他程序员使用，而在这个类中设置了一些受保护域，由于其他程序员可以由这个类再派生出新类，并访问其中的受保护域。在这种情况下，如果需要对这个类的实现进行修改，就必须通知所有使用这个类的程序员。这违背了OOP 提倡的数据封装原则。
3. protect方法：受保护的方法更具有实际意义。如果需要限制某个方法的使用，就可以将它声明为protected。这表明子类（可能很熟悉祖先类）得到信任，可以正确地使用这个方法，而其他类则不行。

事实上，Java 中的受保护部分对所有子类及同一个包中的所有其他类都可见。

#### 2.Object类
不管是对象数组还是基本类型的数组都是Object 类的子类，即任何数组对象都可转为Object对象。数组对象之间若没有继承关系，则不能相互转换。
##### equals方法
1. ==：用于对象，比较的是存储地址。用于基本类型比较的是值，所以在equals中比较基本类型域直接用==。
2. 比较对象域：为了防备可能为null 的情况，需要使用Objects.equals 方法。如果两个参数都为null，Objects.equals(a，b) 调用将返回true ; 如果其中一个参数为null,则返回false; 否则，如果两个参数都不为null，则调用a.equals(b)：
```Java
return Objects.equals(name , other.name)
&& salary == other.sal ary
&& Object.equals(hireDay, other.hireDay) ;
```
3. 在子类中定义equals 方法时，必须首先调用超类的equals。如果检测失败，对象就不可能相等。如果超类中的域都相等，就需要比较子类中的实例域。
```Java
public class Manager extends Employee
{
    public boolean equals(Object otherObject)
    {
    if (!s叩erequals(otherObject)) return false;
    // super.equals checked that this and otherObject belong to the same class
    Manager other = (Manager) otherObject;
    return bonus == other.bonus;
    }

}
```
编写一个完美的equals 方法的建议：
1. 显式参数命名为otherObject, 稍后需要将它转换成另一个叫做other 的变量。
2. 检测this 与otherObject 是否引用同一个对象：`if (this = otherObject) return true;` 这条语句只是一个优化。实际上，这是一种经常采用的形式。因为计算这个等式要比一个一个地比较类中的域所付出的代价小得多。
3. 检测otherObject 是否为null, 如果为null, 返回false。这项检测是很必要的。`if (otherObject = null) return false;`
4. 比较this 与otherObject 是否属于同一个类。如果equals 的语义在每个子类中有所改
变，就使用getClass 检测：`if (getClass() != otherObject.getCIassO) return false;` 如果所有的子类都拥有统一的语义，就使用instanceof 检测：`if (!(otherObject instanceof ClassName)) return false;`
5. 将otherObject 转换为相应的类类型变量：`ClassName other = (ClassName) otherObject`
6. 现在开始对所有需要比较的域进行比较了。使用==比较基本类型域，使用equals 比较对象域。如果所有的域都匹配，就返回true; 否则返回false。
```Java
return fieldl == other.field
&& Objects.equa1s(fie1d2, otnher.field2)
&& ....;
```
**如果在子类中重新定义equals, 就要在其中包含调用super.equals(other)。<br/>注： equals(Object other)参数必须是Object才能覆盖父类的equals()。**
##### 相等测试与继承
- 如果子类能够拥有自己的相等概念，则对称性需求将强制采用getClass 进行检测。
- 如果由超类决定相等的概念，那么就可以使用 instanceof 进行检测，这样可以在不同子类的对象之间进行相等的比较。
#### hashCode()
- 如果重新定义equals 方法，就必须重新定义hashCode 方法，以便用户可以将对象插入到散列表中。
- hashCode 方法应该返回一个整型数值（也可以是负数，) 并合理地组合实例域的散列码,以便能够让各个不同的对象产生的散列码更加均匀。
- 求单个域的hashCode：对象域最好使用null 安全的方法Objects.hashCode。如果存在**数组类型的域，使用静态的Arrays.hashCode()** 计算一个散列码，这个散列码由数组元素的散列码组成。基本数据类型，使用静态方法Double.hashCode 来避免创建Double 对象：
```Java
public int hashCode()
{
    return 7 * Objects.hashCode(name)
        + 11 * Double.hashCode(salary)
        + 13 * Objects.hashCode(hireDay);
}
```
- 需要组合多个散列值时，可以调用ObjeCtS.hash 并提供多个参数。这个方法会对各个参数调用Objects.hashCode，并组合这些散列值:
```Java
public int hashCodeO
{
    return Objects,hash(name, salary, hireDay);
}
```
**注：equals 与hashCode 的定义必须一致，即用于比较或计算的域要一样：如果x.equals(y) 返回true, 那么x.hashCode( ) 就必须与y.hashCode( ) 具有相同的值。**<br/>

#### 3.ArrayList:泛型数组列表
ArrayList 是一个采用类型参数（type parameter)的泛型类（generic class )。
如果数组存储的元素数比较多，又经常需要在中间位置插入、删除元素，就应该考虑使用链表了。
- 使用toArray 方法将数组元素拷贝到一个数组中:
```Java
X[] a = new XPtst.sizeO];
list.toArray(a);
```

#### 4.对象包装器与自动装箱
对象包装器类是不可变的，即一旦构造了包装器，就不允许更改包装在其中的值。同时，对象包装器类还是final , 因此不能定义它们的子类。
在算术表达式中也能够自动地装箱和拆箱:
```Java
Integer n = 3;
n++;
/*
编译器将自动地插人一条对象拆箱的指令，然后进行自增计算，最后再将结果装箱。
等价于：
int tmp = n; // tmp = 3
tmp++;  // tpm =4
n = tmp; // n = 4
*/
```
- 比较时两个包装器对象时应使用equals 方法。<font color=red>但boolean、byte、 char 127，介于-128 ~ 127 之间的short 和int 被包装到固定的对象中，对这些范围内的包装器使用 == 比较，相当于对其基本类型使用==进行值比较。</font>
- 装箱和拆箱是编译器认可的，而不是虚拟机。编译器在生成类的字节码时，插人必要的方法调用。虚拟机只是执行这些字节码。

#### 5.枚举类
`public enuni Size { SMALL , MEDIUM, LARGE, EXTRAJARGE };`
实际上， 这个声明定义的类型是一个类， 它刚好有 4 个实例。在比较两个枚举类型的值时， 永远不需要调用 equals, 而直接使用“ = =” 就可以了。<br/>
可以在枚举类型中添加一些构造器、 方法和域。 当然， 构造器只是在构造枚举常量的时候被调用:
```Java
public enum Size
{
    (SMALLfS"), MEDIUMC'M"), LARGEfL"), EXTRA_LARGE("XL"); // 声明枚举常量，同时，传入参数调用构造函数
    private String abbreviation;
    private Size(String abbreviation) { this,abbreviation = abbreviation; }
    public String getAbbreviation() { return abbreviation; }
}
```
所有的枚举类型都是 Enum 类的子类。

#### 6.反射
反射是指在程序运行期间发现更多的类及其属性的能力。
1. Class类
在程序运行期间，Java 运行时系统始终为所有的对象维护一个被称为运行时的类型标识。
这个信息跟踪着每个对象所属的类。虚拟机利用运行时类型信息选择相应的方法执行。
保存这些信息的类被称为Class，Object 类中的getClass( ) 方法将会返回一个Class 类型的实例。
Class 类实际上是一个泛型类。
2. 获取Class对象的3种方式：
    1. obj.getClass():返回对象的实际类型类名。
    2. Class.forName(str)。无论何时使用这个方法，都应该提供一个异常处理器
    3. 如果T是任意的Java 类型（或void 关键字), T.class 将代表匹配的类对象
3. 一个Class 对象实际上表示的是一个类型，而这个类型未必一定是一种类。如int 不是类，但int.class 是一个Class 类型的对象。
4. 在启动时，包含main 方法的类被加载。它会加载所有需要的类。这些被加栽的类又要加载它们需要的类，以此类推。
5. 虚拟机为每个类型管理一个Class 对象。因此，可以利用==运算符实现两个类对象比较
的操作：`if (e.getClassO — Employee.class) ...`
6. 动态创建类实例：newlnstance 方法调用默认的构造器（没有参数的构造器）初始化新创建的对象。若要调用有参构造函数，必须使用`Constructor` 类中的newlnstance 方法。
```Java 
String s = "java.util .Random";
Object m = Cl ass.forName(s).newlnstance();
```
##### 利用反射分析类的能力
反射机制最重要的内容——检查类的结构。
Class 类中的getFields、getMethods 和getConstructors 方法将分别返回类提供的public修饰的 域、方法和构造器数组，其中包括超类的公有成员。Class 类的getDeclareFields、getDeclareMethods 和getDeclaredConstructors 方法将分别返回类中声明的全部域、方法和构造器，其中包括私有和受保护成员，<font color=red>但不包括超类的成员。</font>

##### 在运行时使用反射分析对象
在运行时查看数据域的实际内容。利用反射机制可以查看在编译时还不清楚的对象域。
1. 反射机制的默认行为受限于Java 的访问控制
2. setAccessible 方法是AccessibleObject 类中的一个方法，它是Field、Method 和Constructor
类的公共超类。
3. f.get(obj) 将返回一个对象，其值为obj 域的当前值。调用f.set(obj，value) 可以将obj 对象的f 域设置成新值。

###### 使用反射编写泛型数组代码
- 将一个Employee[]临时地转换成Object[] 数组，然后再把它转换回来是可以的，但一从开始就是Object[]的数组却永远不能转换成Employee[]数组。
- Array 类中的静态方法newInstance,它能够构造新数组。在调用它时必须提供两个参数，一个是数组的元素类型，一个是数组的长度：`Object newArray = Array.newlnstance(componentType, newLength) ;`
- 任意的数组对象，其直接父类都是Object。故int[] 可以被转换成Object，但不能转换成Object[]。

##### 调用任意方法
- 在Method 类中有一个invoke 方法，它允许调用包装在当前Method 对象中的方法：`Object invoke(Object obj, Object... args)`。第一个参数是隐式参数，其余的对象提供了显式参数。对于静态方法，第一个参数可以被忽略，即可以将它设置为null。因为重载，还必须提供想要的方法的参数类型。
- invoke 的参数和返回值必须是Object 类型的。这就意味着必须进行多次的类型转换。
- 使用反射获得方法指针的代码要比仅仅直接调用方法明显慢一些。
- <font color=red>特别要重申</font>：建议Java 开发者不要使用Method 对象的回调功能。使用接口进行回调会使得代码的执行速度更快，更易于维护。

#### 7.继承的设计技巧
1. 将公共操作和域放在超类
2. 不要使用受保护的域:在同一个包中的所有类都可以访问proteced 域，而不管它是否为这个类的子类。
3. 使用继承实现“ is-a” 关系
4. <font color=red>除非所有继承的方法都有意义，否则不要使用继承。</font>
5. 使用多态，而非类型信息。以下形式的代码都应该考虑使用多态性。使用多态方法或接口编写的代码比使用对多种类型进行检测的代码更加易于维护和扩展。
```Java
if (x is of type1)
    action1(x);
else if (x is of type)
    action2();
```
action1 与action2表示的是相同的概念吗？如果是相同的概念，就应该为这个概念定义一
个方法，并将其放置在两个类的超类或接口中，然后，就可以调用`X.action()`。





