package JavaKernelVolume1.ch07.log;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.*;

/* 将日志记录消息也显示在日志窗口中 */
public class LoggingImageViewer {

    public static void main(String[] args) {
        Thread.dumpStack();

        if (System.getProperty("java.util.logging.config.class") == null
                && System.getProperty("'java.util.logging.config.file") == null) {
            Logger.getLogger(ImageViewerFrame.PACKAGE_NAME).setLevel(Level.ALL);
            final int LOG_ROTATION_COUNT = 10;
            try {
                Handler handler = new FileHandler("%h/LoggingImageViewer.log", 0, LOG_ROTATION_COUNT);
                Logger.getLogger(ImageViewerFrame.PACKAGE_NAME).addHandler(handler);
            } catch (IOException e) {
                Logger.getLogger(ImageViewerFrame.PACKAGE_NAME).log(Level.SEVERE, "Can't create log file handler", e);
            }
        }
        // 将函数式接口添加到awt的事件处理线程中，awt的事件处理线程会按照队列的顺序依次调用每个待处理的事件。
        // awt是单线程模式的，所有awt的组件只能在(推荐方式)事件处理线程中访问
        EventQueue.invokeLater(() -> {
            Handler handler = new WindowHandler();
            handler.setLevel(Level.ALL);
            Logger.getLogger(ImageViewerFrame.PACKAGE_NAME).addHandler(handler);
            // 创建窗口
            JFrame frame = new ImageViewerFrame();
            frame.setTitle("LoggingImageViewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });

        /* 在运行期间抛出了 没有捕获到的异常，默认是将错误信息输出到System.err，比较好的方式是将这些内容记录到一个文件中。
        可以调用静态的Thread.setDefaultUncaughtExceptionHandler 方法改变非捕获异常的处理器：*/
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                // save information in log file
                Logger.getLogger(ImageViewerFrame.PACKAGE_NAME).log(Level.SEVERE, "uncaughtException Thread:" + t, e);
            }
        });

    }
}

class ImageViewerFrame extends JFrame {
    public static final String PACKAGE_NAME = "JavaKernelVolume1.ch07.log";

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 400;

    private JLabel label;
    private static final Logger myLogger = Logger.getLogger(PACKAGE_NAME);

    public ImageViewerFrame() {
        myLogger.entering("ImageViewerFrame", "<init>");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // 菜单栏
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem openItem = new JMenuItem("Open");
        menu.add(openItem);
        openItem.addActionListener(new FileOpenListener());

        JMenuItem exitItem = new JMenuItem("Exit");
        menu.add(exitItem);
        exitItem.addActionListener(event -> {
            myLogger.fine("Exiting.");
            System.exit(0);
        });

        label = new JLabel();
        add(label);
        myLogger.exiting("ImageViewerFrame", "<init>");
    }

    private class FileOpenListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            myLogger.entering("ImageViewerFrame.FileOpenListener", "actionPerformed", e);
            // 创建文件选择器
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            // 设置文件过滤器
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".gif") || f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "GIF images";
                }
            });
            int r = chooser.showOpenDialog(ImageViewerFrame.this);
            if (r == JFileChooser.APPROVE_OPTION) {
                String name = chooser.getSelectedFile().getPath();
                myLogger.log(Level.FINE, "Reading file {0}", name);
                label.setIcon(new ImageIcon(name));
            } else myLogger.fine("File open dialog canceled.");
            myLogger.exiting("ImageViewerFrame.FileOpenListener", "actionPerformed");
        }
    }
}

// 日志处理器，负责将日志输出到当前窗口
class WindowHandler extends StreamHandler {
    private JFrame frame;

    public WindowHandler() {
        frame = new JFrame();
        final JTextArea output = new JTextArea();
        output.setEditable(false);
        frame.setSize(200, 200);
        frame.add(new JScrollPane(output));
        frame.setFocusableWindowState(false);
        frame.setVisible(true);

        setOutputStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                output.append(new String(b, off, len));
            }
        });

        System.out.println("=============================================");
        Thread.dumpStack(); // 在任何地方都可以使用此语句：打印此位置的堆栈轨迹
        System.out.println("=============================================");
    }

    @Override
    public void publish(LogRecord record) {
        if (!frame.isVisible()) return;
        super.publish(record); // 将日志记录发送到希望的目的地。
        flush(); // 为了实时更新日志，需调用此方法，否则只有缓存区满才输出日志。刷新所有已缓冲的数据
    }
}
