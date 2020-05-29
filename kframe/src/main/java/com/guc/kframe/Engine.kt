package com.guc.kframe

import android.content.Context
import com.guc.firstlinecode.utils.SharedPreferencesUtils
import com.guc.kframe.base.ActivityCollector

/**
 * Created by guc on 2020/5/22.
 * 描述：应用核心
 */
object Engine {
    lateinit var context: Context
    lateinit var config: Config

    fun init(context: Context, config: Config) {
        this.context = context
        this.config = config
        SharedPreferencesUtils.init(name = "kframe")
    }


    //退出应用
    fun exit() {
        ActivityCollector.finishAll()
    }
}