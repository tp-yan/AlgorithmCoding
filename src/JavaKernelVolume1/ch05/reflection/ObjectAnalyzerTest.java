package JavaKernelVolume1.ch05.reflection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ObjectAnalyzerTest {
    public static void main(String[] args) {
        ArrayList<Integer> squares = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            squares.add(i * i);
        }
        System.out.println(new ObjectAnalyzer().toString(squares));

        int[] a = {1, 2, 3};
        System.out.println(a.getClass().getName());
        System.out.println(a.getClass().getSuperclass().getName());
        Object b = a;
        System.out.println(b.getClass().getName());
        int[] c = (int[]) b;
        System.out.println(c == a);

        Integer[] A = {7, 8, 9};
        System.out.println(A.getClass().getName());
        System.out.println(A.getClass().getSuperclass().getName());
        Object B = A;
        System.out.println(B.getClass().getName());
        System.out.println(B.getClass().getSuperclass().getName());
        System.out.println(((Integer[]) B)[0]);
    }
}
