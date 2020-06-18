package com.guc.kframe.widget.pickerview.lib

import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Created by guc on 2020/6/15.
 * Description：
 */
class LoopViewGestureListener(private val loopView: WheelView) :
    GestureDetector.SimpleOnGestureListener() {
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        loopView.scrollBy(velocityY)
        return true
    }

}