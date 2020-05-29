#### 1.线程与进程
- 并发执行的进程数目并不是由CPU 数目制约的。操作系统将CPU 的时间片分配给每一个进程，给人并行处理的感觉。
- 每个进程拥有自己的一整套变量，而线程则共享数据。共享变量使线程之间的通信比进程之间的通信更有效、更容易。
- sleep 是Thread 类的静态方法，用于暂停当前线程的活动。
- 如果有很多任务，要为每个任务创建一个独立的线程所付出的代价太大了。可以使用线程池来解决这个问题。

#### 2.中断线程
- 没有可以强制线程终止的方法。然而，`interrupt()` 方法可以用来**请求终止线程**。被中断的线程可以决定如何响应中断。某些线程是如此重要以至于应该处理完异常后，继续执行，而不理会中断。
- 当对一个线程调用interrupt 方法时，线程的中断状态将被置位。每个线程都应该不时地检査这个标志，以判断线程是否被中断。更普遍的情况是，线程将简单地将中断作为一个终止的请求：
```Java
Runnable r = () -> {
try
{
    while (!Thread.currentThread().isInterrupted()) && more work to do)
    {
        do more work
    }
}
catch(InterruptedException e)
{
    // thread was interrupted during sleep or wait
}
finally
{
    cleanup,if required
}
// exiting the run method terminates the thread
};
```
- 当在一个被阻塞的线程（调用sleep 或wait) 上调用interrupt 方法时，阻塞调用将会被`Interrupted Exception` 异常中断

#### 3.线程状态
要确定一个线程的当前状态，可调用getState 方法。
- new Thread(r)，该线程还没有开始运行。这意味着它的状态是new。
- 一旦调用start 方法，线程处于runnable 状态。一个正在运行中的线程仍然处于可运行状态。
- 抢占式调度系统给每一个可运行线程一个时间片来执行任务。当时间片用完，操作系统剥夺该线程的运行权，并给另一个线程运行机会。现在所有的桌面以及服务器操作系统都使用抢占式调度。
- 阻塞：(1)线程试图获取一个内部的对象锁,而该锁被其他线程持有，则该线程进入阻塞状态。
(2)当线程等待另一个线程通知调度器一个条件时，它自己进入等待状态。在调用Object.wait 方法或Thread.join 方法，或者是等待java.util.concurrent 库中的Lock 或Condition 时，就会出现这种情况。

#### 4.线程属性
1. 线程优先级
- 默认情况下，一+线程继承它的父线程的优先级。可以用setPriority 方法提高或降低任何一个线程的优先级。
- 每当线程调度器有机会选择新线程时，它首先选择具有较高优先级的线程。
2. 守护线程
- `t.setDaemon(true);`这一方法必须在线程启动之前调用。
- 守护线程的唯一用是为其他线程提供服务。当只剩下守护线程时，虚拟机就退出了。
- 守护线程应该永远不去访问固有资源，如文件、数据库，因为它会在任何时候甚至在一个操作的中间发生中断。
3. 未捕获异常处理器
- 线程的run 方法不能抛出任何受查异常，但是，非受査异常会导致线程终止。就在线程死亡之前，异常被传递到一个用于未捕获异常的处理器。
- 可以用setUncaughtExceptionHandler 方法为任何线程安装一个处理器。也可以用Thread类的静态方法`setDefaultUncaughtExceptionHandler` 为所有线程安装一个默认的处理器。

#### 5.同步
同步：即当多个线程访问同一段锁保护的代码时，只能有一个线程执行该段代码，其他线程只能阻塞<font color=red>一直等待</font>锁释放后申请到锁才能继续执行。
##### 锁对象
有两种机制防止代码块受并发访问的干扰:
1. Java SE 5.0 引入的`ReentrantLock` 类 
2. `synchronized` 关键字自动提供一个锁以及相关的“条件”

