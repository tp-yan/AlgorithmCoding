package JavaKernelVolume1.ch08.restriction;

import java.io.File;
import java.util.Scanner;

public class BlockTest {
    public static void main(String[] args) {
        /* 1.利用泛型消除编译器对受查异常的检查 */
        new Block() {
            @Override
            public void body() throws Exception {
                Scanner in = new Scanner(new File("ququx"), "UTF-8");
                while (in.hasNext())
                    System.out.println(in.next());
            }
        }.toThread().start();
    }
}
