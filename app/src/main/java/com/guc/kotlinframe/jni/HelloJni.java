package com.guc.kotlinframe.jni;

/**
 * Created by guc on 2021/4/8.
 * Description：jni测试
 */
public class HelloJni {
    // 动态导入 so 库
    static {
        System.loadLibrary("HelloJni");
    }

    public native static String get();
}