用`ReentrantLock` 保护代码块的基本结构如下：
```Java
/*一旦一个线程封锁了锁对象，其他任何线程都无法通过lock 语句。当其他线程调用lock 时，它们被阻塞，
直到第一个线程释放锁对象。*/
myLock.lock(); // a ReentrantLock object
try // 如果使用锁，就不能使用带资源的try 语句。
{
    critical section
}
finally
{/* 警告：把解锁操作括在finally 子句之内是至关重要的。如果在临界区的代码抛出异常，
锁必须被释放。否则，其他线程将永远阻塞。*/
    myLock.unlock(); // make sure the lock is unlocked even if an exception is thrown
}
```
- 锁是可重入的，线程可以重复地获得已经持有的锁。锁保持一个持有计数（hold count ) 来跟踪对lock 方法的嵌套调用。线程在每一次调用lock 都要调用unlock 来释放锁。被一个锁保护的代码可以调用另一个使用相同的锁的方法。
- **警告：要留心临界区中的代码，不要因为异常的抛出而跳出临界区。如果在临界区代码结束之前抛出了异常，finally 子句将释放锁，但会使对象可能处于一种受损状态。**
##### 条件对象
通常，线程进入临界区，却发现在某一条件满足之后它才能执行。要使用一个**条件对象**来管理那些已经获得了一个锁但是却不能做有用工作的线程。
- 一个锁对象可以有一个或多个相关的条件对象。你可以用newCondition 方法获得一个条件对象。
- 等待获得锁的线程和调用await 方法的线程存在本质上的不同。一旦一个线程调用await方法，它进人该条件的等待集。当锁可用时，该线程不能马上解除阻塞。相反，**它处于阻塞状态，直到另一个线程调用同一条件上的signalAll 方法时为止。**
- signalAll重新激活因为这一条件而等待的所有线程。**signalAll 方法仅仅是通知正在等待的线程**：此时有可能已经满足条件，<font color=red>值得再次去检测该条件</font>。通常，对await 的调用应该在如下形式的循环体中：
    ```Java
    while ( !(ok to proceed) )
        condition.await();
    ```
- signal则是随机解除等待集中某个线程的阻塞状态。
##### synchronized 关键字
Java 语言内部的机制。从1.0 版开始，**Java中的每一个对象都有一个内部锁**。如果一个方法用synchronized 关键字声明，那么对象的锁将保护整个方法。要调用该方法和其他synchronized 声明的方法，线程必须获得内部的对象锁。
- 内部对象锁只有一个相关条件。`wait` 方法添加一个线程到等待集中，`notifyAll/notify` 方法解除等待线程的阻塞状态。
- 类对象锁：将静态方法声明为synchronized 也是合法的。调用这种方法，该方法获得相关的类对象的内部锁(如**Bank.class对象**的锁被锁住)。没有其他线程可以调用同一个类的这个或任何其他的同步静态方法。
- 最好既不使用`Lock/Condition `也不使用`synchronized `关键字。在许多情况下你可以使用`java.util.concurrent `包中的一种机制，它会为你处理所有的加锁。
- 如果synchronized 关键字适合你的程序，那么请尽量使用它，这样可以减少编写的代码数量，减少出错的几率。
##### synchronized块
以下代码获得对象`Obj` 的(内部)锁:
```Java
synchronized (obj) // this is the syntax for a synchronized block
{
    critical section
}
```
有时程序员**使用一个对象的锁**来实现额外的原子操作，实际上称为客户端锁定（client side locking)，客户端锁定是非常脆弱的，通常不推荐使用。
##### Volatile 域
volatile让变量每次在使用的时候，都从主存中读取，而不是从各个线程的“工作内存”。volatile遵循了happens-before原则：若volatile变量的写操作发生在另一个线程B读之前，则线程B读到的一定是修改后的新值！
- <font color=red>Volatile使变量具有内存可见性，但不能提供原子性。</font>例如，方法`public void flipDone()){ done = !done; }`不能确保翻转域中的值。不能保证读取、翻转和写入不被中断。
- Volatile不具原子性：当修改值从某个线程工作区写到主存时，并没有检查主存中的本体是否还是原来的值，即是否和工作内存中的副本一样。有可能其他线程已经修改了并更新到主体，此时就抹去了该新值。
- 如果一个域只有赋值和读取操作那么就应该声明为Volatile。
- 补充：引用类型的读写(赋值)都是原子性的。对于基本类型的读写(赋值)都是原子性的，除了long和double，因为JMM一次只能读32位，所以64位必须分2次读，在多线程中也就有问题。但使用volatile的long和double，读写都是原子性的。
##### 原子性
`java.util.concurrent.atomic` 包中有很多类使用了很高效的机器级指令**而不是使用锁**来保证其他操作的原子性。
- `Atomiclnteger` 类提供了方法incrementAndGet 和decrementAndGet, 它们分别以原子方式（通过CAS原理(也可以理解为乐观锁)实现）将一个整数自增或自减。
- 希望完成更复杂的更新，就必须使用compareAndSet 方法。实际上，应当在一个循环中计算新值和使用`compareAndSet`:
    ```Java
    do {
        oldValue = largest.getO ;
        newValue = Math ,max (oldValue, observed) ;
    } while (llargest.compareAndSet (oldValue, newValue)); //如果另一个线程也在更新largest，就可能阻止这个线程更新。这样一来，compareAndSet会返回false, 而不会设置新值。 
    ```
