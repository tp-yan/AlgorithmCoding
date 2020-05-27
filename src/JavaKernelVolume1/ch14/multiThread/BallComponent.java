package JavaKernelVolume1.ch14.multiThread;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 小球容器，负责绘制小球。
 */
public class BallComponent extends JPanel {
    private static final int DEFAULT_WIDTH = 450;
    private static final int DEFAULT_HEIGHT = 350;

    private List<Ball> balls = new ArrayList<>();


    public void paintGraphics(Graphics graphics) {
        super.paintComponent(graphics); // 擦除背景
        Graphics2D g2 = (Graphics2D)graphics;
        for (Ball b : balls) {
            g2.fill(b.getShape()); // 填充指定Shape的内部空间
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    /**
     * Add a ball to the component
     */
    public void add(Ball ball) {
        balls.add(ball);
    }
}
