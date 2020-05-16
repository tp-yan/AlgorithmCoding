package JavaKernelVolume1.ch06.innerClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import javax.swing.Timer;

public class AnonymousInnerClass {
    public static void main(String[] args) {
        TalkingClock2 clock = new TalkingClock2();
        clock.start(1000, true);
        // keep program running until user selects "0k"
        JOptionPane.showMessageDialog(null, "Quit program?");
        System.exit(0);
    }
}

class TalkingClock2 {
    public void start(int interval, boolean beep) {
        /* 1.局部内部类：存在于外部类的方法中
        * 可以访问外部类的成员以及所在方法的局部变量。编译器会检测局部内部类对局部变量的访问，为每一个变量建立相应的数据域，
          并将局部变量拷贝到构造器中。
        */
        class TimePrinter implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("At the tone, the time is " + new Date());
                if (beep) Toolkit.getDefaultToolkit().beep();
            }
        }

        /* 1.5局部内部类里'修改'局部变量 */
        // 局部内部类访问的局部变量必须是final的，故不能直接用 int counter，可以使用数组来实现。
        int[] counter = new int[1];
        Date[] dates = new Date[100];
        for (int i = 0; i < dates.length; i++) {
            dates[i] = new Date(){// 内部类扩展父类
                @Override
                public int compareTo(Date anotherDate) {
                    counter[0]++; // 统计比较的次数
                    return super.compareTo(anotherDate);
                }
            };
        }
        Arrays.sort(dates);
        System.out.println(counter+" comparisons.");

        /* 2.匿名内部类：可以实现接口或扩展父类。
           只能创建一个对象，没有构造函数，只能调用父类构造函数。
         */
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("At the tone, the time is " + new Date());
                if (beep) Toolkit.getDefaultToolkit().beep();
            }
        };
        /* 实现事件监听器和其他回调最好还是使用lambda 表达式，而不是匿名内部类
           Timer t = new Timer(1000,event -> {
                System.out.println("At the tone, the time is " + new Date());
                if (beep) Toolkit.getDefaultToolkit().beep();}
                );
        */
        Timer t = new Timer(1000,listener);
        t.start();
    }
}
