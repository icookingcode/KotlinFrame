package com.guc.kotlinframe.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Created by guc on 2021/3/24.
 * Description：
 */
class Migration1to2(startVersion: Int, endVersion: Int) : Migration(startVersion, endVersion) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("alter table cache add column update_time INTEGER")
    }

}