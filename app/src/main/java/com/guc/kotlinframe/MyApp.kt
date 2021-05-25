package com.guc.kotlinframe

import android.app.Application
import com.guc.kframe.Config
import com.guc.kframe.Engine
import com.guc.kframe.base.SystemManager
import com.guc.kframe.system.SystemWaterMark
import com.guc.kframe.utils.LogG
import com.guc.kframe.utils.RunStateRegister
import com.guc.kframe.utils.ScreenUtils
import com.guc.kframe.utils.ToastUtil
import com.guc.kotlinframe.db.FileCache
import com.guc.kotlinframe.db.MyDataBase
import com.guc.kotlinframe.utils.SignatureUtil
import jni.JNIDemo
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by guc on 2020/5/22.
 * 描述：程序入口
 */
class MyApp : Application() {
    companion object {
        private val TAG = "MyApp"
    }

    override fun onCreate() {
        super.onCreate()
        val config = Config().apply {
            currentMode = BuildConfig.MODE
            urlDebug = "http://192.168.181.128/"
            urlBeta = "http://192.168.181.128/"
            urlRelease = "http://192.168.181.128/"
        }
        Engine.init(this, config)

        SystemManager.getSystem<SystemWaterMark>()?.apply {
            angle = 30
            enable = true
            text = "自定义水印\n第二行"
            lineHeight = ScreenUtils.dp2px(100)
            textAlpha = 0.2f
            lineSpace = 15
        }
        RunStateRegister.register(this, object : RunStateRegister.StateCallback {
            override fun onBackground() {
                ToastUtil.toast("KotlinFrame已后台运行")
            }

            override fun onForeground() {
            }
        })
        initDataBase()
        testSignature()
    }

    /**
     * 测试签名
     */
    private fun testSignature() {
        SignatureUtil.getSignature(this)
        LogG.loge(TAG, JNIDemo().testJni())
    }

    private fun initDataBase() {
        thread {
            val cacheDao = MyDataBase.getInstance(this).fileCacheDao()
            val fileId = UUID.randomUUID().toString()
            cacheDao.getCacheByFileId(fileId)?.apply {
                createTime = System.currentTimeMillis()
                cacheDao.updateCache(this)
                LogG.loge(TAG, "更新:${this}")
            } ?: run {
                cacheDao.insertAll(
                    FileCache(
                        fileId,
                        externalCacheDir?.absolutePath,
                        System.currentTimeMillis(), System.currentTimeMillis()
                    )
                )
                LogG.loge(TAG, "插入")
            }
        }
    }
}