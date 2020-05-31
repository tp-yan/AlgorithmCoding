public class Test {
    public static void modularBit(int value, int m) {
        System.out.println((value % m) == (value & (m - 1)));
    }

    public static void main(String[] args) {
        int[] values = {10, 12, 15, 17, 22, 68, 1234, 8910};
        for (int i = 0; i < values.length; i++) {
            for (int j = 2; j < 8; j++) {
                int m = (int) Math.pow(2,j);
                modularBit(values[i], j);
            }
        }
    }
}