- 在Java SE 8 中，可以提供一个lambda 表达式更新变量:
    ```Java
    largest.updateAndGet (x -> Math.max(x, observed));
    或
    largest.accumulateAndCet(observed , Math::max);
    // 还有getAndUpdate 和getAndAccumulate 方法可以返回原值。
    ```
- 如果有大量线程要访问相同的原子值，性能会大幅下降。Java SE 8 提供了`LongAdder` 和`LongAccumulator` 类来解决这个问题。
    - LongAdder 包括多个变量（加数)其总和为当前值。可以有多个线程更新不同的加数，线程个数增加时会自动提供新的加数。调用increment 让计数器自增，或者调用add 来增加一个量，或者调用sum 来获取总和。
    - LongAccumulator 将这种思想推广到**任意的累加操作**。要加人新的值，可以调用accumulate。调用get 来获得当前值。下面的代码可以得到与LongAdder 同样的效果：
        ```Java
        LongAccumulator adder = new LongAccumulator(Long::sum,0); // 指定累加操作，和加数初始值
        // In some thread...
        adder.accumulate(value);
        /* 调用accumulate 并提供值v 时，其中一个变量会以原子方式更新为 ai = a op v, 这里op是中缀形式的累加操作。
        在我们这个例子中，调用accumulate 会对某个i 计算 ai = ai + v。
        get 的结果是a1 op q2 op . . . op an 在我们的例子中，这就是累加器的总和：a1 + a2 + … +an
        */
        ```
- 如果认为可能存在大量竞争，只需要使用LongAdder 而不是AtomicLong。

###### 线程局部变量
ThreadLocal 辅助类为各个线程提供各自的实例。要为每个线程构造一个实例，可以使用以下代码：
```Java
public static final ThreadLocal<SimpleDateFormat> dateFormat =
ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
// 要访问具体的格式化方法，可以调用：
String dateStamp = dateFormat.get().format(new Date());
// 在一个给定线程中首次调用get 时，会调用initialValue 方法（可通过withInitial方法设定）。在此之后，get 方法会返回属于当前线程的那个实例。
```
- java..util.Rand0m 类是线程安全的。但是如果多个线程需要等待一个共享的随机数生成器，这会很低效。`ThreadLocalRandom.current()` 调用会返回特定于当前线程的Random 类实例:
    ```Java
    int random = ThreadLocalRandom.current().nextlnt(upperBound);
    ```
##### 锁测试与超时
`tryLock` 方法试图申请一个锁，在成功获得锁后返回true, 否则，立即返回false, 而且线程可以立即离开去做其他事情：
```Java
if (myLock.tryLock())
{// now the thread owns the lock
    try { . . . }
    finally { myLock.unlock(); }
}
else // do something else
```
- 调用`tryLock`时可以使用超时参数：`if (myLock.tryLock(100, TineUnit.MILLISECONDS)) . . .`
- lock 方法不能被中断。如果一个线程在等待获得一个锁时被中断，中断线程在获得锁之前一直处于阻塞状态。如果出现死锁，那么，lock 方法就无法终止。
- 如果调用带有用超时参数的`tryLock`, 那么如果线程在等待期间被中断，将抛出`InterruptedException` 异常。这是一个非常有用的特性，因为允许程序打破死锁。
##### 读/写锁
`java.util.concurrent.locks` 包定义了两个锁类：
1. `ReentrantLock` 类 
2. `ReentrantReadWriteLock` 类：适合很多线程从一个数据结构读取数据而很少线程修改其中数据的话。

