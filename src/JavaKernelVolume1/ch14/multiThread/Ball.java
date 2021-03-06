package JavaKernelVolume1.ch14.multiThread;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Ball {
    private static final int XSIZE = 15;
    private static final int YSIZE = 15;
    private double x = 0;
    private double y = 0;
    // 坐标改变量，正负代表方向
    private double dx = 1;
    private double dy = 1;


    {
        x += Math.random()*30;
        y += Math.random()*50;
    }
    /**
     *  Moves the ball to the next position, reversing direction if it hits one of the edges
     */
    public void move(Rectangle2D bounds) {
        x += dx;
        y += dy;

        // 碰壁，改变方向
        if (x < bounds.getMinX()) {
            x = bounds.getMinX();
            dx = -dx;
        }
        if (x + XSIZE >= bounds.getMaxX()) {
            x = bounds.getMaxX()-XSIZE;
            dx = -dx;
        }
        if (y < bounds.getMinY()) {
            y = bounds.getMinY();
            dy = -dy;
        }
        if (y + YSIZE >= bounds.getMaxY()) {
            y = bounds.getMaxY()-YSIZE;
            dy = -dy;
        }
    }

    // Gets the shape of the ball at its current position.
    public Ellipse2D getShape() {
        return new Ellipse2D.Double(x,y,XSIZE,YSIZE);
    }
}
