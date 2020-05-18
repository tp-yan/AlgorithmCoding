#### 1.异常
异常处理的任务就是将控制权从错误产生的地方转移给能够处理这种情况的错误处理器。
##### 1.异常分类
- 异常对象都是派生于Throwable 类的一个实例。
- Error 类层次结构描述了Java 运行时系统的内部错误和资源耗尽错误。应用程序不应该抛出这种类型的对象。
- Java 语言规范将派生于Error 类或RuntimeException 类的所有异常称为非受查( unchecked ) 异常，所有其他的异常称为受查（checked) 异常。编译器将核查是否为所有的受査异常提供了异常处理器。
##### 2.声明未检查异常
如果遇到了无法处理的情况，那么Java 的方法可以抛出一个异常。
```Java
public Fi1elnputStream(String name) throws FileNotFoundException
```
不需要声明Java 的内部错误，即从Error 继承的错误。也不应该声明从RuntimeException 继承的那些非受查异常。
<br/><font color=red>警告：</font>如果在子类中覆盖了超类的一个方法，子类方法中声明的受查异常不能比超类方法中声明的异常更通用（也就是说，子类方法中可以抛出更特定的异常，或者根本不抛出任何异常）。特别需要说明的是，**如果超类方法没有抛出任何受查异常，子类也不能抛出任何受查异常**。

#### 3.创建异常类
定义的类应该包含两个构造器，一个是默认的构造器；另一个是带有详细描述信息的构造器（超类Throwable 的toString 方法将会打印出这些详细信息，这在调试中非常有用)。
```Java
class FileFormatException extends IOException
{
    public FileFormatExceptionO {}
    public FileFormatException(String gripe)
    {
        super(gripe);
    }
}
```
#### 2.捕获异常
如果没有处理器捕获这个异常，当前执行的线程就会结束。要想捕获一个异常，必须设置`try/catch` 语句块。<br/>
通常，应该捕获那些知道如何处理的异常，而将那些不知道怎样处理的异常继续进行传递。
##### 1.捕获多个异常
```Java
try
{

    code that might throw exceptions
}
// 合并异常：只有当捕获的异常类型彼此之间不存在子类关系时才需要这个特性
// 捕获多个异常时，异常变量隐含为final 变量
catch (FileNotFoundException | UnknownHostException e)
{
    emergency action for missing files and unknown hosts
}
catch (IOException e)
{
    emergency action for all other I/O problems
}
```
##### 3.再次抛出异常与异常链
在 catch 子句中可以抛出一个异常， 这样做的目的是改变异常的类型。强烈建议使用这种包装技术：将原始异常设置为新异常的“ 原因”。
```Java
try
{
    access the database
}
catch (SQLException e)
{
    Throwable se = new ServletException ("database error")；
    se.initCause(e);
    throw se;
}
```
当捕获到异常时，就可以使用下面这条语句重新得到原始异常：
`Throwable e = se.getCause();`<br/>
只想记录一个异常，再将它重新抛出，而不做任何改变：
```Java
try
{
    access the database
}
catch (Exception e)
{
    logger.log(level, message, e); // 将异常信息输出到日志
    throw e;
}
```
##### 3.finally子句
不管是否有异常被捕获，finally 子句中的代码都被执行。
```Java
InputStream in = new FileInputStream(. . .);
try{
    // 1
    code that might throw exceptions
    // 2
}
catch (IOException e)
{
    // 3
    show error message
    // 4
}
finally
{
    // 5
    in.close();
}
// 6
```
- 如果catch子句抛出了一个异常，异常将被抛回这个方法的调用者。在这里，执行标注1、3、5 处的语句。
- 如果finally中遇到一个异常，这个异常将会被抛出，并且必须由另一个catch 子句捕获。

强烈建议解耦合`try/catch` 和`try/finally` 语句块：
```Java
InputStrean in = ...;
// 外层的try 语句块也只有一个职责，就是确保报告出现的错误。同时还能捕获finally中抛出的异常
try
{
    // 内层的try 语句块只有一个职责，就是确保关闭输入流。
    try
    {
        code that might throw exceptions
    }
    finally
    {
        in.close();
    }
}
catch (IOException e)
{
    show error message
}
```
<font color=red>警告：</font>若利用`return`语句从try 语句块中退出。在方法返回前，finally 子句的内容将被执行。如果finally 子句中也有一个`return` 语句，这个返回值将会覆盖原始的返回值。

##### 4.带资源的try
若try 中抛出了异常，且这些异常只有这个方法的调用者才能够给予处理。同时finally 语句块也抛出异常，那么try抛出的异常将会丢失，转而抛出finally中的异常。针对这种情况，可以使用2个嵌套的try/catch解决，但推荐使用带资源的try（前提是资源属于一个实现了AutoCloseable 接口的类）。
```Java
try (Resource res = ...)
{
    work with res
}
```
`try`块正常退出时，或者存在一个异常时会自动调用`res.close()`。还可以指定多个资源:
```Java
try (Scanner in = new Scanner(new FileInputStream("/usr/share/dict/words"); "UTF-8");
PrintWriter out = new PrintWriter("out.txt"))
{
    while (in.hasNext())
        out.println(in.next().toUpperCase());
}
```
带资源的try 语句自身也可以有catch 子句和一个finally 子句。这些子句会在关闭资源之后执行。

