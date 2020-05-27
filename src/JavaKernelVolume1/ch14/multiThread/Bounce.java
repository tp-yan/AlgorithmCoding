package JavaKernelVolume1.ch14.multiThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Bounce {
    public static void main(String[] args) {
        EventQueue.invokeLater(() ->{
            JFrame frame = new BounceFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

/**
 * 承载小球和按钮组件的容器
 */
class BounceFrame extends JFrame {
    private BallComponent comp; // 存放小球的容器
    public static final int STEPS = 1000;
    public static final int DELAY = 3;

    public BounceFrame() {
        setTitle("Bounce");
        comp = new BallComponent();
        add(comp, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        addButton(buttonPanel, "Start", event -> addBall());
        addButton(buttonPanel, "Close", event -> System.exit(0));
        add(buttonPanel,BorderLayout.SOUTH);
        pack();
    }

    private void addButton(Container c, String name, ActionListener listener) {
        JButton button = new JButton(name);
        c.add(button);
        button.addActionListener(listener);
    }

    /**
     * Adds a bouncing ball to the panel and makes it bounce 1,000 times .
     */
    private void addBall() {
        try {
            Ball ball = new Ball();
            comp.add(ball);
            for (int i = 1; i <= STEPS; i++) {
                ball.move(comp.getBounds());
                comp.paintGraphics(comp.getGraphics());
                Thread.sleep(DELAY);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
