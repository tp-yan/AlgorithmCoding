package JavaKernelVolume1.ch06.lambda;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.function.IntConsumer;
import javax.swing.*;

public class LambdaTest {
    public static void main(String[] args) {
        /* 1.使用lambda表达式 */
        String[] planets = new String[]{"Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        System.out.println(Arrays.toString(planets));
        System.out.println("dictionary order:");
        Arrays.sort(planets);
        System.out.println(Arrays.toString(planets));
        System.out.println("length order:");
        // lambda表达式：即代码块。(String first,String second) -> {return first.length()-second.length();} 简写
        Arrays.sort(planets, (first, second) -> first.length() - second.length()); // ()里的参数类型可以通过上下文推导出来
        System.out.println(Arrays.toString(planets));

        // 只有一个参数且其类型可推导出时，可以省略小括号。只有一句话时可以省略{}
        Timer t = new Timer(5000, event -> System.out.println("The time is " + new Date()));
        t.start();



        /* 2.函数式接口必须有一个抽象方法
         * lambda 表达式总会转换为某个函数接口的实例。最好把lambda 表达式看作是一个函数，而不是一个对象。
         * 不能把 lambda 表达式赋给类型为Object 的变量，Object不是一个函数式接口。
         */
        Runnable a = () -> {
            for (int i = 100; i >= 0; i--) System.out.println(i);
        };
        a.run();
        // 无须指定lambda 表达式的返回类型，总是由上下文推导得出
        Comparator<String> com = (first, second) -> first.length() - second.length();

        /* 3.方法引用
         * 使用已有方法作为函数式接口传入
         * */
        Timer tm = new Timer(10000, System.out::println); //等价于lambda 表达式 x -> System.out.println(x)
        tm.start();
        // 指示的是实例方法，故该方法实际调用： (x, y) -> x.compareToIgnoreCase(y)
        Arrays.sort(planets,String::compareToIgnoreCase);
        System.out.println(Arrays.toString(planets));

        new TimedGreeter().greet();


        JOptionPane.showMessageDialog(null, "Quit program?");
        System.exit(0);
    }

    /* 4.lambda中引用外围变量
    * lambda 表达式中捕获的变量必须实际上是最终变量( effectively final。)
    * 实际上的最终变量是指，这个变量初始化之后就不会再为它赋新值。
    *  */
    public static void repeatMessage(String text, int delay) {
        ActionListener al = event ->{
            System.out.println(text);
            Toolkit.getDefaultToolkit().beep();
        };
    }

    /* 5.接收(处理)lambda 表达式 */
    // 1)选择一个函数式接口作为参数，这里选Runnable
    public static void repeat(int n, Runnable action) {
        for (int i=0;i<n;i++)
            action.run(); // 这里调用 lambda表达式的主体
    }

    // 2)函数式接口中方法带参数
    // 最好使用Java给出的标准函数式接口
    // 若要自己设计函数式接口，其中只有一个抽象方法，可以用@FunctionalInterface 注解来标记这个接口。
    // 标记：如果你无意中增加了另一个非抽象方法，编译器会产生一个错误消息。
    // 并不是必须使用注解根据定义，任何有一个抽象方法的接口都是函数式接口。
    public static void repeat2(int n, IntConsumer action) {
        for (int i = 0; i < n; i++) {
            action.accept(i);
        }
    }
}

class Application {
    public void init() {
        ActionListener al = event->{
            System.out.println(this.toString()); // 在一个lambda 表达式中使用this 关键字时，是指创建这个lambda 表达式
            // 的方法的this参数。this.toStringO 会调用Application 对象的toString 方法
        };
    }
}



class Greeter
{
    public void greet()
    {
        System.out.println("Hello, world!");
    }

    void greet(ActionEvent actionEvent) {
        System.out.println("Hello, actionEvent!");
    }
}

class TimedGreeter extends Greeter {
    public void greet() {
        Timer t = new Timer(1000,super::greet);
        t.start();
    }
}
