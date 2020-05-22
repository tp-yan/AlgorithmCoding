#### 1.Java集合框架
##### 队列Queue
- 队列通常有两种实现方式：一种是使用循环数组；另一种是使用链表。循环数组是一个有界集合，即容量有限。如果程序中要收集的对象数量没有上限，就最好使用链表来实现。
- 扩展`AbstractQueue` 类要比实现`Queue` 接口中的所有方法轻松得多。
##### Collection 接口
集合类的基本接口是`Collection` 接口。`iterator` 方法用于返回一个实现了`Iterator` 接口的对象，用于遍历集合对象。
##### 迭代器Iterator
- 如果想要査看集合中的所有元素，就请求一个迭代器，并在`hasNext` 返回true 时反复地调用`next` 方法。
- 用“`foreach`” 循环可以更加简练地表示同样的循环操作:
```Java
Collection<String> c = ...;
Iterator<String> iter = c.iterator()；
for (String element : c) // 编译器简单地将“ foreach” 循环翻译为带有迭代器的循环
{
    do something with element
}
```
- `foreach` 循环可以与任何实现了`Iterable` 接口的对象一起工作，`Collection` 接口扩展了`Iterable` 接口。因此，对于标准类库中的任何集合都可以使用`foreach`循环。
- 在Java SE 8 中，甚至不用写循环。`Iterator`可以调用`forEachRemaining` 方法并提供一个`lambda`表达式（它会处理一个元素）。将对迭代器的每一个元素调用这个`lambda` 表达式，直到再没有元素为止。`iterator.forEachRemaining(e -> do something with e);`
- Java 迭代器在执行查找操作的同时，迭代器的位置随之向前移动。应该将Java 迭代器认为是位于两个元素之间。`Iterator` 接口的`remove` 方法将会删除上次调用`next` 方法时返回的元素。如果调用`remove` 之前没有调用`next` 将是不合法的。
- 元素被访问的顺序取决于集合类型。
- 不应该去实现`Collection` 接口，而是继承类`AbstractCollection`由它提供基本方法，子类只需提供`iterator`和`size` 方法。
##### 集合框架中的接口
两个基本接口：`Collection 和 Map`。
 - List是一个有序集合，可以采用两种方式访问元素：使用迭代器访问，或者使用一个整数索引来访问。后一种方法称为随机访问（`random access`)，因为可以按任意顺序访问元素。
 - 标记接口`RandomAccess`不包含任何方法，可以用它来测试一个特定的集合是否支持高效的随机访问：`if (c instanceof RandomAccess)`。
 - `Set` 接口等同于`Collection` 接口，只是不允许有重复元素。

#### 2.具体的集合类
除了以Map 结尾的类之外，其他类都实现了Collection 接口，而以Map 结尾的类实现了Map 接口。
##### 链表`LinkedList`
链表是一个有序集合。在Java 程序设计语言中，所有链表实际上 <font color=red>都是双向链接的</font>，即每个结点还存放着指向前驱结点的引用。
- LinkedList.add 方法将对象添加到链表的尾部。其迭代器 ListIterator add方法可以在中间（当前迭代位置前，迭代器会保持当前位置）插入。
- ListIterator不能连续调用两次remove()，在remove()之前调用previous 就会将删除由侧元素，调用next就会将删除左侧元素。
- 为了**避免发生并发修改**的异常，可以有多个集合的迭代器，但只能有一个可读写，其他只能读。
- 可以使用Listlterator 类从前后两个方向遍历链表中的元素，并可以添加、删除元素。
- 链表不支持快速地随机访问。<font color=red>如果要查看链表中第n个元素，就必须从头开始，越过n-1个元素。</font>**每次査找一个元素都要从列表的头部重新开始搜索**。避免使用以整数索引表示链表中位置的所有方法，如：`for (int i = 0; i < list.size();i++) do something with list.get(i);`
如果需要对集合进行随机访问，就使用数组或ArrayList, 而不要使用链表。
##### 数组列表ArrayList
- ArrayList 封装了一个动态再分配的对象数组。
- 有两种访问元素的协议：一种是用迭代器，另一种是用get 和set 方法随机地访问每个元素。后者不适用于链表，但对数组却很有
用。
- Vector 类的所有方法都是同步的。可以由两个线程安全地访问一个Vector 对象。而ArrayList 方法不是同步的，因此，建议在不需要同步时使用ArrayList, 而不要使用Vector。
##### 散列集HashSet
可以快速地査找所需要的对象，这就是散列表（hash table)。
- 散列表为每个对象计算一个整数，称为散列码（hashcode。)散列码是由对象的实例域产生的一个整数。
- 在Java 中，散列表用链表数组实现。每个列表被称为桶（bucket)。要想査找表中对象的位置，就要先计算它的散列码，
然后与桶的总数取余，所得到的结果就是保存这个元素的桶的索引（所以HashTable可以快速确定是否包含指定对象）。

