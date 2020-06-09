package com.guc.kframe.system

import com.guc.kframe.Engine
import com.guc.kframe.base.BaseSystem
import com.guc.kframe.system.download.DownloadTask
import com.guc.kframe.system.download.Task

/**
 * Created by guc on 2020/6/9.
 * 描述：下载管理
 */
class SystemDownload : BaseSystem() {
    private var taskList: HashMap<String, DownloadTask>? = null
    override fun initSystem() {
        taskList = HashMap()
    }

    override fun destroy() {
        taskList = null
    }

    fun download(task: Task, callback: ((Task) -> Unit)?) {
        val downloadTask = DownloadTask(Engine.context) { task ->
            if (task.status == DownloadTask.STATUS_SUCCESS) {
                taskList?.remove(task.url)
            }
            callback?.let { it(task) }
        }
        downloadTask.execute(task)
        taskList?.put(task.url, downloadTask)
    }

    fun pause(task: Task) {
        taskList?.get(task.url)?.isPaused = true
    }
}