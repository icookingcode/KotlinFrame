package com.guc.kotlinframe.db

import androidx.room.*

/**
 * Created by guc on 2021/3/23.
 * Description：缓存  --访问数据库的接口
 */
@Dao
interface CacheDao {
    @Query("SELECT * FROM cache")
    fun getAll(): List<FileCache>

    @Insert
    fun insertAll(vararg caches: FileCache)

    @Delete
    fun delete(cache: FileCache)

    @Query("SELECT * FROM cache where file_id =:fileId")
    fun getCacheByFileId(fileId: String): FileCache?

    @Update
    fun updateCache(cache: FileCache)
}