HashSet 类，就是基于散列表的集。
- 散列集迭代器将依次访问所有的桶。由于散列将元素分散在表的各个位置上，所以访问它们的顺序几乎是随机的。
- 警告：在更改集中的元素时要格外小心。如果元素的散列码发生了改变，元素在数据结构中的位置也会发生变化。
##### 树集TreeSet
树集是一个有序集合( sorted collection)。可以以任意顺序将元素插入到集合中。在对集合进行遍历时，每个值将自动地按照排序后的顺序呈现。
- 排序是用树结构完成的（当前实现使用的是红黑树（red-black tree)。
- 注：要使用树集，必须能够比较元素。这些元素必须实现Comparable 接口或者构造集时必须提供一个Comparator 
##### 队列与双端队列
在Java SE 6 中引人了`Deque 接口`，并由ArrayDeque(底层动态数组实现) 和LinkedList 类实现。这两个类都提供了双端队列，而且在必要时可以增加队列的长度。 
##### 优先级队列
- 优先级队列（priority queue) 中的元素可以按照任意的顺序插入，却总是按照排序的顺序进行检索。无论何时调用remove 方法，总会获得当前优先级队列中最小的元素。无论何时调用remove 方法，总会获得当前优先级队列中最小的元素。
- 优先级队列使用了一个优雅且高效的数据结构，称为堆（heap)(小顶堆)。堆是一个可以自我调整的二叉树。
- 使用优先级队列的典型示例是任务调度。每当启动一个新的任务时，都将优先级最高的任务从队列中删除（由于习惯上将1设为“最高” 优先级，所以会将最小的元素删除)。
- 与TreeSet 中的迭代不同，这里的迭代并不是按照元素的排列顺序访问的。而删除却总是删掉剩余元素中优先级数最小的那个元素。

#### 3.映射Map
散列映射对键进行散列，树映射用键的整体顺序对元素进行排序，并将其组织成搜索树。散列或比较函数只能作用于键。
- 要迭代处理映射的键和值，最容易的方法是使用forEach 方法。可以提供一个接收键和值的lambda 表达式：
```Java
scores.forEach((k, v) ->
    System.out.println("key=" + k + ", value:" + v));
```
##### 更新映射项
3种方式:
```Java
Map<String, Integer> counts = new HashMap<>();
String word = "Tom";
// (1)
counts.put(word, counts.getOrDefault(word, 0) + 1);
// (2)
counts.putIfAbsent(word, 0);
counts.put(word, counts.get(word) + 1);
// (3) 将把word 与1 关联，否则使用Integer::sum 函数组合原值和1 (也就是将原值与1 求和)。
counts.merge(word, 1, Integer::sum);
```
##### 映射视图
映射的视图（View )：实现了Collection 接口或某个子接口的对象。<br/>
有3 种视图：键集、值集合（不是一个集）以及键/ 值对集。
```Java
Set<K> keySet()
Collection<V> values()
Set<Map.Entry<K, V>> entrySet() // Map.Entry 接口
```
如果在这3种视图上调用迭代器的remove 方法，实际上会从映射中删除这个键和与它关联的值。不能通过视图增加元素。
##### 弱散列映射WeakHashMap
WeakHashMap 使用弱引用（weak references) 保存键。意思是，键对象使用一个‘弱引用’指向它。如果某个对象只能由WeakReference 引用，垃圾回收器仍然回收它，但要将引用这个对象的弱引用放入队列中。一个弱引用进人队列意味着这个键不再被他人使用，并且已经被收集起来。于是，WeakHashMap 将删除对应的条目。
##### 链接散列集与映射
- LinkedHashSet 和LinkedHashMap 类用来记住插入元素项的顺序。当条目插入到表中时，就会并入到双向链表中（条目之间互相指向）。
- LinkedHashMap还可以按元素访问顺序排序，而不是插入顺序。每次调用get 或put, 受到影响的条目将从当前的位置删除，并放到条目链表的尾部（即尾部存放的是最近访问元素）。`LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)`。accessOrder 参数为true 时表示访问顺序，为false 时表示插入顺序。
- 访问顺序对于实现高速缓存的“ 最近最少使用” 原则十分重要。可以构造LinkedHashMap 的子类，然后覆盖：`protected boolean removeEldestEntry(Map.Entry<K，V> eldest)`，在此方法中规定如何处理“最旧”的元素。
##### IdentifyHashMap
IdentifyHashMap的键不是使用其HashCode方法而是System.identifyHashCode，即Object类原始的hashCode()，根据对象的内存地址计算的。比较2个IdentifyHashMap对象时使用==而不是equals。

