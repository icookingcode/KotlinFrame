package com.guc.kotlinframe

import android.app.Application
import com.guc.kframe.Engine

/**
 * Created by guc on 2020/5/22.
 * 描述：程序入口
 */
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Engine.init(this)
    }
}