下面是使用读/ 写锁的必要步骤：
```Java
// 1) 构造一个ReentrantReadWriteLock 对象：
private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock():
// 2) 抽取读锁和写锁：
private Lock readLock = rwl . readLock() ;
private Lock writeLock = rwl .writeLock();
/// 3) 对所有的获取方法加读锁：
public double getTotalBalance()
{
    readLock.lock();
    try { . . . }
    finally { readLock.unlock(); }
}
// 4) 对所有的修改方法加写锁：
public void transfer(. . .)
{
    writeLock.lock();
    try { . . . }
    finally { writeLock.unlock(); }
}
```

#### 6.阻塞队列
对于许多线程问题，可以通过使用一个或多个队列以优雅且安全的方式将其形式化。生产者线程向队列插入元素，消费者线程则取出它们。使用队列，可以安全地从一个线程向另一个线程传递数据。
- 当试图向队列添加元素而队列已满，或是想从队列移出元素而队列为空的时候，阻塞队列（blocking queue ) 导致线程阻塞。
- 如果将队列当作线程管理工具来使用，将要用到put 和take 方法。如果队列满，则put 方法阻塞；如果队列空，则take 方法阻塞。
- ` java.util.concurrent` 包中的几种阻塞队列：
    + 无上界`LinkedBlockingQueue`
    + `LinkedBlockingDeque` 是前者的一个双端版本
    + 基于数组的`ArrayBlockingQueue` 在构造时需要指定容量、没有容量上限的
    + `PriorityBlockingQueue` 是一个带优先级的队列（**用堆实现**），而不是先进先出队列，元素按照它们的优先级顺序被移出。
    + `TranSferQueue` 接口，允许生产者线程等待，直到消费者准备就绪可以接收一个元素。`inkedTransferQueue` 类实现了这个接口。

#### 7.线程安全的集合
如果**多线程要并发地修改一个数据结构**，例如散列表，那么**很容易会破坏这个数据结构**。可以通过提供锁来保护共享数据结构，但是选择线程安全的实现作为替代可能更容易些。
##### 高效的映射、集和队列
java.util.concurrent 包提供了映射、有序集和队列的高效实现：`ConcurrentHashMap、
ConcurrentSkipListMap、ConcurrentSkipListSet 和ConcurrentLinkedQueue`。
- 确定这些集合当前的大小通常需要遍历。
- 对于一个包含超过20 亿条目的映射，JavaSE 8 引入了一个`mappingCount` 方法可以把大小作为long 返回。
- 集合返回弱一致性（weakly consistent) 的迭代器。这意味着迭代器不一定能反映出它们被构造之后的所有的修改，即在迭代过程中，若其他线程修改了集合，这个迭代器不会报错。而java.util 包中的迭代器就会抛出异常。
- 在JavaSE 8 中，**并发散列映射将桶组织为树，而不是列表**，键类型实现了Comparable, 从而可以保证性能为0(logN)
##### 映射条目的原子更新
多个线程修改ConcurrentHashMap时，只能保证这个数据结构不会被破坏，而不能保证内容的正确性。要使得多线程修改的内容的正确性，一是使用原子操作，二是还是使用锁来保护操作序列。
- 使用传统原子操作比如`replace`，它会以原子方式用一个新值替换原值，前提是之前没有其他线程把原值替换为其他值。必须一直这么做，直到replace 成功：
    ```Java
    do {
            oldValue = counter.get(word);
            newValue = oldValue == null ? 1 : oldValue + 1;
        } while (!counter.replace(word, oldValue, newValue)); // replace与AtomicLong的compareAndSet一样是原子性的。
    ```
