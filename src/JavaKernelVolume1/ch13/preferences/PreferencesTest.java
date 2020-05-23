package JavaKernelVolume1.ch13.preferences;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

/**
 * A program to test preference settings. The program remembers the frame
 * position, size, and title
 */
public class PreferencesTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            PreferencesFrame frame = new PreferencesFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

/**
 * Preferences 类以一种平台无关的方式提供了这样一个中心存储库。
 * 在Windows中，Preferences 类使用注册表来存储信息；在Linux 上，信息则存储在本地文件系统中。
 * 存储库的各个节点分别有一个单独的 键/值对表，可以用来存储数值、字符串或字节数组，但不能存储可串行化的对象.
 * 每个程序用户分别有一棵树，一棵树上有多个节点(多个包)，每个节点对应一个key-vale表；另外还有一棵系统树，可以用于存放所有用户的公共信息。
 * <p>
 * A frame that restores position and size from user preferences and updates the
 * preferences upon exit.
 */
class PreferencesFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    // 若要访问树中的一个节点，需要从用户或系统根开始：
    private Preferences root = Preferences.userRoot();
    // 获得树中的节点，节点路径最好等于类的包名，这样就有另一种便捷方式来获得这个节点
    private Preferences node = root.node("JavaKernelVolume1.ch13.preferences");

    public PreferencesFrame() {
        int left = node.getInt("left", 0); // 必须要指定默认值
        int top = node.getInt("top", 0);
        int width = node.getInt("width", DEFAULT_WIDTH);
        int height = node.getInt("height", DEFAULT_HEIGHT);
        setBounds(left, top, width, height);

        String title = node.get("title", "");
        if (title.equals(""))
            title = JOptionPane.showInputDialog("Please supply a frame title:");
        if (title == null) title = "";
        setTitle(title);

        final JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem exportItem = new JMenuItem("Export preferences");
        menu.add(exportItem);
        exportItem.addActionListener(event -> {
            if (chooser.showSaveDialog(PreferencesFrame.this) == JFileChooser.APPROVE_OPTION) {
                savePreferences();
                try {
                    OutputStream out = new FileOutputStream(chooser.getSelectedFile());
                    node.exportSubtree(out); // 将这个节点及其子节点的首选项写至指定的流。
                    out.close();
                } catch (BackingStoreException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        JMenuItem importItem = new JMenuItem("Import preferences");
        menu.add(importItem);
        importItem.addActionListener(event -> {
            if (chooser.showOpenDialog(PreferencesFrame.this) == JFileChooser.APPROVE_OPTION) {
                try (InputStream in = new FileInputStream(chooser.getSelectedFile())) {
                    // 可能是用户或系统Preferences
                    Preferences.importPreferences(in); // 导入指定流中包含的首选项。导入后不能立即生效！
                } catch (IOException | InvalidPreferencesFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        menu.add(exitItem);
        exitItem.addActionListener(e -> {
            savePreferences();
            System.exit(0);
        });

    }

    public void savePreferences() {
        node.putInt("left", getX());
        node.putInt("top", getY());
        node.putInt("width", getWidth());
        node.putInt("height", getHeight());
        node.put("title", getTitle());
    }
}
