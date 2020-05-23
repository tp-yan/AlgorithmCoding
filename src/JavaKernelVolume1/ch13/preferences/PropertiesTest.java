package JavaKernelVolume1.ch13.preferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

/* Java 应用的传统做法，将配置信息（用户首选项和定制信息）保存在属性文件中。
 * 实际上是通过属性映射（property map)将信息保存到属性文件中。
 * 习惯上，会把程序属性存储在用户主目录的一个子目录中。目录名通常以一个点号开头，这个约定说明这是一个对用户隐藏的系统目录。
 * */
public class PropertiesTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            PropertiesFrame frame = new PropertiesFrame();
            frame.setVisible(true);
        });
    }
}

/**
 * A frame that restores position and size from a properties file and updates
 * the properties upon exit.
 */
class PropertiesFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    private File propertiesFile;
    private Properties settings;

    public PropertiesFrame() {
        String userDir = System.getProperty("user.home"); // 获取用户目录
        System.out.println("userDir:"+userDir);
        File propertiesDir = new File(userDir, ".corejava");
        System.out.println(propertiesDir.getAbsolutePath());
        if (!propertiesDir.exists()){
            boolean r = propertiesDir.mkdir();
            System.out.println("propertiesDir.mkdir():"+r);
        }

        propertiesFile = new File(propertiesDir, "program.properties");

        // 配置默认值
        Properties defaultSettings = new Properties();
        defaultSettings.setProperty("left", "0");
        defaultSettings.setProperty("top", "0");
        defaultSettings.setProperty("width", "" + DEFAULT_WIDTH);
        defaultSettings.setProperty("height", "" + DEFAULT_HEIGHT);
        defaultSettings.setProperty("title", "");

        settings = new Properties(defaultSettings);
        if (propertiesFile.exists()) {
            try (InputStream in = new FileInputStream(propertiesFile)) {
                settings.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int left = Integer.parseInt(settings.getProperty("left"));
        int top = Integer.parseInt(settings.getProperty("top"));
        int width = Integer.parseInt(settings.getProperty("width"));
        int height = Integer.parseInt(settings.getProperty("height"));
        setBounds(left, top, width, height);

        String title = settings.getProperty("title");
        if (title.equals(""))
            title = JOptionPane.showInputDialog("Please supply a frame title:");
        if (title == null) title = "";
        setTitle(title);

        // 保存程序退出时窗口属性
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                settings.setProperty("left", "" + getX());
                settings.setProperty("top", "" + getY());
                settings.setProperty("width", "" + getWidth());
                settings.setProperty("height", "" + getHeight());
                settings.setProperty("title", getTitle());
                try (OutputStream out = new FileOutputStream(propertiesFile)) {
                    settings.store(out, "Program Properties");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
    }
}
