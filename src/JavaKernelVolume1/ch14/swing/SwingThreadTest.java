package JavaKernelVolume1.ch14.swing;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * 不要在非UI线程中调用修改UI界面的方法。
 * 而应该使用EventQueue 类的invokeLater 方法和invokeAndWait 方法使所调用的方法在事件分配线程(UI线程)中执行。
 * 只在单一的线程中修改UI界面。
 * <p>
 * 应该将Swing 代码放置到实现Runnable 接口的类的run 方法中。
 * 然后，创建该类的一个对象，将其传递给静态的invokeLater 或invokeAndWait 方法。
 * 当事件放人事件队列时，invokeLater 方法立即返回，而run 方法被异步执行。
 * invokeAndWait方法会阻塞，直到run 方法终止。
 * 这两种方法都是在事件分配线程(UI线程)中执行run 方法。没有新的线程被创建。
 */
public class SwingThreadTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> { // 异步执行，不阻塞
            JFrame frame = new SwingThreadFrame();
            frame.setTitle("SwingThreadTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

/**
 * This frame has two buttons to fill a combo box from a separate thread. The
 * "Good" button uses the event queue, the "Bad" button modifies the combo box directly.
 */
class SwingThreadFrame extends JFrame {
    public SwingThreadFrame() {
        final JComboBox<Integer> combo = new JComboBox<>();
        combo.insertItemAt(Integer.MAX_VALUE, 0);
        combo.setPrototypeDisplayValue(combo.getItemAt(0)); // 显示原型值，用于计算(条目)显示宽度和高度。
        combo.setSelectedIndex(0);

        JPanel panel = new JPanel();
        JButton goodButton = new JButton("Good");
        goodButton.addActionListener(event -> {
            new Thread(new GoodWorkerRunnable(combo)).start();
        });
        panel.add(goodButton);

        JButton badButton = new JButton("Bad");
        badButton.addActionListener(event -> {
            new Thread(new BadWorkerRunnable(combo)).start();
        });
        panel.add(badButton);
        add(panel);
        pack();
    }
}
/**
 * This runnable modifies a combo box by randomly adding and removing numbers.
 * order to ensure that the combo box is not corrupted, the editing
 * operations are forwarded to the event dispatch thread.
 */
class GoodWorkerRunnable implements Runnable {
    private JComboBox<Integer> comboBox;
    private Random generator;

    public GoodWorkerRunnable(JComboBox<Integer> combo) {
        comboBox = combo;
        generator = new Random();
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            while (true) {
                EventQueue.invokeLater(() -> {
                    int i = Math.abs(generator.nextInt());
                    if (i % 2 == 0) {
                        System.out.println("insert value:" + i);
                        comboBox.insertItemAt(i, 0);
                    } else if (comboBox.getItemCount() > 0) {
                        System.out.println("getItemCount:" + comboBox.getItemCount());
                        System.out.println("i:" + i);
                        int index = i % comboBox.getItemCount();
                        comboBox.removeItemAt(index);
                        System.out.println("removeItemAt:" + index);
                    }
                });
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * This runnable modifies a combo box by randomly adding and removing numbers.
 * This can result in errors because the combo box methods are not synchronized
 * and both the worker thread and the event dispatch thread access the combo box.
 * Error：在子线程中修改UI界面！
 * 在UI线程和子线中都访问UI界面，而没有同步，很容易导致并发访问异常！
 */
class BadWorkerRunnable implements Runnable {
    private JComboBox<Integer> comboBox;
    private Random generator;

    public BadWorkerRunnable(JComboBox<Integer> combo) {
        comboBox = combo;
        generator = new Random();
    }

    @Override
    public void run() {
        try {
            while (true) {
                int i = Math.abs(generator.nextInt());
                if (i % 2 == 0) {
                    comboBox.insertItemAt(i, 0);
                } else if (comboBox.getItemCount() > 0) {
                    comboBox.removeItemAt(i % comboBox.getItemCount());
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}