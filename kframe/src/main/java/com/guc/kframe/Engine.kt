package com.guc.kframe

import android.content.Context
import android.os.Process
import com.guc.kframe.base.ActivityCollector
import com.guc.kframe.base.SystemManager
import com.guc.kframe.system.SystemCrash
import com.guc.kframe.utils.LogG

/**
 * Created by guc on 2020/5/22.
 * 描述：应用核心
 */
object Engine {
    lateinit var context: Context
    lateinit var config: Config

    fun init(context: Context, config: Config) {
        this.context = context.applicationContext
        if (config.logNativeDir.isEmpty()) {
            config.logNativeDir =
                context.externalCacheDir?.absolutePath ?: context.cacheDir.absolutePath
        }
        this.config = config
        //设置log开关
        LogG.logOpen = config.logEnable
        //启动必要system
        SystemManager.getSystem(SystemCrash::class.java)
    }


    //退出应用
    fun exit() {
        SystemManager.destroyAllSystem()
        ActivityCollector.finishAll()
        Process.killProcess(Process.myPid())
    }
}