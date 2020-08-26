package com.guc.kotlinframe.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by guc on 2020/8/26.
 * Description：定位服务
 */
class MyNavigationService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}