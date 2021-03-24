package com.guc.kotlinframe.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by guc on 2021/3/23.
 * Description：文件缓存对象
 */
@Entity(tableName = "cache")
data class FileCache(
    @ColumnInfo(name = "file_id") val fileId: String?,
    @ColumnInfo(name = "local_path") var localPath: String?,
    @ColumnInfo(name = "create_time") var createTime: Long
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}