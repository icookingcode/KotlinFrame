package com.guc.kframe

import android.content.Context
import com.guc.firstlinecode.utils.SharedPreferencesUtils

/**
 * Created by guc on 2020/5/22.
 * 描述：应用核心
 */
object Engine {
    lateinit var context: Context

    fun init(context: Context){
        this.context = context
        SharedPreferencesUtils.init(name = "kframe")
    }
}