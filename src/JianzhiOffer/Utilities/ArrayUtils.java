package JianzhiOffer.Utilities;

public class ArrayUtils {
    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 随机数发生器：生成随机数组用于测试，有正有负
     *
     * @param maxlen 随机的数组长度
     * @param max    最大值
     * @return
     */
    public static int[] genRandArr(int maxlen, int max) {
        int[] arr = new int[maxlen]; // 长度随机
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (max + 1)) - (int) (max * Math.random());
        }
        return arr;
    }

    /**
     * 生成随机正数数组
     */
    public static int[] genPosRandArr(int len, int max) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * (max + 1));
        }
        return arr;
    }

}
