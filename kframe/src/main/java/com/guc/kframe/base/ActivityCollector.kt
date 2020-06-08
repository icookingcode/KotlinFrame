package com.guc.kframe.base

import android.app.Activity
import android.os.Process

/**
 * Created by guc on 2020/4/28.
 * 描述：管理Activity
 */
object ActivityCollector {
    private val activities = ArrayList<Activity>()
    private var currentActivity: Activity? = null

    fun addActivity(activity: Activity) {
        activities.add(activity)
        currentActivity = activity
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
        currentActivity = if (activities.isNotEmpty()) {
            activities[activities.size - 1]
        } else {
            null
        }
    }

    fun getCurrentActivity(): Activity? = currentActivity

    fun finishAll() {
        for (a in activities) {
            if (!a.isFinishing) a.finish()
        }
        activities.clear()
        currentActivity = null
        Process.killProcess(Process.myPid())
    }

}