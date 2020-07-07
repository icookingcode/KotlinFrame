package com.guc.kframe.utils

import android.content.Context
import android.content.SharedPreferences
import com.guc.kframe.Engine

/**
 * Created by guc on 2020/7/7.
 * Description：SharePreferences 工具
 */
object SPUtils {
    private lateinit var preference: SharedPreferences

    fun getSharedPreferences(
        context: Context = Engine.context,
        spName: String = "sp_kframe",
        mode: Int = Context.MODE_PRIVATE
    ): SharedPreferences {
        preference = context.getSharedPreferences(spName, mode)
        return preference
    }

    fun <T> putValue(key: String, t: T) {
        if (isInit()) {
            preference.edit().apply {
                when (t) {
                    is Int -> putInt(key, t)
                    is Long -> putLong(key, t)
                    is Boolean -> putBoolean(key, t)
                    is String -> putString(key, t)
                    is Float -> putFloat(key, t)
                    is Double -> putFloat(key, t.toFloat())
                }
                apply()
            }
        } else {
            throw Exception("not init,please use SharedPreferencesUtils.init()")
        }
    }

    fun <T> getValue(key: String, defaultValue: T): T {
        if (isInit()) {
            return when (defaultValue) {
                is Int -> preference.getInt(key, defaultValue)
                is Long -> preference.getLong(key, defaultValue)
                is Boolean -> preference.getBoolean(key, defaultValue)
                is String -> preference.getString(key, defaultValue)
                is Float -> preference.getFloat(key, defaultValue)
                is Double -> preference.getFloat(key, defaultValue.toFloat()).toDouble()
                else -> defaultValue
            } as T
        } else {
            throw Exception("not init,please use SharedPreferencesUtils.init()")
        }
    }

    fun getValue(key: String, default: String? = null): String? {
        if (isInit()) {
            return preference.getString(key, default)
        } else {
            throw Exception("not init,please use SharedPreferencesUtils.init()")
        }
    }

    fun clear() {
        if (isInit()) {
            preference.edit().clear().apply()
        } else {
            throw Exception("not init,please use SharedPreferencesUtils.init()")
        }
    }

    private fun isInit(): Boolean {
        return ::preference.isInitialized
    }
}