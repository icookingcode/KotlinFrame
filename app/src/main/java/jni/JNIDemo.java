package jni;

/**
 * @Author: guc
 * @Description: JNIDemo
 * @Date: 2021/4/9 13:46
 * @Version: 1.0
 */
public class JNIDemo {
    static {
        System.loadLibrary("JNIDemo");
    }

    public native String testJni();
}
