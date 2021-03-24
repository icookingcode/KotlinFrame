package com.guc.kotlinframe.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Created by guc on 2021/3/24.
 * Description：
 */
class Migration2to3(startVersion: Int, endVersion: Int) : Migration(startVersion, endVersion) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("create table crash_info (uid integer primary key autoincrement not null ,crash_info text,crash_time integer not null  )")
    }
}