- 将value改为保证原子性操作的类，比如AtomicLong、LongAdder等：
    ```Java
    ConcurrentHashMap<String，AtomicLong>
    ConcurrentHashMap<String,LongAdder>
    map.putlfAbsent(word, new LongAdder());
    map.get(word).increment();
    map.putlfAbsent(word, new LongAdder()).increment();
    ```
- Java SE 8 提供了一些可以更方便地完成原子更新的方法:
    + 调用compute 方法时可以提供一个键和一个计算新值的函数:
        ```Java
        counter.compute(word, (k, v) -> v == null ? 1 : v + 1);
        ```
    + merge方法有一个参数表示键不存在时使用的初始值。否则，就会调用你提供的函数来结合原值与初始值（与compute不同，这个函数不处理键）。merge方法适合解决：首次增加一个键时需要做些特殊的处理：
        ```Java
        counter.merge(word, 1L, (existingValue, nValue) -> existingValue + nValue);
        counter.merge(word, 1L, Long::sum);
        ```
- ConcurrentHashMap 中不允许有null 值。
##### 对并发散列映射的批操作
批操作会遍历映射，处理遍历过程中找到的元素。
```Java
 // (1) search：为每个键或值提供一个函数，直到函数生成一个非null 的结果。然后搜索止，返回这个函数的结果。
 long threshold = 2;
 // 找出第一个出现次数超过1000 次的单词。需要搜索键和值：
 String result = counter.search(threshold, (k, v) -> v > 1000 ? k : null); // 元素多于threshold时，采用多线程处理
 // (2) forEach
 // a.第一个只为各个映射条目提供一个消费者函数，例如：
 counter.forEach(threshold, (k, v) -> System.out.println(k + " -> " + v));
 // b.第二种形式还有一个转换器函数，这个函数要先提供，其结果会传递到消费者。
 // 转换器可以用作为一个过滤器。只要转换器返回null , 这个值就会被跳过。
 // 下面只打印有大值(> 1000)的条目：
 counter.forEach(threshold, (k, v) -> v > 1000 ? k + " ->" + v : null, Systemout::println);
 // (3) reduce 操作用一个累加函数组合其输入
 Long sum = counter.reduceValues(threshold, Long::sum);
 // 与forEach 类似，也可以提供一个转换器函数.可以如下计算最长的键的长度：
 Integer maxLength = counter.reduceKeys(threshold,
         String::length, // 转换器，处理结果传给Integer::max
         Integer::max); // 2元操作函数。
 // 统计多少个条目的值> 1000:
 Long count = counter.reduceValues(threshold,
         v -> v > 1000 ? 1L : null,
         Long::sum); // 如果只有一个元素，则返回其转换结果，不会应用累加器。
```
##### 并行数组算法
在Java SE 8 中，Arrays 类提供了大量**并行化操作**。
```Java
// (1)静态Arrays.parallelSort 方法可以对一个基本类型值或对象的数组排序。
// 从文件直接读入字符串
String contents = new String(Files.readAllBytes(Paths.get("D:\\Project\\JavaProjects\\AlgorithmCoding\\src\\JavaKernelVolume1\\ch14\\cocurrentSet\\alice.txt")), StandardCharsets.UTF_8);
String[] words = contents.split("[\\P{L}]+"); // 分割单词
Arrays.parallelSort(words);
System.out.println(Arrays.toString(words));
// 对对象排序时，可以提供一个Comparator
Arrays.parallelSort(words, Comparator.comparing(String::length));
System.out.println(Arrays.toString(words));
// (2) parallelSetAll 方法会用由一个函数计算得到的值填充一个数组。这个函数接收元素索引，然后计算相应位置上的值。
int[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
Arrays.parallelSetAll(values, index -> index * values[index]);
System.out.println(Arrays.toString(values));
// (3) parallelPrefix 方法，它会用对应一个给定结合操作的前缀的累加结果替换各个数元素。
// 考虑数组[1，2, 3, 4, . . .] 和 x 操作。执行后数组将包含：
// [1, 1x 2, 1x 2 x 3, 1 x 2 x 3 x 4, . . .]
int[] vs = {1, 2, 3, 4, 5};
Arrays.parallelPrefix(vs, (x, y) -> x * y);
```

