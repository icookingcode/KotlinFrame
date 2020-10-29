package com.guc.kframe.system.download

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by guc on 2020/6/8.
 * 描述：下载任务
 */
@Parcelize
data class Task(
    val url: String,
    var progress: Int = -1,
    var status: Int = DownloadTask.STATUS_WAIT,
    var filePath: String = "",
    var downloadPath: String? = null,
    var speed: String = "0Kb/s"
) : Parcelable