package com.guc.kframe.widget.banner

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Created by guc on 2020/6/11.
 * Description：Banner ViewPager
 */
class BannerViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    constructor(context: Context) : this(context, null)

    var scrollable = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean = if (scrollable)
        super.onTouchEvent(ev)
    else
        false

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean = if (scrollable)
        super.onInterceptHoverEvent(event)
    else
        false
}