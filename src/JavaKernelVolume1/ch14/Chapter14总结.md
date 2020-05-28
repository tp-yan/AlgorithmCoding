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
有时程序员**使用一个对象的锁**来实现额外的原子操作，实际上称为客户端锁定（clientside locking)，客户端锁定是非常脆弱的，通常不推荐使用。
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
java.util.concurrent.locks 包定义了两个锁类：
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
