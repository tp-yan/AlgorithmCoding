package JavaKernelVolume1.ch03; // 注：若源文件有包名，在控制台编译源文件、执行.class时都需要带上包名.xx，否则会出现找不到类名！

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

/*
 * Java核心卷1 第3章
 * Unicode标准编码采用固定的4字节表示字符。
 * utf-8：采用变长的1~4字节表示字符。绝大多数汉字用3字节，少数用4字节，ASCII范围的字符都是1字节。兼容ASCII
 * utf-16：采用2或4字节表示字符。绝大多数汉字用2字节，少数用4字节，故ASCII范围的字符是2字节。不兼容ASCII
 * 在JVM中字符或字符串都用char或char[]表示，而char采用的是utf-16编码，固定2字节。若用char表示utf-16中超过2字节的字符时，就需要
 * 使用多个char来表示！
 *
 * 注：编码得到.class使用的是Modified UTF-8而非标准的UTF-8。JVM运行.class时需要将Modified UTF-8转为UTF-16。
 * */

public class Chapter03 {

    /* 1.字符串 */
    private void strFeature() {
        String s1 = "abcd"; // 只有字符串常量还是存于共享池的
        System.out.println("字符个数:" + s1.length() + " == 码点个数:" + s1.codePointCount(0, s1.length()));
        System.out.println(s1 + "占用字节数:" + s1.getBytes().length);
        System.out.println("1位置的代码单元(char)：" + s1.charAt(1)); // 返回 代码单元:char
        System.out.println("1位置的码点(int)：" + s1.codePointAt(1)); // 返回码点:int

        System.out.println("字符串-->码字：");
        int[] arr = s1.codePoints().toArray();
        for (int a : arr) {
            System.out.print(a + " ");
        }

        String s2 = "中文中国";
        System.out.println("\n字符数:" + s2.length() + " 码点数:" + s2.codePointCount(0, s2.length())); // 码点的数量，即字符的个数
        System.out.println(s2 + "占用字节数:" + s2.getBytes().length);
        System.out.println("0位置的代码单元(char)：" + s2.charAt(0)); // 返回的是 char型
        System.out.println("0位置的码点(int)：" + s2.codePointAt(0)); // 返回的是码点，int类型

        System.out.println("字符串-->码字：");
        arr = s2.codePoints().toArray();
        for (int a : arr) {
            System.out.print(a + " ");
        }

        // getBytes 实际是做编码转换，缺省使用UTF-8格式编码
        System.out.println("\n中:" + "中".getBytes().length); // 3
        System.out.println("中:" + "中".getBytes(StandardCharsets.UTF_8).length); // 3
        System.out.println("中:" + "中".getBytes(StandardCharsets.UTF_16).length); // 4
        System.out.println("a:" + "a".getBytes().length); // 1
        System.out.println("a:" + "a".getBytes(StandardCharsets.UTF_8).length); // 1
        System.out.println("a:" + "a".getBytes(StandardCharsets.UTF_16).length); // 4
        System.out.println("a".toCharArray().length);

        System.out.println("常量字符串：");
        String s3 = "ab" + "cd"; // 通过'+'拼接的字符串仍然是存在于共享池？？？
        if (s1 == s3) {
            System.out.println("s1 == s3");
        }
        if (s1.equals(s3)) {
            System.out.println("s1.equals(s3)");
        }
        if (s1.substring(0, 2) == "ab") {
            System.out.println("s1.substring(0,2) == \"ab\"");
        }
        if (s1.substring(0, 2).equals("ab")) {
            System.out.println("s1.substring(0,2).equals(\"ab\")");
        }
        if (s1.equals(s1.substring(0, 2) + "cd")) {
            System.out.println("s1.equals(s1.substring(0,2)+\"cd\")");
        }
        if (s1 == s1.substring(0, 2) + "cd") {
            System.out.println("s1 == s1.substring(0,2)+\"cd\"");
        }

        String ss = "\uD842\uDFB7abcd中国"; // 第一个字符超过了BMP，需要使用4个字节表示
        System.out.println(ss.charAt(0));
        System.out.println(ss.charAt(1));
        System.out.println(ss.charAt(2));
        // 正确遍历Java字符串
        for (int i = 0; i < ss.length(); ) {
            int cp = ss.codePointAt(i); // 第i个码点
            // do something
            System.out.println("cp:" + cp);
            i += Character.charCount(cp); // 码字cp占的char个数
        }
        // 获取第i个码点
        int index = ss.offsetByCodePoints(0, 6);
        int cp = ss.codePointAt(index);
        System.out.println("cp:" + cp);


        System.out.println(Character.charCount("\uD842\uDFB7".codePointAt(0)));
        System.out.println(Character.charCount("a".codePointAt(0)));
    }

