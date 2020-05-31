package JianzhiOffer.Chapter02.DataStruct;

import java.util.Arrays;

public class Q05ReplaceBlank {
    /*
     面试题5：替换空格
     题目：请实现一个函数，把字符串中的每个空格替换成"%20"。例如输入“We are happy.”，
     则输出“We%20are%20happy.”。
 */
    /*
     传入的字符串最后一个字符为'\0'表示结尾，不属于字符串内容。且题目需要，str数组后面有足够的空闲内存空间
     */
    public static void replaceBlank(char[] str) {
        if (str == null || str.length < 1) return;
        //1.首先求原字符串的长度
        int index = 0;
        int originalLength = 0;
        int countSpace = 0;
        while (str[index] != '\0') {
            if (str[index++] == ' ')// 统计空格个数
                countSpace++;
            originalLength++;
        }
        int newLength = originalLength + 2 * countSpace; // 新字符串比原长多：2*空格数
        if (newLength > str.length) return; // 必须保证传入的 str数组，能够容纳新字符串

        int pointer1 = originalLength;
        int pointer2 = newLength;
        // 从后往前复制原内容，使用 %20代替空格
        while (pointer1 >= 0 && pointer2 > pointer1) {// 若 pointer2==pointer1，说明pointer2赶上了pointer1，则处理完所有的空格了
            if (str[pointer1] == ' ') {
                str[pointer2--] = '0';
                str[pointer2--] = '2';
                str[pointer2--] = '%';
            } else {//原样拷贝
                str[pointer2--] = str[pointer1];
            }
            pointer1--;
        }
    }

    /*
     相关题目：2个有序数组A1，A2，把A2插入到A1中保持有序，A1有足够多余空间。
     注：要求arr1末尾带\0，而arr2末尾不带\0
     */
    public static void mergeArray(char[] arr1, char[] arr2) {
        if (arr1 == null || arr1.length < 1 || arr2 == null || arr2.length < 1) return;
        int index = 0;
        int arr1Length = 0;
        // arr1有效长度，不含\0
        while (arr1[index++] != '\0') arr1Length++;
        int totalLength = arr1Length + arr2.length;
        if (totalLength > arr1.length) return;

        index = totalLength - 1;
        int pointer1 = arr1Length - 1;
        int pointer2 = arr2.length - 1;
        while (pointer1 >= 0 && pointer2 >= 0) {
            if (arr1[pointer1] >= arr2[pointer2]) {
                arr1[index--] = arr1[pointer1--];
            } else {
                arr1[index--] = arr2[pointer2--];
            }
        }
        while (pointer1 >= 0) {
            arr1[index--] = arr1[pointer1--];
        }
        while (pointer2 >= 0) {
            arr1[index--] = arr2[pointer2--];
        }
    }


    public static void main(String[] args) {
        Test1();
        Test2();
        Test3();
        Test4();
        Test5();
        Test6();
        Test7();
        Test8();
        Test9();

        char[] arr1 = new char[100];
        String str1 = "Qabefg\0";
        helper(arr1, str1);
        mergeArray(arr1, "Acdek".toCharArray());
        System.out.println(arr1);
    }

    // ====================测试代码====================
    static void Test(String testName, char str[], int length, char expected[]) {
        if (testName != null)
            System.out.printf("%s begins: ", testName);

        replaceBlank(str);

        StringBuilder sb = new StringBuilder();
        String result = "";
        if (str != null) {
            int index = 0;
            while (str[index] != '\0') sb.append(str[index++]);
            sb.append('\0');
            result = sb.toString();
        }

        if (expected == null && str == null)
            System.out.printf("passed.\n");
        else if (expected == null && str != null)
            System.out.printf("failed.\n");
        else if (Arrays.equals(result.toCharArray(), expected))
            System.out.printf("passed.\n");
        else
            System.out.printf("failed.\n");
    }

    static void helper(char[] str, String s) {
        System.arraycopy(s.toCharArray(), 0, str, 0, s.length());
    }

    // 空格在句子中间
    static void Test1() {
        int length = 100;
        char[] str = new char[length];
        String s = "hello world\0";
        helper(str, s);
        System.out.println(str);
        System.out.println(str.length);
        int count = 0;
        for (char c : str) {
            System.out.println(count + ":" + c);
            count++;
            if (count >= s.length()) break;
        }

        Test("Test1", str, length, "hello%20world\0".toCharArray());
    }

    // 空格在句子开头
    static void Test2() {
        int length = 100;
        char[] str = new char[length];
        String s = " helloworld\0";
        helper(str, s);

        Test("Test2", str, length, "%20helloworld\0".toCharArray());
    }

    // 空格在句子末尾
    static void Test3() {
        int length = 100;
        char[] str = new char[length];
        String s = "helloworld \0";
        helper(str, s);

        Test("Test3", str, length, "helloworld%20\0".toCharArray());
    }

    // 连续有两个空格
    static void Test4() {
        int length = 100;
        char[] str = new char[length];
        String s = "hello  world\0";
        helper(str, s);

        Test("Test4", str, length, "hello%20%20world\0".toCharArray());
    }

    // 传入null
    static void Test5() {
        Test("Test5", null, 0, null);
    }

    // 传入内容为空的字符串
    static void Test6() {
        int length = 100;
        char[] str = new char[length];
        String s = "\0";
        helper(str, s);
        Test("Test6", str, length, "\0".toCharArray());
    }

    //传入内容为一个空格的字符串
    static void Test7() {
        int length = 100;
        char[] str = new char[length];
        String s = " \0";
        helper(str, s);

        Test("Test7", str, length, "%20\0".toCharArray());
    }

    // 传入的字符串没有空格
    static void Test8() {
        int length = 100;
        char[] str = new char[length];
        String s = "helloworld\0";
        helper(str, s);
        Test("Test8", str, length, "helloworld\0".toCharArray());
    }

    // 传入的字符串全是空格
    static void Test9() {
        int length = 100;
        char[] str = new char[length];
        String s = "   \0";
        helper(str, s);
        Test("Test9", str, length, "%20%20%20\0".toCharArray());
    }
}
