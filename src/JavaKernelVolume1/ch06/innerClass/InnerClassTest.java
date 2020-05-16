package JavaKernelVolume1.ch06.innerClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class InnerClassTest {
    public static void main(String[] args) {
        TalkingClock clock = new TalkingClock(1000, true);
        clock.start();
        JOptionPane.showMessageDialog(null, "Quit program?");
        System.exit(0);
    }
}

class TalkingClock {
    private int interval;
    private boolean beep;

    public TalkingClock(int interval, boolean beep) {
        this.interval = interval;
        this.beep = beep;
    }

    public void start() {
        ActionListener listener = new TimerPrinter(); // JVM会将 外部类的this作为参数传入内部类的构造器，故内部类里含有指向外部类的引用
        Timer t = new Timer(interval, listener);
        t.start();
    }

    /* TimePrinter 类声明为私有的。这样一来，只有TalkingClock 的方法才能够构造TimePrinter 对象。
    只有内部类可以是私有类，而常规类只可以具有包可见性，或公有可见性。*/
    public class TimerPrinter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("At the tone, the time is " + new Date());
            // 内部类方法可以访问该类定义所在的作用域中的数据，包括私有的数据。
            if (beep) Toolkit.getDefaultToolkit().beep();
        }
    }
}