#### 8.Callable 与Future
Runnable 封装一个异步运行的任务，可以把它想象成为一个没有参数和返回值的异步方法。Callable 与Runnable 类似，但是有返回值。
- Callable 接口的参数化类型指示方法返回类型；
- Future 保存异步计算的结果。可以启动一个计算，将Future 对象交给某个线程，Future 对象的所有者在结果计算好之后就可以获得它。
    + get 方法的调用被阻塞，直到计算完成。
    + cancel 方法取消该计算。如果计算还没有开始，它被取消且不再开始。如果计算处于运行之中，那么若mayInterrupt 参数为true, 它就被中断。

FutureTask 包装器是一种非常便利的机制，**可将Callable 转换成Future 和Runnable, 它同时实现二者的接口**。如：
```Java
Callable<Integer> myComputation = . . .;
FutureTask<Integer> task = new FutureTask<Integer>(myComputation);
Thread t = new Thread(task); // it's a Runnable
t.start()；
Integer result = task.get(); // it's a Future
```

#### 9.执行器Executor
执行器（Executor) 类有许多静态工厂方法用来构建线程池。
- 如果程序中创建了大量的生命期很短的线程，应该使用线程池（thread pool )。
- 将Runnable 对象交给线程池，就会有一个线程调用run 方法。当run 方法退出时，线程不会死亡，而是在池中准备为下一个请求提供服务。
##### 线程池
- `newCachedThreadPool`方法构建了一个线程池，对于每个任务，如果有空闲线程可用，立即让它执行任务，如果没有可用的空闲线程，则创建一个新线程。
- `newFixedThreadPool`方法构建一个具有固定大小的线程池。如果提交的任务数多于空闲的线程数，那么把得不到服务的任务放置到队列中，当其他任务完成以后再运行它们。
- `newSingleThreadExecutor` 是一个退化了的大小为1 的线程池：由一个线程执行提交的任务，一个接着一个。

这3 个方法返回实现了`ExecutorService` 接口的`ThreadPoolExecutor` 类的对象。可以将一个Runnable 对象或Callable 对象提交给ExecutorService:
```Java
Future<?> submit(Runnable task) // 可以使用这样一个对象来调用isDone、cancel 或isCancelled。但是，get 方法在完成的时候只是简单地返回null.
Future<T> submit(Runnable task, T result) // Future 的get 方法在完成的时候返回指定的result 对象。
Future<T> submit(Callable<T> task) // 返回的Future 对象将在计算结果准备好的时候得到它。
```
调用submit 时，会得到一个Future 对象，可用来查询该任务的状态。

当用完一个线程池的时候，调用`shutdown`。该方法启动该池的关闭序列。**被关闭的执行器不再接受新的任务。当所有任务都完成以后，线程池中的线程死亡**。另一种方法是调用`shutdownNow`，该池取消尚未开始的所有任务并试图中断正在运行的线程。

使用连接池时应该做的事：
1. 调用Executors 类中静态的方法newCachedThreadPool 或newFixedThreadPool。
2. 调用submit 提交Runnable 或Callable 对象。
3. 如果想要取消一个任务，或如果提交Callable 对象，那就要保存好返回的Future对象。
4. 当不再提交任何任务时，调用shutdown。

##### 预定(延迟)执行
`ScheduledExecutorService` 接口具有为预定执行（Scheduled Execution) 或重复执行任务而设计的方法。它是一种允许使用线程池机制的`java.util.Timer` 的泛化。可以预定Runnable 或Callable 在初始的延迟之后只运行一次。也可以预定一个Runnable对象周期性地运行。
##### 控制任务组
使用Executor有更有实际意义的原因，控制一组相关任务。
invokeAny 方法提交所有对象到一个Callable 对象的集合中，并返回某个已经完成了的任务的结果。无法知道返回的究竟是哪个任务的结果：
```Java
List<Callab1e<T> tasks = . . .;
List<Future<T> results = executor.invokeAll (tasks):
for (Future<T> result : results)
    processFurther(result.get())); // 这个方法的缺点是如果第一个任务恰巧花去了很多时间，则可能不得不进行等待。
```

