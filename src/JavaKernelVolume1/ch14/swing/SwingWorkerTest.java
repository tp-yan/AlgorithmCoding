package JavaKernelVolume1.ch14.swing;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class SwingWorkerTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new SwingWorkerFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

/**
 * This frame has a text area to show the contents of a text file, a menu to open a file and
 * cancel the opening process, and a status line to show the file loading progress.
 */
class SwingWorkerFrame extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private JLabel statusLine;
    private JMenuItem openItem;
    private JMenuItem cancelItem;
    private SwingWorker<StringBuilder, ProgressData> textReader;
    public static final int TEXT_ROWS = 20;
    public static final int TEXT_COLUMNS = 60;

    public SwingWorkerFrame() {
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        textArea.setEditable(false);
        add(new JScrollPane(textArea));

        statusLine = new JLabel(" ");
        add(statusLine, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        openItem = new JMenuItem("Open");
        menu.add(openItem);
        openItem.addActionListener(event -> {
            // 1.打开文件选择对话框，获取操作结果
            int result = fileChooser.showOpenDialog(null);
            // 2.返回结果为成功选中文件
            if (result == JFileChooser.APPROVE_OPTION) {
                textArea.setText("");
                openItem.setEnabled(false);
                // 3.获取选中的文件
                textReader = new TextReader(fileChooser.getSelectedFile());
                textReader.execute(); // 开启后台线程(工作线程)：启动SwingWorker执行其doInBackground方法
                cancelItem.setEnabled(true);
            }
        });

        cancelItem = new JMenuItem("Cancel");
        menu.add(cancelItem);
        cancelItem.setEnabled(false);
        cancelItem.addActionListener(e -> {
            textReader.cancel(true); // 调用Future的cancel方法终止线程
        });
        pack();
    }

    // 封装文本及其行号
    private class ProgressData {
        public int number;
        private String line;
    }

    /**
     * SwingWorker<T，V> 产生类型为T 的结果以及类型为V 的进度数据。
     */
    private class TextReader extends SwingWorker<StringBuilder, ProgressData> {
        private File file;
        private StringBuilder text = new StringBuilder();

        public TextReader(File file) {
            this.file = file;
        }

        // The following method executes in the worker thread; it doesn't touch Swing components.
        // 在此执行耗时任务，将结果StringBuilder传给done()，在中途可调用publish将数据ProgressData传给process()更新进度条
        @Override
        protected StringBuilder doInBackground() throws Exception {
            int lineNumber = 0;
            try (Scanner in = new Scanner(new FileInputStream(file), "UTF-8")) {
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    lineNumber++;
                    text.append(line).append("\n");
                    ProgressData data = new ProgressData();
                    data.number = lineNumber;
                    data.line = line;
                    publish(data);
                    Thread.sleep(1);
                }
            }
            return text;
        }

        /*The following methods execute in the event dispatch thread.
        为了提高效率，几个对publish 的调用结果，可用对process 的一次调用成批处理。
        同时更新行号与文本。
         */
        @Override
        protected void process(List<ProgressData> chunks) {
            /* 要取消正在进行的工作，使用Future 接口的cancel 方法。
            当该工作被取消的时候，get方法抛出CancellationException 异常。 */
            if (isCancelled()) return;
            StringBuilder b = new StringBuilder();
            // 只显示最后一个条目的行号
            statusLine.setText("" + chunks.get(chunks.size() - 1).number);
            for (ProgressData data : chunks) {
                b.append(data.line).append("\n"); // 将多条收集到一起再一次更新到textArea
            }
            textArea.append(b.toString());
        }

        // 读取所有内容之后，进行最后的更新
        // 当该工作被取消的时候，get方法抛出CancellationException异常。
        @Override
        protected void done() {
            try {
                /* SwingWorker<T，V> 实现了Future<T>。doInBackground返回结果可通过Future 接口的get 方法获得。
                 * 由于get 方法阻塞直到结果成为可用，因此不要在 调用execute 之后马上调用它。典型地，从done 方法调用get。 */
                StringBuilder result = get();
                textArea.setText(result.toString());
                statusLine.setText("Done");
            } catch (InterruptedException | CancellationException e) {
                textArea.setText("");
                statusLine.setText("Cancelled");
            } catch (ExecutionException e) {
                statusLine.setText("" + e.getCause());
            }
            cancelItem.setEnabled(false);
            openItem.setEnabled(true);
        }
    }
}
