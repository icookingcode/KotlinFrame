package com.guc.kotlinframe.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by guc on 2021/3/24.
 * Description：
 */
@Entity(tableName = "crash_info")
data class CrashInfo(
    @ColumnInfo(name = "crash_info") val crashInfo: String?,
    @ColumnInfo(name = "crash_time") var crashTime: Long
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}