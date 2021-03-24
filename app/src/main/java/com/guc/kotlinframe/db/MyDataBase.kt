package com.hnyf.spc.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.guc.kotlinframe.db.FileCache

/**
 * Created by guc on 2021/3/23.
 * Description：
 */
@Database(version = 1, entities = [FileCache::class])
abstract class MyDataBase : RoomDatabase() {
    abstract fun fileCacheDao(): CacheDao

    companion object {
        private const val DB_NAME = "spc.db"
        private var instance: MyDataBase? = null

        @Synchronized
        fun getInstance(context: Context): MyDataBase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                MyDataBase::class.java,
                DB_NAME //数据库名
            ).build().apply { instance = this }
        }
    }
}