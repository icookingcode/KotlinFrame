package com.guc.kframe

import android.content.Context
import com.guc.kframe.base.ActivityCollector
import com.guc.kframe.base.SystemManager
import com.guc.kframe.system.SystemCrash

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
        //启动必要system
        SystemManager.getSystem(SystemCrash::class.java)
    }


    //退出应用
    fun exit() {
        ActivityCollector.finishAll()
        SystemManager.destroyAllSystem()
    }
}