#### 4.视图与包装器
keySet 方法返回一个实现Set接口的类对象，这个类的方法对原映射进行操作。这种集合称为视图。<br/>
由于视图只是包装了接口而不是实际的集合对象，所以只能访问接口中定义的方法。
##### 轻量级集合包装器
- Arrays 类的静态方法asList 将返回一个**包装了普通Java 数组**的List 包装器。`List<Card> cardList = Arrays.asList(cardDeck);`它是一个视图对象，带有访问底层数组的get 和set 方法。改变数组大小的所有方法都会抛出一个Unsupported OperationException 异常。
- Collections 类包含很多实用方法，这些方法的参数和返回值都是集合。
- `Collections.nCopies(n, anObject)` 将返回一个实现了List 接口的不可修改的对象，
##### 不可修改的视图
Collections 还有几个方法，用于产生集合的不可修改视图。这些视图对现有集合增加了一个运行时的检查。如果发现试图对集合进行修改，就抛出一个异常，同时这个集合将保持未修改的状态。
##### 同步视图
类库的设计者**使用视图机制来确保常规集合的线程安全**，而不是实现线程安全的集合类。例如，Collections 类的静态synchronizedMap 方法可以将任何一个映射表转换成具有同步访问方法的Map:
```Java
Map<String, Employee〉map = Collections.synchronizedMap(new HashMap<String, Employee>()); //return a synchronized view of the specified map.
```
##### 受查视图
```Java
ArrayList<String> strings = new ArrayList<>()；
List<String> safestrings = Collections.checkedList(strings，String.class);
ArrayList rawList = safestrings;
rawList.add(new Date()); //视图的add 方法将检测插入的对象是否属于给定的类。 在运行到add方法时就报错
```

#### 5.算法
##### 排序与混排
对列表进行随机访问的效率很低，一般使用归并排序对列表进行高效的排序。然而，Java 程序设计语言并不是这样实现的。它直接将所有元素转入一个数组，对数组进行排序，然后，再将排序后的序列复制回列表。

传递给排序算法的列表：必须是可修改的，但不必是可以改变大小的。
- 如果列表支持set 方法，则是可修改的。
- 如果列表支持add 和remove 方法，则是可改变大小的。
```Java
List<Employee> staff = new LinkedList<>();
Collections.sort(staff); // 这个方法要求列表元素实现了Comparable 接口
// comparingDouble:传入一个double键提取函数
staff.sort(Comparator.comparingDouble(Employee::getSalary)); // 采用其他方式对列表进行排序
// 根据元素类型的compareTo 方法给定排序顺序，按照逆序对列表staff 进行排序
staff.sort(Comparator.reverseOrder()); // 返回一个比较器，比较器则返回 b.compareTo(a)。
// 同样地，按薪资降序
staff.sort(Comparator.comparingDouble(Employee::getSalary).reversed());
```
`Collections.shuffle`算法：
 如果提供的列表没有实现`RandomAccess` 接口，`shuffle` 方法将元素复制到数组中，然后打乱数组元素的顺序，最后再将打乱顺序后的元素复制回列表。
 ```Java
Collections.shuffle(numbers);
List<Integer> winningCombination = numbers.subList(0, 6);
Collections.sort(winningCombination);
```
##### 二分查找
Collections 类的`binarySearc`h 方法，必须是实现了`List接口`的有序集合。如果为binarySearch 算法提供一个链表，它将自动地变为线性查找。**只有采用随机访问，二分査找才有意义。**
##### 集合与数组的转换
```Java
// 1)Arrays.asList 包装器：数组->集合
String[] values = {"Li","Zhang","Wang"};
HashSet<String> names = new HashSet<>(Arrays.asList(values));
// 2) 从集合得到数组会更困难一些。当然，可以使用toArray 方法：
Object[] vs = names.toArray(); // 返回的数组是一个Object[] 数组，不能改变它的类型
// 必须使用toArray 方法的一个变体形式
String[] nvs = names.toArray(new String[0]); // 传入长度为0的数组，则自动创建一样长度的新数组；若传入指定大小的
String[] nvs2 = names.toArray(new String[10]); // 若传入指定大小的数组，则不新建数组
```

#### 6.遗留集合
##### Hashtable 类
Hashtable 类与HashMap 类的作用一样，实际上，它们拥有相同的接口。**与Vector 类的方法一样。Hashtable 的方法也是同步的。**如果需要并发访问，则要使用`ConcurrentHashMap`。
##### 属性映射（property map)
它有下面3 个特性：
- 键与值都是字符串。
- 表可以保存到一个文件中，也可以从文件中加载。
- 使用一个默认的辅助表。

实现属性映射的Java类称为`Properties`。属性映射通常用于程序的特殊配置选项。