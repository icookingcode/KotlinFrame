package com.guc.kframe.system.download

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * Created by guc on 2020/6/8.
 * 描述：下载服务
 */
class ServiceDownload : Service() {
    val iBinder = DownloadBinder()

    companion object {
        const val TAG = "ServiceDownload"
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? = iBinder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    inner class DownloadBinder : Binder() {
        var callback: ((Task) -> Unit)? = null
        var downloadTask: DownloadTask? = null

        //添加监听
        fun registerCallback(callback: (Task) -> Unit) {
            this.callback = callback
        }

        fun download(task: Task) {
            downloadTask = DownloadTask(this@ServiceDownload) { task ->
                callback?.let {
                    it(task)
                }
            }
            downloadTask?.execute(task)
        }

        fun paused() {
            downloadTask?.isPaused = true
        }

        fun cancel() {
            downloadTask?.isCanceled = true
        }

    }
}