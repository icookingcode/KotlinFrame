package com.guc.kframe.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Created by guc on 2020/10/28.
 * Description：App运行状态监测 后台/前台运行
 */
object RunStateRegister {
    fun register(application: Application, callback: StateCallback) {
        application.registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks() {
            private var startedActivityCount = 0
            override fun onActivityStarted(activity: Activity) {
                if (startedActivityCount == 0) { //前台运行
                    callback.onForeground()
                }
                startedActivityCount++
            }

            override fun onActivityStopped(activity: Activity) {
                startedActivityCount--
                if (startedActivityCount == 0) {
                    callback.onBackground()
                }
            }
        })
    }

    /**
     * Created by Guc on 2020/10/28.
     * Description：前台/后台运行回调
     */
    interface StateCallback {
        fun onForeground()
        fun onBackground()
    }

    open class SimpleActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

    }
}