**将结果按可获得的顺序保存**起来更有实际意义。可以用`ExecutorCompletionService` 来进行排列。
用常规的方法获得一个执行器。然后，构建一个ExecutorCompletionService，提交任务给完成服务（completion service。)该服务管理Future 对象的阻塞队列，其中包含已经提交的任务的执行结果（当这些结果成为可用时）：
```Java
ExecutorCompletionService<T> service = new ExecutorCompletionService<>(executor):
for (Callable<T> task : tasks) service.submit(task); // 提交一个任务给底层的执行器。
for (int i = 0; i < tasks.size();i++)
    processFurther(service.take().get()); // take：移除下一个已完成的结果，如果没有任何已完成的结果可用则阻塞。
```
##### Fork-Join框架
在后台，fork-join 框架使用了一种有效的智能方法来平衡可用线程的工作负载，这种方法称为工作密取（work stealing)。每个工作线程都有一个双端队列 (deque) 来完成任务。一个工作线程将子任务压入其双端队列的队头。（只有一个线程可以访问队头，所以不需要加锁）一个工作线程空闲时，它会从另一个双端队列的队尾“密取” 一个任务。

假设有一个处理任务，它可以很自然地分解为子任务：
```Java
if (problemSize < threshold)
    solve problem directly
else{
    break problem into subproblems
    recursively solve each subproblem
    combine the results
}
```
要采用框架可用的一种方式完成这种递归计算，需要提供一个扩展`RecursiveTask()` 的类（如果计算会生成一个类型为T 的结果）或者提供一个扩展`RecursiveAction` 的类（如果不生成任何结果)。再覆盖`compute`方法来生成并调用子任务，然后合并其结果：
```Java
// 代码结构跟普通递归一样，但这里是使用多线程来并发处理子任务。
// 父线程负责将任务划分为多个子任务，把每个子任务交给一个子线程(并发执行)，等所有子线程都完成后，父线程再汇总结果
class Counter extends RecursiveTask<Integer>
{
    protected Integer compute()
    {
        if (to - from < THRESHOLD)
        {
            solve problem directly
        }
        else
        {
            int mid = (from + to) / 2;
            Counter first = new Counter(va1ues, from, mid, filter);
            Counter second = new Counter(va1ues, mid, to, filter);
            // invokeAll 方法接收到很多任务并阻塞，直到所有这些任务都已经完成。
            invokeAll (first, second);
            // join 方法将生成结果。对每个子任务应用了join, 并返回其总和。
            return first.join() + second.join();
        }
    }
}
```
#### 10.同步器(线程控制)
java.util.concurrent 包包含了几个能帮助人们管理相互合作的线程集的类。
##### 倒计时门栓
一个倒计时门栓（`CountDownLatch`) 让一个线程集等待直到计数变为0。**倒计时门栓是一次性的。一旦计数为0, 就不能再重用了。**
##### 障栅
`CyclicBarrier` 类实现了一个集结点（rendezvous) 称为障栅（barrier)。当一个线程完成了它的那部分任务后，我们让它运行到障栅处。一旦所有的线程都到达了这个障栅，障栅就撤销，线程就可以继续运行。
```Java
// 首先，构造一个障栅，并给出参与的线程数：
CyclicBarrier barrier = new CydicBarrier(nthreads);
// 每一个线程做一些工作，完成后在障栅上调用await :
public void run()
{
    doWork();
    bamer.await(); //可选的超时参数： barrier.await(100, TimeUnit.MILLISECONDS); 
// 如果任何一个在障栅上等待的线程离开了障栅，那么障栅就被破坏了。在这种情况下，所有其他线程的await 方法抛出BrokenBarrierException 异常。那些已经在等待的线程立即终止await 的调用。
```
可以提供一个可选的障栅动作（barrier action), 当所有线程到达障栅的时候就会执行这一动作：
```Java
Runnable barrierAction = ...; // 该动作可以收集那些单个线程的运行结果
CyclicBarrier barrier = new CyclicBarrier(nthreads, barrierAction);
```
障栅被称为是循环的（cyclic), 因为可以在所有等待线程被释放后被重用。

