package JavaKernelVolume1.ch14.multiThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BounceThread {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new BounceThreadFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class BounceThreadFrame extends JFrame {
    private BallComponent comp; // 存放小球的容器
    public static final int STEPS = 1000;
    public static final int DELAY = 5;

    public BounceThreadFrame() {
        setTitle("Bounce");
        comp = new BallComponent();
        add(comp, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        addButton(buttonPanel, "Start", event -> addBall());
        addButton(buttonPanel, "Close", event -> System.exit(0));
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }

    private void addButton(Container c, String name, ActionListener listener) {
        JButton button = new JButton(name);
        c.add(button);
        button.addActionListener(listener);
    }

    /**
     * Adds a bouncing ball to the panel and makes it bounce 1,000 times .
     * 可以发起多个球，每个球都在自己的线程中运行。
     */
    private void addBall() {
        Ball ball = new Ball();
        comp.add(ball);
        // 应该将要并行运行的任务task与运行机制Thread解耦合。如
        Runnable task = () -> {
            try {
                for (int i = 1; i <= STEPS; i++) {
                    ball.move(comp.getBounds());
                    comp.paintGraphics(comp.getGraphics());
                    Thread.sleep(DELAY);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        // 要为每个任务创建一个独立的线程所付出的代价太大了。可以使用线程池来解决这个问题
        new Thread(task).start();
    }
}