package com.guc.kframe.system.download

import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * Created by guc on 2020/6/8.
 * 描述：下载任务
 */
class DownloadTask(val context: Context, val callback: (Task) -> Unit) :
    AsyncTask<Task, Task, Task>() {
    var isCanceled = false
    var isPaused = false
    var lastProgress = 0
    var file: File? = null

    companion object {
        const val STATUS_WAIT = -1
        const val STATUS_SUCCESS = 0
        const val STATUS_FAILED = 1
        const val STATUS_PAUSED = 2
        const val STATUS_CANCEL = 3
        const val STATUS_LOADING = 4 //下载中
    }

    //后台任务开始执行前调用
    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Task?): Task {
        var inputStream: InputStream? = null
        var savedFile: RandomAccessFile? = null
        var file: File? = null
        var task: Task? = null

        try {

            var downloadedLength = 0L
            task = params[0]
            val url = task?.url ?: throw IllegalArgumentException("file url is null")
            val fileName = url.substring(url.lastIndexOf("/"))
            val dictionary = task.downloadPath ?: Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ).path
            file = File(dictionary + fileName)

            if (file.exists()) {
                downloadedLength = file.length()
            } else {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            task.filePath = file.absolutePath
            this.file = file
            val contentLength = getContentLength(url)
            if (contentLength == 0L) {
                return task.apply { this.status = STATUS_FAILED }
            } else if (contentLength == downloadedLength) {
                return task.apply {
                    this.status = STATUS_SUCCESS
                    this.progress = 100
                }
            }
            val client = OkHttpClient()
            val request = Request.Builder()
                //断点下载
                .addHeader("RANGE", "bytes=$downloadedLength-$contentLength")
                .url(url)
                .build()
            val response = client.newCall(request).execute()
            inputStream = response.body?.byteStream()
            if (inputStream == null) {
                return task.apply { this.status = STATUS_FAILED }
            }
            savedFile = RandomAccessFile(file, "rw")
            savedFile.seek(downloadedLength)
            val b = ByteArray(1024)
            var total = 0
            var delSize = 0
            var longTimeMillis: Long = 0
            inputStream.use {
                longTimeMillis = System.currentTimeMillis()
                var len = it.read(b)
                while ((len) != -1) {
                    if (isCanceled) return task.apply { this.status = STATUS_CANCEL }
                    else if (isPaused) return task.apply { this.status = STATUS_PAUSED }
                    else {
                        total += len
                        savedFile.write(b, 0, len)
                        val progress = ((total + downloadedLength) * 100 / contentLength).toInt()
                        publishProgress(task.apply {
                            this.progress = progress
                            this.status = STATUS_LOADING
                            if (System.currentTimeMillis() - longTimeMillis < 1000) {
                                delSize += len
                            } else {
                                this.speed = calculateSpeed(
                                    System.currentTimeMillis(),
                                    longTimeMillis,
                                    delSize
                                )
                                longTimeMillis = System.currentTimeMillis()
                                delSize = 0
                            }
                        })
                        len = it.read(b)
                    }

                }
                return task.apply {
                    this.status = STATUS_SUCCESS
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            inputStream?.close()
            savedFile?.close()
            if (isCanceled) {
                file?.delete()
            }
        }
        return Task(url = task?.url ?: "", status = STATUS_FAILED)
    }

    //运行在主线程，可对UI进行更新进度
    override fun onProgressUpdate(vararg values: Task?) {
        val progress = values[0] ?: return
        if (progress.progress > lastProgress) {
            callback(progress)
            lastProgress = progress.progress
        }
    }

    //后台任务执行完毕后调用
    override fun onPostExecute(result: Task?) {
        result?.let { callback(it) }
    }

    private fun getContentLength(url: String): Long {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val contentLength = response.body?.contentLength() ?: 0
            response.body?.close()
            return contentLength

        }
        return 0L
    }

    /**
     * 网速计算
     */
    private fun calculateSpeed(endTime: Long, startTime: Long, size: Int): String {
        val delTime = endTime - startTime
        if (delTime.toInt() == 0) return "0 Kb/s"
        val speed = size * 1.0 / (delTime / 1000) / 1024
        return if (speed > 1024) {
            String.format("%.2fMb/s", speed / 1024)
        } else {
            String.format("%.2fKb/s", speed)
        }
    }

}