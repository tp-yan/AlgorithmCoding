package homework;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * https://www.nowcoder.com/questionTerminal/ac61207721a34b74b06597fe6eb67c52
 * 给定一个十进制数M，以及需要转换的进制数N。将十进制数M转化为N进制数.
 */
public class P01_DecimalConversion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int M = sc.nextInt();
        int N = sc.nextInt();

        int flag = 0; // N是否为负数
        if (M < 0) {
            M = -M;
            flag = 1;
        }

        ArrayList<Integer> remains = new ArrayList<>();
        while (M >= N) {
            remains.add(M % N);
            M /= N;
        }
        remains.add(M);
        StringBuilder sb = new StringBuilder();
        for (int i = remains.size() - 1; i >= 0; i--) {
            if (remains.get(i) >= 10) {
                switch (remains.get(i)) {
                    case 10:
                        sb.append("A");
                        break;
                    case 11:
                        sb.append("B");
                        break;
                    case 12:
                        sb.append("C");
                        break;
                    case 13:
                        sb.append("D");
                        break;
                    case 14:
                        sb.append("E");
                        break;
                    case 15:
                        sb.append("F");
                        break;
                }
            } else {
                sb.append(remains.get(i));
            }
        }
        if (flag == 1) {
            System.out.println("-" + sb.toString());
        } else {
            System.out.println(sb.toString());
        }
    }
}
