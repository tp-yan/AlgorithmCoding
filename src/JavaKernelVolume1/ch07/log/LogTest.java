package JavaKernelVolume1.ch07.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* 日志记录并不将消息发送到控制台上，这是处理器的任务。处理器也有级别。要想在控制台上看到FINE 级别的消息，就需要进行下列设置:
    java.util.logging.ConsoleHandler.level=FINE
* */
public class LogTest {

    public static void main(String[] args) throws IOException {
        /* 1.简单日志使用全局日志记录器（global logger)，默认只输出 info及以上级别的日志，可以在logging.properties中设置 */
//        Logger.getGlobal().setLevel(Level.OFF); // 取消全局日志记录器的所有日志输出
        Logger.getGlobal().info("File->Open menu item info");
        Logger.getGlobal().severe("File->Open menu item severe");
        Logger.getGlobal().warning("File->Open menu item warning");
        // 若为info级别，那么以下日志不会输出
        Logger.getGlobal().fine("File->Open menu item selected");
        Logger.getGlobal().finer("File->Open menu item selected");
        Logger.getGlobal().finest("File->Open menu item selected");

        System.out.println();
        /* 2.自定义日志记录器 */
        // 如果将记录级别设定为INFO 或者更低，则需要修改日志处理器的配置。默认的日志处理器不会处理低于INFO 级别的信息。
        myLogger.setLevel(Level.FINEST);
        myLogger.log(Level.FINE, "Level.FINE");
        myLogger.severe("myLogger severe");
        myLogger.warning("myLogger warning");
        myLogger.info("myLogger info");
        // 默认只输出 info及以上级别的日志

        myLogger.fine("myLogger fine");
        myLogger.finer("myLogger finer");
        myLogger.finest("myLogger finest");

        read("test.txt", "w+");
        System.out.println();
        try {
            logExceptionMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* 5.日志记录本地化 */
        // 获取日志记录器时，可以指定一个资源包：
        Logger logger = Logger.getLogger("Language_test", "JavaKernelVolume1.ch07.logmessages");
        logger.info("readingFile");
        logger.log(Level.INFO, "readingFile", new Object[]{"src", "dst"}); // 向占位符传入参数

        Logger fileLogger = Logger.getLogger("fileLogger");
        // 自定义日志文件名，循环存储方式，大小限制，是否追加等配置
        FileHandler fileHandler = new FileHandler("%h/myFileLogger%g.log", 1000, 5, true);
        fileLogger.addHandler(fileHandler);
        fileLogger.info("file logger");

        /* 6.过滤器 */
        logger.setFilter(record-> {
            String r = record.getMessage();
        return r.contains("tp");}); // 过滤掉不包含'tp'的记录，返回true即不过滤，false则过滤
        logger.info("tp1");
        logger.warning("tp2");
        logger.info("TP");
        logger.warning("tp");
        logger.warning("TP");
    }

    /* 2.创建或获取记录器
     * 未被任何变量引用的日志记录器可能会被垃圾回收，所以设为 static，防止被回收。
     * */
    private static final Logger myLogger = Logger.getLogger("JavaKernelVolume1.ch07.log");

    /* 3.日志跟踪执行流 */
    public static int read(String file, String pattern) {
        myLogger.entering("JavaKernelVolume1.ch07.log.LogTest", "read");
        // do something...
        int count = 0;
        myLogger.exiting("JavaKernelVolume1.ch07.log.LogTest", "read", count);
        return count;
    }

    /* 4.日志记录异常信息 */
    public static void logExceptionMsg() throws IOException {
        // 1) 方式一：void throwing(St ring className, String methodName , Throwable t)
        // 调用throwing 可以记录一条FINER 级别的记录和一条以THROW 开始的信息
        if (true) {
            IOException exception = new IOException("ioEx");
            myLogger.throwing("JavaKernelVolume1.ch07.log.LogTest", "logExceptionMsg", exception);
            throw exception;
        }
        // 2) 方拾二：void log(Level l , String message, Throwable t)
        try {
            throw new IOException("ioEx");
        } catch (IOException e) {
            myLogger.log(Level.WARNING, "logExceptionMsg", e);
        }
    }

}
