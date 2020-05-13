package JavaKernelVolume1.ch05.reflection;

import java.lang.reflect.Array;
import java.util.Arrays;

public class CopyOfTest {
    public static void main(String[] args) {
        int[] a = {1,2,3};
        a = (int[])goodCopyOf(a,10);
        System.out.println(Arrays.toString(a));

        String[] b = {"Tom","Ficm","Zhang"};
        b = (String[])goodCopyOf(b,10);
        System.out.println(Arrays.toString(b));

        System.out.println("The following call will generate an exception.");
        b = (String[])badCopyOf(b,10); // Object[] 不能转为 String[]
    }

    /**
     * 通过反射编写一个通用的可以扩展任意类型数组的方法
     * @param a 为了实现对任意类型的数据都通用，须将参数设为Object，因为基本类型数组能转换为Object，但不能转换成Object[]
     * @param newLength
     * @return 返回和传入数组类型相同的扩展数组
     */
    public static Object goodCopyOf(Object a, int newLength) {
        Class cl = a.getClass();
        if (!cl.isArray()) return null; // 非数组对象
        Class componentType = cl.getComponentType();
        int length = Array.getLength(a);
        Object newArray = Array.newInstance(componentType,newLength); // 动态创建数组，可以是多维数组
        System.arraycopy(a,0,newArray,0,Math.min(length,newLength));
        return newArray;
    }

    /**
     * 将一个Employee[]临时地转换成Object[] 数组，然后再把它转换回来是可以的，
     * 但一从开始就是Object[]的数组却永远不能转换成Employee[]数组。
     * 为
     * @param a
     * @param newLength
     * @return 返回的数组，一开始就是Object[]，无法强制转为其他类型的数组
     */
    public static Object[] badCopyOf(Object[] a, int newLength) {
        Object[] newArray = new Object[newLength]; // 此处创建的是Object[]，而不是与数组a的实际类型相同的数组，所以对返回值
        // 强制转换会报错
        System.arraycopy(a,0,newArray,0,Math.min(newLength,a.length));
        return newArray;
    }
}
