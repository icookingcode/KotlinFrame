package com.guc.kotlinframe

import com.guc.kframe.utils.CacheManagerUtils
import com.guc.kframe.utils.DateTimeUtils
import com.guc.kframe.utils.hashMap
import org.junit.Test
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testHashMap() {
        val map = hashMap {
            this["guc"] = "你好"
            this["ok"] = "确定"
        }
        println(map)
    }

    @Test
    fun testCacheUtil() {
        val cache = CacheManagerUtils.getFolderSize(File("G:\\study"))
        println(cache)
        println(CacheManagerUtils.getFormatSize(cache.toDouble()))
    }

    @Test
    fun testAssets() {

    }


    @Test
    fun testDateUtils() {
        val df = SimpleDateFormat("yyyyMMdd")
        val calendar = Calendar.getInstance()

        val monday = DateTimeUtils.getMondayOfCurrentWeek(calendar, "yyyyMMdd")
        println(monday)

        val firstDay = DateTimeUtils.getFirstDayOfCurrentMonth(calendar)
        println(firstDay)

        val max = DateTimeUtils.getMaxDayByYearMonth(2019, 2)
        println("最大：$max 号")

    }

    private fun setDate(year: Int, month: Int, day: Int, calendar: Calendar) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
    }

    /**
     * 获取本周周一
     */
    private fun getMondayOfCurrentWeek(
        calendar: Calendar = Calendar.getInstance(),
        format: String = "yyyyMMdd"
    ): String {
        val df = SimpleDateFormat(format)
        val dayOfWeekToday = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DAY_OF_YEAR, -dayOfWeekToday + 2)
        return df.format(calendar.time)
    }

    /**
     * 获取本月1号
     */
    private fun getFirstDayOfCurrentMonth(
        calendar: Calendar = Calendar.getInstance(),
        format: String = "yyyyMMdd"
    ): String {
        val df = SimpleDateFormat(format)
        val dayOfMonthToday = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.add(Calendar.DAY_OF_YEAR, -dayOfMonthToday + 1)
        return df.format(calendar.time)
    }

    /**
     * 获取本月最大天数
     */
    private fun getMaxDayOfCurrentMonth(calendar: Calendar = Calendar.getInstance()) =
        calendar.getActualMaximum(Calendar.DATE)

    /**
     * 获取指定年月的最大天数
     */
    private fun getMaxDayByYearMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        return getMaxDayOfCurrentMonth(calendar)

    }

}
