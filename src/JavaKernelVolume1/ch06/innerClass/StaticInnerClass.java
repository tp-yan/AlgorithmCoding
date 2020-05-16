package JavaKernelVolume1.ch06.innerClass;

public class StaticInnerClass {
    public static void main(String[] args) {

    }
}

class ArrayAlg {
    /* 在内部类不需要访问外围类对象的时候，应该使用静态内部类。
    与常规内部类不同，静态内部类可以有静态域和方法。
     */
    // 用于存放2个浮点数的 静态内部类
    public static class Pair{// 将Pair 定义为ArrayAlg 的内部公有类。此后，通过ArrayAlg.Pair 访问它
        private double first;
        private double second;

        public Pair(double first, double second) {
            this.first = first;
            this.second = second;
        }

        public double getFirst() {
            return first;
        }

        public double getSecond() {
            return second;
        }
    }

    public static Pair minmax(double[] values) {
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        for (double v : values) {
            if (min > v) min = v;
            if (max < v) max = v;
        }
        return new Pair(min,max);
    }
}
