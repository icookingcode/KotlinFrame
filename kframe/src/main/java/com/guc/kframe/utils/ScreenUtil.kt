package com.guc.kframe.utils

import android.content.Context
import android.content.res.Configuration
import com.guc.kframe.Engine

/**
 * Created by guc on 2020/4/29.
 * 描述：屏幕工具
 */
object ScreenUtil {
    //判断当前设备是否是平板
    fun isPad(context: Context = Engine.context): Boolean =
        (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE

    /**
     * dp -> px
     */
    fun dp2px(dp: Int, context: Context = Engine.context): Int =
        (context.resources.displayMetrics.density * dp + 0.5).toInt()

    /**
     * px -> dp
     */
    fun px2dp(px: Int, context: Context = Engine.context): Int =
        (px / context.resources.displayMetrics.density + 0.5f).toInt()

    /**
     * sp -> px
     */
    fun sp2px(sp: Int, context: Context = Engine.context): Int =
        (context.resources.displayMetrics.scaledDensity * sp + 0.5).toInt()

    /**
     * px -> sp
     */
    fun px2sp(px: Int, context: Context = Engine.context): Int =
        (px / context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
}