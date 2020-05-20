package JavaKernelVolume1.ch08.restriction;

public abstract class Block {
    public abstract void body() throws Exception;
    public Thread toThread(){ // 将任务 body() 包装到 run方法执行
        return new Thread(){
            @Override
            public void run() { // run方法不能抛出异常，必须捕获所有受检查异常。
                try {
                    body(); // 将抽象方法留给外部调用实现
                } catch (Throwable e) {
                    Block.<RuntimeException>throwAs(e); // 虽然run不能抛出异常，但这里利用泛型还是抛出了异常
                }
            }
        };
    };

    // 将异常强制转为泛型T，让编译器认为不是异常类型
    public static <T extends Throwable> void throwAs(Throwable e) throws T{
        throw (T)e;
    }
}
