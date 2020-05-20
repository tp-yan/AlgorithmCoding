package JavaKernelVolume1.ch08.genericReflection;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Scanner;

/*
 * 如果对象是泛型类的实例，关于泛型类型参数则得不到太多信息，因为它们会被擦除。
 * 可以通过虚拟机得到泛型类和泛型方法的签名信息。
 *
 * 使用泛型反射AI> I 打印出给定类的有关内容
 * */
public class GenericReflectionTest {
    public static void main(String[] args) {
        String name;
        if (args.length > 0) {
            name = args[0];
        } else {
            try (Scanner in = new Scanner(System.in)) {
                System.out.println("Enter class name (e.g. java.util.Collections): ");
                name = in.next();
            }
        }

        try {
            Class<?> cl = Class.forName(name);
            printClass(cl);
            for (Method m : cl.getDeclaredMethods()) { // 所有方法不包括继承的
                printMethod(m);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void printClass(Class<?> cl) {
        System.out.print(cl);
        // getTypeParameters:如果这个类型被声明为泛型类型， 则获得泛型类型变量，否则获得一个长度为 0 的数组。
        printTypes(cl.getTypeParameters(), "<", ",", ">", true);
    }

    public static void printMethod(Method m) {
        String name = m.getName();
        // 1.方法修饰符
        System.out.print(Modifier.toString(m.getModifiers()));
        System.out.print(" ");
        // 2.泛型方法声明，若打印泛型参数与方法返回值类型
        // getTypeParameters:如果这个方法被声明为泛型方法， 则获得泛型类型变量，否则返回长度为 0 的数组。
        printTypes(m.getTypeParameters(), "<", ",", ">", true);
        // getGenericReturnType:获得这个方法被声明的泛型返回类型。
        printType(m.getGenericReturnType(), false);
        System.out.print(" ");
        // 3.方法名
        System.out.print(name);
        System.out.print("(");
        // 4.方法声明中的泛型参数类型
        // getGenericParameterTypes:获得这个方法被声明的泛型参数类型。 如果这个方法没有参数， 返回长度为 0 的数组。
        printTypes(m.getGenericParameterTypes(), "", ", ", "", false);
        System.out.println(")");
    }

    public static void printTypes(Type[] types, String pre, String sep, String suf, boolean isDefinition) {
        if (pre.equals("extends") && Arrays.equals(types, new Type[]{Object.class})) return;
        if (types.length > 0) System.out.print(pre);
        for (int i = 0; i < types.length; i++) {
            if (i > 0) System.out.print(sep);
            printType(types[i], isDefinition);
        }
        if (types.length > 0) System.out.print(suf);
    }

    /**
     * class Pair<T> extends java.lang.Object
     * public static <T extends Employee & java.lang.Comparable<T>> Pair<T> minmax(T[])
     *
     * @param type
     * @param isDefinition
     */
    public static void printType(Type type, boolean isDefinition) {
        // Type的1个子类型和4个子接口
        if (type instanceof Class) {// type是类，比如Employee
            Class<?> t = (Class<?>) type;
            System.out.print(t.getName());
        } else if (type instanceof TypeVariable) { // type是类型变量，比如 T extends Employee & java.lang.Comparable<T>
            TypeVariable<?> t = (TypeVariable<?>) type;
            System.out.print(t.getName());
            if (isDefinition) // 获得类型变量的子类限定:  Employee & java.lang.Comparable<T>
                printTypes(t.getBounds(), " extends ", "&", "", false);
        } else if (type instanceof WildcardType) { // type是通配符，如 ? super Manager & java.lang.Comparable
            WildcardType t = (WildcardType) type;
            System.out.print("?");
            printTypes(t.getUpperBounds(), " extends ", "&", "", false);
            printTypes(t.getLowerBounds(), " super ", "&", "", false);
        } else if (type instanceof ParameterizedType) { // type是描述泛型类或接口类型（如Comparable<? super T>。)
            ParameterizedType t = (ParameterizedType) type;
            Type owner = t.getOwnerType();
            if (owner != null) {
                printType(owner, false);
                System.out.print(".");
            }
            printType(t.getRawType(), false);
            printTypes(t.getActualTypeArguments(), "<", ",", ">", false);
        } else if (type instanceof GenericArrayType) { // type是类型变量
            GenericArrayType t = (GenericArrayType) type;
            System.out.print(".");
            printType(t.getGenericComponentType(), isDefinition);
            System.out.print("[]");
        }
    }
}
