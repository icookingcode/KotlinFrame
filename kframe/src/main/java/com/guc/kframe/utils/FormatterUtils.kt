package com.guc.kframe.utils

import java.util.*

/**
 * Created by guc on 2020/6/8.
 * 描述：格式化工具
 */
object FormatterUtils {

    fun format(double: Double, pattern: String = "%.2f"): String = format(pattern, double)

    fun format(pattern: String, vararg args: Any?): String =
        Formatter().format(pattern, *args).toString()
}