package NiukePractice;

import java.util.Scanner;

/**
 * 题目描述
 * 小Q正在给一条长度为n的道路设计路灯安置方案。
 * <p>
 * 为了让问题更简单,小Q把道路视为n个方格,需要照亮的地方用'.'表示, 不需要照亮的障碍物格子用'X'表示。
 * <p>
 * 小Q现在要在道路上设置一些路灯, 对于安置在pos位置的路灯, 这盏路灯可以照亮pos - 1, pos, pos + 1这三个位置。
 * <p>
 * 小Q希望能安置尽量少的路灯照亮所有'.'区域, 希望你能帮他计算一下最少需要多少盏路灯。
 * <p>
 * 输入描述:
 * 输入的第一行包含一个正整数t(1 <= t <= 1000), 表示测试用例数
 * 接下来每两行一个测试数据, 第一行一个正整数n(1 <= n <= 1000),表示道路的长度。
 * 第二行一个字符串s表示道路的构造,只包含'.'和'X'。
 * 输出描述:
 * 对于每个测试用例, 输出一个正整数表示最少需要多少盏路灯。
 * <p>
 * 输入
 * 2
 * 3
 * .X.
 * 11
 * ...XX....XX
 * 输出
 * 1
 * 3
 */
public class Street_Lamp_Placed {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String tmp = sc.nextLine();
        int t = Integer.parseInt(tmp.trim());

        while (t-- > 0) {
            tmp = sc.nextLine();
//            System.out.println(tmp);

            String str = sc.nextLine();

            int counter = 0;
            int index = str.indexOf(".");

            if (index >= 0) {
                while (str.length() > 0 && index >= 0) {
                    counter++;
                    if (index + 3 < str.length()) {
                        str = str.substring(index+3);
                    }else{
                        break;
                    }
                    index = str.indexOf(".");
                }
                System.out.println(counter);
            } else {
                System.out.println(0);
            }
        }
    }
}
