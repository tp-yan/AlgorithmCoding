package JavaKernelVolume1.ch05.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.Scanner;

/*
能够分析类能力的程序称为反射（reflective)。
 */
public class ReflectionTest {
    public static void main(String[] args) {
        /* 1.获取Class对象的3种方式 */
        // 1.obj.getClass()
        Random gen = new Random();
        Class cl = gen.getClass(); // Class其实是泛型类 这里cl实际为 Class<Random>类型
        System.out.println(cl.getName());
        // 2.Class.forName(str)
        String className = "JavaKernelVolume1.ch05.Manager";
        try {
            Class cl2 = Class.forName(className);
            System.out.println(cl2.getName());
            System.out.println(cl2.getSuperclass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 3.如果T是任意的Java 类型（或void 关键字), T.class 将代表匹配的类对象。
        // 一个Class 对象实际上表示的是一个类型，而这个类型未必一定是一种类
        Class cl3 = Random.class;
        Class cl4 = int.class; // int 不是类，但int.class 是一个Class 类型的对象。
        Class cl5 = Double[].class;
        System.out.println(cl3.getName());
        System.out.println(cl4.getName());
        System.out.println(cl5.getName());

        /* 2.虚拟机为每个类型管理一个Class 对象。因此，可以利用=运算符实现两个类对象比较的操作 */
        if (gen.getClass() == Random.class)
            System.out.println("gen.getClass() == Random.class，说明Class对象的唯一性");

        /* 3.使用Class.newInstance():调用无参构造函数创建新对象 */
        // 想调用有参构造函数，必须使用 java.lang.reflect.Constructor.Constructor 类中的 newInstance 方法传递参数
        try {
            System.out.println(Random.class.newInstance().nextInt(10));
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        /* 4.运行时分析类 */
        analyzeClass(args);
    }

    /* 4.利用反射分析类的结构 */
    // 此方法可以分析JVM加载的任何类
    public static void analyzeClass(String[] args){
        String className;
        if (args.length > 0 )
            className = args[0];
        else{
            Scanner in = new Scanner(System.in);
            System.out.println("Enter class name:");
            className = in.next();
        }
        try {
            Class cl = Class.forName(className);
            Class superCl = cl.getSuperclass();
            // 1.打印类声明语句
            String modifiers = Modifier.toString(cl.getModifiers()); // 类的修饰符
            if (modifiers.length() > 0)
                System.out.println(modifiers+" ");
            System.out.print("class " + className);
            if (superCl != null && superCl != Object.class)
                System.out.println(" extends "+superCl.getName());
            // 2.打印类内容
            System.out.println("{");
            printConstructors(cl);
            System.out.println();
            printMethods(cl);
            System.out.println();
            printFields(cl);
            System.out.println("}");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void printFields(Class cl) {
        Field[] fields = cl.getDeclaredFields();
        for (Field f : fields) {
            Class type = f.getType(); // 域类型
            System.out.print("\t");
            String modifier = Modifier.toString(f.getModifiers());
            if (modifier.length() > 0)
                System.out.print(modifier+" ");
            System.out.println(type.getName()+" " + f.getName()+";");
        }
    }

    private static void printMethods(Class cl) {
        Method[] methods = cl.getDeclaredMethods();
        for (Method m : methods) {
            Class retType = m.getReturnType(); // 方法返回类型
            System.out.print("\t");
            String modifier = Modifier.toString(m.getModifiers());
            if (modifier.length() > 0)
                System.out.print(modifier+" "); // 修饰符包括：public、private、protect、static、volatile、final等
            System.out.print(retType.getName()+" "+m.getName()+"(");
            Class[] paramTypes = m.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                if (i > 0) System.out.print(",");
                System.out.print(paramTypes[i].getName());
            }
            System.out.println(");");
        }
    }

    private static void printConstructors(Class cl) {
        Constructor[] constructors = cl.getDeclaredConstructors(); // 所有构造器
        for (Constructor c : constructors) {
            String name = c.getName();
            System.out.print("\t");
            String modifiers = Modifier.toString(c.getModifiers()); // 修饰符
            if (modifiers.length() > 0)
                System.out.print(modifiers+" ");
            System.out.print(name+"(");

            // 方法参数
            Class[] paramTypes = c.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                if (i>0)
                    System.out.print(",");
                System.out.print(paramTypes[i].getName());
            }
            System.out.println(");");
        }
    }

}