##### 5.方法堆栈轨迹
堆栈轨迹（stack trace ) 即方法的调用过程。静态的`Thread.getAllStackTrace` 方法， 它可以产生所有线程的堆栈轨迹。
```Java
Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces() ;
for (Thread t : map.keySet () )
{
    // StackTraceElement 类含有能够获得文件名和当前执行的代码行号的方法，还含有能够获得类名和方法名的方法。
    StackTraceElement[] frames = map.get(t);
    analyze frames
    // Throwable::getStackTrace()：获得构造这个对象时调用堆栈的跟踪。
}
```

#### 3.使用断言
断言机制允许在测试期间向代码中插入一些检査语句。当代码发布时，这些插人的检测语句将会被自动地移走。
```Java
// 这两种形式都会对条件进行检测，如果结果为false, 则抛出一个AssertionError 异常。在第二种形式中，表达式将被传入AssertionError 的构造器，并转换成一个消息字符串。
assert 条件;
assert 条件:表达式; //  表达式” 部分的唯一目的是产生一个消息字符串。
```
在启用或禁用断言时不必重新编译程序。启用或禁用断言是类加载器( class loader) 的功能。当断言被禁用时，类加载器将跳过断言代码，因此，不会降低程序运行的速度。
- 断言检查只用于开发和测阶段。
- 断言只应该用于在测试阶段确定程序内部的错误位置。

#### 4.记录日志
##### 1.基本日志
全局日志记录器：
- Logger.getClobal().info("File->Open menu item selected")
- Logger.getClobal ().setLevel (Level .OFF);

自定义日志记录器：
- `private static final Logger myLogger = Logger.getLogger("com.mycompany.myapp");` 防止日志记录器被JVM垃圾回收声明为静态变量
- 在默认情况下，只记录前3个级别，`Level.ALL` 开启所有级别的记录。
- `logger.warning(message); 
logger,fine(message); 
logger.log(Level.FINE,message);`
- 进入、退出方法：`logger.entering("com.mycompany.mylib.Reader", "read",
new Object[] { file, pattern });
logger.exiting("com.mycompany.mylib. Reader", "read" , count); `
- 记录异常信息：`Logger.getLogger("com.mycompany.myapp").log(Level.WARNING , "Reading image", e);`

##### 2.日志本地化
- 本地化的应用程序包含资源包（resource bundle ) 中的本地特定信息。资源包由各个地区( 如美国或德国）的映射集合组成。ResourceBimdle 类自动地对它们进行定位。
- 在请求日志记录器时，可以指定一个资源包：
`Logger logger = Logger.getLogger(loggerName, "com.mycompany.logmessages");`
- 通常需要在本地化的消息中增加一些参数，因此，消息应该包括占位符{0}、{1} 等：`Reading file {0}.
Achtung! Datei {0} wird eingelesen.`。向占位符传递具体的值：
`
logger.log(Level .INFO, "readingFile", fileName);
logger.log(Level .INFO, "renami ngFile", new Object[] { oldNmae, newName });
`

##### 3.日志处理器
日志记录并不将消息发送到控制台上，这是处理器的任务。另外，**处理器也有级别**。要想在控制台上看到FINE 级别的消息，就需要进行下列设置：`java.util.logging.ConsoleHandler.level=FINE`。
- 在默认情况下,日志记录器将记录发送到`ConsoleHandler` 中，并由它输出到`System.err`流中。
- 对于一个要被记录的日志记录，它的日志记录级别必须高于日志记录器和处理器的阈值。

安装自己的处理器：
```Java
Logger logger = Logger.getLogger("com.mycompany.myapp") ;
logger.setLevel (Level .FINE) ;
logger.setUseParentHandlers(false); // 在默认情况下，日志记录器将记录发送到自己的处理器和父处理器。这里设置不发送到父处理器
Handler handler = new ConsoleHandler();
handler.setLevel (Level .FINE);
logger.addHandler(handler):
```
要想将日志记录发送到其他地方，就要添加其他的处理器：
`FileHandler`：可以收集文件中的记录。
```Java
// 将记录发送到默认文件的处理器
FileHandler handler = new FileHandler();
logger.addHandler(handler);

Logger.getLogger("").setLevel(Level.ALL);
final int LOG_ROTATION_COUNT = 10;
// %h：指定本机用户目录
Handler handler = new FileHandler("%h/myapp.log", 0, LOG_ROTATION_COUNT):
Logger.getLogger("").addHandler(handler):
```

#### 5.调试技巧
1. 一个不太为人所知但却非常有效的技巧是在每一个类中放置一个单独的main 方法。这样就可以对每一个类进行单元测试。可以为每个类保留一个main 方法，在运行applet 应用程序的时候，这些main 方法不会被调用，而在运行应用程序的时候，Java 虚拟机只调用启动类的main 方法。
2. JUnit 是一个非常常见的单元测试框架，利用它可以很容易地组织测试用例套件。只要修改类，就需要运行测试。
3. 日志代理（logging proxy) 是一个子类的对象，它可以截获方法调用，并进行日志记录，然后调用超类中的方法。
4. 只要在代码的任何位置插入下面这条语句就可以获得堆栈轨迹：`Thread.duapStack();`—般来说，堆栈轨迹显示在`System.err` 上。也可以利用`Throwable`的`printStackTrace(PrintWriter s)`方法将它发送到一个文件中。将它捕获到一个字符串中：
```Java
StringWriter out = new StringWriter();
new Throwable().printStackTrace(new PrintWriter(out));
String description = out.toString();
```
5. 可以调用静态的`Thread.setDefaultUncaughtExceptionHandler`方法改变非捕获异常的处理器：
```Java
Thread.setDefaultUncaughtExceptionHandlerf
new Thread.UncaughtExceptionHandler()
{
    public void uncaughtException(Thread t, Throwable e)
    {
        save information in logfile
    }
};)
```
