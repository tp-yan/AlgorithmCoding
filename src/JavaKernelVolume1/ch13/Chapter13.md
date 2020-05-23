#### 1.Jar文件
一个JAR 文件既可以包含类文件，也可以包含诸如图像和声音这些其他类型的文件。JAR 文件是压缩的，它使用了大家熟悉的ZIP 压缩格式。
##### 创建JAR文件
新建一个jar文件：`jar cvf JARFileName File1 File2`，例如：`jar cvf FirstJar.jar *.class *.java *.png`。一般不包含java源文件。<br/>
**注：jar文件中必须与`class`包相同的目录结构，否则无法找到类。**
##### 清单文件
除了类文件、图像和其他资源外，每个 JAR 文件还包含一个用于描述归档特征的清单文件（manifest)。清单文件被命名为 `MANIFEST.MF` , 它位于 JAR 文件的一个特殊 `META-INF` 子目录中。<br/>
创建一个包含清单的JAR 文件，应该运行：
`jar cfm MyArchive.jar manifest.mf com/mycompany/mypkg/*.class`
##### 可执行JAR文件
可以使用jar 命令中的e 选项指定程序的入口点，即通常需要在调用java 程序加载器时
指定的类：
`jar cvfe HyPrograni.jar com.mycompany.mypkg.MainAppClass files to add`。或者，可以在清单中指定应用程序的主类：
```Java
Manifest-Version: 1.0
描述这个归档文件的行
Main-Class: com.mycompany.mypkg.MainAppClass

Name: Woozle.class
描述这个文件的行
Name: cora/mycompany/mypkg/
描述这个包的行

```
清单文件的最后一行必须以换行符结束。
启动应用程序：`java -jar MyProgram.jar`
#### 资源
加载资源必要的步骤：
1. 获得具有资源的Class 对象，例如，`AboutPanel.class`。
2. 如果资源是一个图像或声音文件，那么就需要调用`getresource (filename)` 获得作为 URL 的资源位置，然后利用`getImage` 或`getAudioClip` 方法进行读取。
3. 与图像或声音文件不同，其他资源可以使用`getResourceAsStream `方法读取文件中的
数据。

类加载器可以记住如何定位类，然后在同一位置査找关联的资源。
```Java
URL url = ResourceTest.class.getResource("about.gif");
Image img = new Imagelcon(url).getlmage();
```
这段代码的含义是“ 在找到ResourceTest 类的地方查找about.gif 文件”。

除了可以将资源文件与类文件放在同一个目录中外，还可以将它放在子目录中。
```Java
data/text/about.txt
```
这是一个相对的资源名，它会被解释为相对于加载这个资源的类所在的包。**注意，必须
使用“ /” 作为分隔符。**
##### 密封
可以将 Java 包密封（ seal ) 以保证不会有其他的类加人到其中。要想密封一个包，需要将包中的所有类放到一个JAR 文件中。可以在清单文件的主节中加入`Sealed`：
```Java
Name: com/mycoinpany/util/
Sealed: true
Name: com/myconpany/misc/
Sealed: false
```

#### 2.应用首选项的存储
##### 属性映射Properties
将配置信息保存在属性文件中。习惯上，会把程序属性存储在用户主目录的一个子目录中。目录名通常以一个点号开头，这个约定说明这是一个对用户隐藏的系统目录。

要找出用户的主目录，可以调用`System.getProperties` 方法：
`String userDir = System.getProperty("user.home")；`
```Java
Properties settings = new Properties();
settings.setProperty("width", "200");
settings.setProperty("title", "Hello, World!");
// 可以使用store 方法将属性映射列表保存到一个文件中。
OutputStream out = new FileOutputStream("program.properties");
settings.store(out, "Program Properties");
// 要从文件加载属性，可以使用以下调用：
I叩utStrean in = new Filei叩utStream("program.properties");
settings.load(in);
```
统一设置默认值：
```Java
// 把所有默认值都放在一个二级属性映射中，并在主属性映射的构造器中提供这个二级映射。
Properties defaultSettings = new Properties();
defaultSettings.setProperty("width", "300");
defaultSettings.setProperty("height", "200");
defaultSettings.setProperty("titie", "Default title");
Properties settings = new Properties(defaultSettings);
```
##### Preferences API
使用属性文件有以下缺点：
- 有些操作系统没有主目录的概念，所以很难找到一个统一的配置文件位置。
- 关于配置文件的命名没有标准约定，用户安装多个Java 应用时，就更容易发生命名冲突。

Preferences 类以一种平台无关的方式提供了这样一个中心存储库。在Windows中，Preferences 类使用注册表来存储信息；在Linux 上，信息则存储在本地文件系统中。

存储库的各个节点分别有一个单独的 键/值对表，可以用来存储数值、字符串或字节数组，但不能存储可串行化的对象。

每个程序用户分别有一棵树，一棵树上有多个节点(多个包)，每个节点对应一个key-value表；另外还有一棵系统树，可以用于存放所有用户的公共信息。

若要访问树中的一个节点，需要从用户或系统根开始：
```Java
Preferences root = Preferences.userRootQ ;
或
Preferences root = Preferences.systemRootO:
然后访问节点。可以直接提供一个节点路径名:
Preferences node = root.node("/com/mycompany/myapp"):
```
Preferences还可以导入导出配置：
```Java
void exportSubtree(OutputStream out)
void exportNode(OutputStream out)
数据用XML 格式保存。可以通过调用以下方法将数据导入到另一个存储库：
void importPreferences(InputStreain in)
```

