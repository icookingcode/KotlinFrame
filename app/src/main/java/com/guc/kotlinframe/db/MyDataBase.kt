package com.guc.kotlinframe.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.guc.kotlinframe.db.migration.Migration1to2
import com.guc.kotlinframe.db.migration.Migration2to3

/**
 * Created by guc on 2021/3/23.
 * Description：
 */
@Database(version = 3, entities = [FileCache::class, CrashInfo::class])
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
            ).addMigrations(Migration1to2(1, 2), Migration2to3(2, 3))
                .build().apply { instance = this }
        }
    }
}