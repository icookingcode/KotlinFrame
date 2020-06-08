package com.guc.kframe.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Created by guc on 2020/6/8.
 * 描述：可禁止滑动的ViewPager
 */
class NoScrollViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    var canScroll: Boolean = false

    constructor(context: Context) : this(context, null)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean =
        if (canScroll) super.onInterceptTouchEvent(ev) else false
}