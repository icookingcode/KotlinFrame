package com.guc.kotlinframe

import android.app.Application
import com.guc.kframe.Config
import com.guc.kframe.Engine
import com.guc.kframe.base.SystemManager
import com.guc.kframe.system.SystemWaterMark

/**
 * Created by guc on 2020/5/22.
 * 描述：程序入口
 */
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = Config().apply {
            currentMode = Config.MODEL_DEBUG
            urlDebug = "http://192.168.44.141:8099/"
            urlBeta = "http://192.168.44.141:8099/"
            urlRelease = "http://192.168.44.141:8099/"
        }
        Engine.init(this, config)

        SystemManager.getSystem(SystemWaterMark::class.java)?.apply {
            angle = -30
            enable = true
            text = "自定义水印"
        }
    }
}