//
// Created by guc on 2021/4/8.
//
/*
 * Class:     com_guc_kotlinframe_jni_HelloJni
 * Method:    get
 * Signature: ()Ljava/lang/String;
 */
 #include<jni.h>
 #include<stdio.h>

 #include "com_guc_kotlinframe_jni_HelloJni.h"
JNIEXPORT jstring JNICALL Java_com_guc_kotlinframe_jni_HelloJni_get
  (JNIEnv *env, jclass jclass){
    //返回一个字符串
          return (*env)->NewStringUTF(env,"This is my first NDK Application");
  }
