package com.guc.kframe.base

import android.content.Context
import com.guc.kframe.Engine

/**
 * Created by guc on 2020/5/28.
 * 描述：系统基类
 */
open abstract class BaseSystem : ISystem {
    protected val TAG = BaseSystem::class.java.simpleName
    protected lateinit var context: Context
    override fun createSystem() {
        context = Engine.context
        initSystem()
    }

    override fun destroySystem() {
        destroy()
    }

    protected abstract fun initSystem()
    protected abstract fun destroy()

    protected fun <T : BaseSystem> getSystem(clazz: Class<T>): T? = SystemManager.getSystem(clazz)
}