    /* 2. 输入输出*/
    // 控制台读取密码使用专用类Console而非Scanner
    // 需在控制台下，运行此方法，在IDE报错
    private String[] getUserAndPwd() {
        Console con = System.console();
        String username = con.readLine("user name:"); // Console只能读取一行，不能读入单个词或数值
        char[] pwd = con.readPassword("password:"); // 为了安全密码存入char数组而非字符串，使用后覆盖char[]
        return new String[]{username, new String(pwd)};
    }

    // 格式化输出
    private void formatOutput() {
        System.out.printf("2*%,+.2f\n", -333333.33); // ,:分组 +:正负号
        // 旧式输出日期
        System.out.printf("%tc\n", new Date()); // tc:完整的日期格式
        System.out.printf("%s %tB %<te %<tY", "Due Date:", new Date()); // <:使用前一个参数作为输出参数，重复使用一个参数
        System.out.printf("\n%1$s %2$tB %2$te, %2$tY", "Due Date:", new Date()); // %1$：参数索引，指示使用第一个参数，也是方便参数复用
        // 格式化字符串
        String fmtStr = String.format("\nHello %s. Next year, you'll be %d.", "TP", 25);
        System.out.println(fmtStr);
    }

    // 使用Scanner读取文件，PrintWriter写文件
    private void fileOperation() throws IOException {
        PrintWriter out = new PrintWriter(".\\src\\JavaKernelVolume1\\myFile.txt", "UTF-8");
        out.println("hello everyone!");
        out.printf("%s %tB %<te %<tY", "Due Date:", new Date());
        out.close();

        Scanner in = new Scanner(Paths.get(".\\src\\JavaKernelVolume1\\myFile.txt"), String.valueOf(StandardCharsets.UTF_8));
        while (in.hasNext()) {
            System.out.println(in.nextLine());
        }
        in.close();
    }

    /* 3.数组 */
    // 快速打印数组
    private void printArray() {
        int[] a = new int[]{1, 2, 3, 4, 5};
        int[] b = {1, 2, 3, 4, 5}; // 创建数组对象并同时赋予初始值的简写方式
        int[] c = new int[0]; // 注意，数组长度为0 与null 不同
        for (int x : a) { // foreach:后者必须是实现了Iterable接口的可迭代对象
            System.out.print(x + " ");
        }
        System.out.println("\n" + Arrays.toString(a)); // 数组转为字符串后直接打印
    }

    // 拷贝数组
    private void copyArray() {
        int[] a = {2, 3, 4, 5};
        int[] b = Arrays.copyOf(a, a.length);
        System.out.println(Arrays.toString(b));
        a = Arrays.copyOf(a, 2 * a.length); // 通常用于扩展数组长度，多余的元素填0，布尔型填false
        System.out.println(Arrays.toString(a));
    }

    // 数组排序
    private void sortArray() {
        int n = 100;
        int[] numbers = new int[100]; // 号码池，用于抽数
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i + 1;
        }
        int k = 6;
        int[] result = new int[k];
        // 随机从numbers中抽取k个不重复的号码
        for (int i = 0; i < k; i++) {
            int r = (int) (Math.random() * n); // 0~n-1
            result[i] = numbers[r];
            // 将最后一元素移到被选中元素的位置，长度减一，避免重复选中
            numbers[r] = numbers[n - 1];
            n--;
        }
        Arrays.sort(result);
        for (int a : result) {
            System.out.print(a + " ");
        }
    }

    // 多维数组
    private void multiDimensionArray() {
        double[][] arr = new double[4][3];
        arr = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        // 使用foreach遍历二维数组需要2层嵌套
        for (double[] row : arr) {
            for (double e : row) {
                System.out.print(" " + e);
            }
        }
        // 快速打印2维数组
        System.out.println();
        System.out.println(Arrays.deepToString(arr));
    }


    public static void main(String[] args) throws IOException {
        // 命令行参数
        if (args.length > 0)
            for (String s : args) { // 源文件名字不包含在args中
                System.out.print(s + " ");
            }
        System.out.println();
        System.out.println("当前程序执行路径：" + System.getProperty("user.dir"));

        Chapter03 obj = new Chapter03();
        /*
        obj.formatOutput();
        obj.fileOperation();
        obj.printArray();
        obj.copyArray();
         */
        obj.sortArray();
        obj.multiDimensionArray();
    }
}