##### 同步队列
同步队列是一种将生产者与消费者线程配对的机制。当一个线程调用`SynchronousQueue`的put 方法时，它会阻塞直到另一个线程调用take 方法为止，反之亦然。**数据仅仅沿一个方向传递，从生产者到消费者**。

#### 11.线程与Swing
- 除了事件分配线程(Event-Dispatch-Thread)，不要在任何线程(包括main线程)中接触Swing 组件。
- 应该将Swing 代码放置到实现Runnable 接口的类的run 方法中。然后，创建该类的一个对象，将其传递给`EventQueue` 类的静态的`invokeLater` 方法和`invokeAndWait` 方法：
    ```Java
    EventQueue.invokeLater(()-> {// 这里使用lambda表达式生成一个匿名内部类对象
    label.setText(percentage + "% complete");
    });
    ```
    当事件放入事件队列时，**invokeLater 方法立即返回，而run 方法被异步执行**。invokeAndWait 方法等待直到run 方法确实被执行过为止。
    
    **这两种方法都是在事件分配线程中执行run 方法**。<font color=red>没有新的线程被创建</font>。传递给invokeLater的run方法中不应该有花费很长时间的动作，而应该开启新线程处理，在新线程中再调用`EventQueue.invokeLater()`将结果传给事件分配线程，让其完成组件更新。

##### 使用Swing 工作线程(SwingWorker)
与Android的`AsyncTask`异步任务基本一样。<br/>
每当要在SwingWorker线程中做一些工作时，构建一个新的SwingWorker（**每一个SwingWorker对象只能被使用一次**)，然后调用execute 方法启动后台任务。

- `SwingWorke<T,V>` (产生类型为T 的结果以及类型为V 的进度数据)实现了`Future<T>`
- 可以通过`Future` 接口的get 方法获得`doInBackground()`返回结果。由于get 方法阻塞直到结果成为可用，因此不要在调用execute 之后马上调用它，一般在`done` 方法调用get。
- 要取消正在进行的工作，使用Future 接口的`cancel` 方法。
- 为了提高效率，对`publish` 的多次调用，可转变成对`process` 的一次调用成批处理：`process` 方法**接收一个包含所有中间结果的列表<V>**。
#####  单一线程规则
在Swing 程序中，main 方法的生命周期是很短的。它在事件分配线程中规划用户界面的构造然后退出。<br/>
不应该在main线程中执行如下代码： 
```Java
public static void main(String[] args)
{
   new MainFrame().setVisible(true);
}
// 应该调用EventQueue.invokeLater
public static void main(String[] args)
{
    EventQueue.invokeLater(()->{
      new MainFrame().setVisible(true);
    });
}
```

当运行一个 Swing 程序时，会自动创建三个线程。
1. 主线程，负责执行main 方法，主线程也不应该与Swing组件进行交互，应该使用`EventQueue.invokeLater`去初始化UI界面。
2. `toolkit`线程，负责捕捉系统事件，比如键盘、鼠标移动等，程序员不会有任何代码在这个线程上执行。Toolkit线程的作用是把自己捕获的事件传递给事件派发线程。
3. 事件派发线程EDT接收来自 toolkit 线程的事件，并且将这些事件组织成一个队列，**EDT的工作内容就是将这个队列中的事件按照顺序派发给相应的事件监听器，并且调用事件监听器中的回调函数，这也意味着，所有的事件处理代码都是在EDT而不是主线程中执行**。

由于事件派发是单线程的操作，所以只有等待前面事件监听器的回调函数执行完毕，才能够执行组件更新的操作，以及继续派发后面的事件。这样导致的一个后果就是：当在一个事件监听回调函数中做了耗时的操作，那么界面就会停住，界面上所有控件失效（不可触发）。

这就是为什么：把事件处理函数中将耗时的操作放到新线程（一般称之为工作线程）中执行，而不是让其在EDT中执行。