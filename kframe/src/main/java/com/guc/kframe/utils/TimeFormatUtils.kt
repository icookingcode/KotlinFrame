package com.guc.kframe.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatUtils {
    const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val YYYYMMDDHHMMSS = "yyyyMMddHHmmss"
    const val YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
    const val HH_MM_SS = "HH:mm:ss"
    const val YYYY_MM_DD = "yyyy-MM-dd"
    var sdf = SimpleDateFormat()

    /**
     * timeMills format to String
     */
    fun timeMills2String(timeMills: Long, pattern: String = YYYY_MM_DD_HH_MM_SS): String {
        val date = Date(timeMills)
        sdf = SimpleDateFormat(pattern)
        return try {
            sdf.format(date)
        } catch (e: Exception) {
            timeMills.toString()
        }
    }

    /**
     * date format to String
     */
    fun date2String(date: Date, pattern: String = YYYY_MM_DD_HH_MM_SS): String {
        sdf = SimpleDateFormat(pattern)
        return try {
            sdf.format(date)
        } catch (e: Exception) {
            date.toString()
        }
    }

    /**
     * 格式转换
     * @timeStr:时间字符串
     * @oldPattern:原格式
     * @newPattern:新格式
     */
    fun formatConversion(
        timeStr: String,
        oldPattern: String,
        newPattern: String = YYYY_MM_DD_HH_MM_SS
    ): String {
        val date: Date
        sdf = SimpleDateFormat(oldPattern)
        try {
            date = sdf.parse(timeStr)
        } catch (e: Exception) {
            return timeStr;
        }
        return date2String(date, newPattern)
    }

    /**
     * @time:时间字符串
     * @pattern:格式
     * @return: null if parse except else date
     */
    fun timeString2Date(time: String, pattern: String): Date? =
        try {
            sdf = SimpleDateFormat(pattern)
            sdf.parse(time)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}

