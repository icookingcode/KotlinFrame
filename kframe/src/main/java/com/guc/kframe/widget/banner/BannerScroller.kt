package com.guc.kframe.widget.banner

import android.content.Context
import android.os.Build
import android.view.animation.Interpolator
import android.widget.Scroller

/**
 * Created by guc on 2020/6/11.
 * Description：BannerScroller  control the scroll
 */
class BannerScroller(
    context: Context,
    interpolator: Interpolator? = null,
    flywheel: Boolean = context.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.HONEYCOMB
) : Scroller(context, interpolator, flywheel) {
    var mDuration: Int = 800
    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }


}