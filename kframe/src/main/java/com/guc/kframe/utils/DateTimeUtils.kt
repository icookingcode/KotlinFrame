package com.guc.kframe.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val YYYYMMDDHHMMSS = "yyyyMMddHHmmss"
    const val YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
    const val HH_MM_SS = "HH:mm:ss"
    const val YYYY_MM_DD = "yyyy-MM-dd"
    var sdf = SimpleDateFormat()

    /**
     * timeMills format to String
     */
    fun timeMills2String(
        timeMills: Long,
        pattern: String = YYYY_MM_DD_HH_MM_SS,
        locale: Locale = Locale.getDefault(),
        timeZone: TimeZone = TimeZone.getTimeZone("GMT+8:00")
    ): String {
        val date = Date(timeMills)
        sdf = SimpleDateFormat(pattern, locale).apply {
            this.timeZone = timeZone
        }
        return try {
            sdf.format(date)
        } catch (e: Exception) {
            timeMills.toString()
        }
    }

    /**
     * date format to String
     */
    fun date2String(
        date: Date,
        pattern: String = YYYY_MM_DD_HH_MM_SS,
        locale: Locale = Locale.getDefault(),
        timeZone: TimeZone = TimeZone.getTimeZone("GMT+8:00")
    ): String {
        sdf = SimpleDateFormat(pattern, locale).apply {
            this.timeZone = timeZone
        }
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
        newPattern: String = YYYY_MM_DD_HH_MM_SS,
        locale: Locale = Locale.getDefault()
    ): String {
        val date: Date?
        sdf = SimpleDateFormat(oldPattern, locale)
        try {
            date = sdf.parse(timeStr)
        } catch (e: Exception) {
            return timeStr
        }
        return date2String(date, newPattern)
    }

    /**
     * @time:时间字符串
     * @pattern:格式
     * @return: null if parse except else date
     */
    fun timeString2Date(
        time: String,
        pattern: String,
        timeZone: TimeZone = TimeZone.getTimeZone("GMT+8:00")
    ): Date? =
        try {
            sdf = SimpleDateFormat(pattern).apply {
                this.timeZone = timeZone
            }
            sdf.parse(time)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    /**
     * 获取本周周一
     */
    fun getMondayOfCurrentWeek(
        calendar: Calendar = Calendar.getInstance(),
        format: String = "yyyyMMdd",
        locale: Locale = Locale.getDefault()
    ): String {
        sdf = SimpleDateFormat(format, locale)
        val dayOfWeekToday = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DAY_OF_YEAR, -dayOfWeekToday + 2)
        return sdf.format(calendar.time)
    }

    /**
     * 获取本月1号
     */
    fun getFirstDayOfCurrentMonth(
        calendar: Calendar = Calendar.getInstance(),
        format: String = "yyyyMMdd",
        locale: Locale = Locale.getDefault()
    ): String {
        sdf = SimpleDateFormat(format, locale)
        val dayOfMonthToday = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.add(Calendar.DAY_OF_YEAR, -dayOfMonthToday + 1)
        return sdf.format(calendar.time)
    }

    /**
     * 获取本月最大天数
     */
    fun getMaxDayOfCurrentMonth(calendar: Calendar = Calendar.getInstance()) =
        calendar.getActualMaximum(Calendar.DATE)

    /**
     * 获取指定年月的最大天数
     */
    fun getMaxDayByYearMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        return getMaxDayOfCurrentMonth(calendar)

    }
}

