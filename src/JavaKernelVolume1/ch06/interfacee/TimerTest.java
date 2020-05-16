package JavaKernelVolume1.ch06.interfacee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * 使用回调
 */
public class TimerTest {
    /* 1.接口与回调 */
    public static void method1() {
        ActionListener ac = new TimerPrinter();
        // java.swing下的 计时器
        Timer t = new Timer(5000, ac); // 传入监听器对象，其包含回调方法
        t.start();
        JOptionPane.showMessageDialog(null, "Quit program?");
        System.exit(0);
    }

    /* 2.Comparator 接口 */
    // 比较器是实现了Comparator 接口的类的实例
    public static void method2() {
        Comparator<String> com = new LengthComparator();
        String[] words = {"windows","success","event","log"};
        // 使用Comparator接口，需要调用对象的compare()，而不是被对象对象的
        System.out.println((com.compare(words[0],words[1]) > 0?words[0]:words[1])+"更长");
        Arrays.sort(words,com);
        System.out.println(Arrays.toString(words));
    }


    public static void main(String[] args) {
        method2();
    }
}

/**
 * Java中是通过对象传递实现方法回调的，要求对象所属类实现了该回调方法。
 * ActionEvent 参数提供了事件的相关信息，例如，产生这个事件的源对象。
 */
class TimerPrinter implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("At the tone, the time is " + new Date());
        Toolkit.getDefaultToolkit().beep();
    }
}

// 按字符串长度排序
class LengthComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }
}