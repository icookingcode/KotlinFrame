package com.guc.kframe.widget.pickerview.lib

import java.util.*
import kotlin.math.abs

/**
 * Created by guc on 2020/6/15.
 * Description：scroll inertia
 */
class InertiaTimerTask(private val loopView: WheelView, private val velocityY: Float) :
    TimerTask() {
    private var a = Int.MAX_VALUE.toFloat()
    override fun run() {
        if (a == Int.MAX_VALUE.toFloat()) {
            a = if (abs(velocityY) > 2000f) {
                if (velocityY > 0.0f) {
                    2000f
                } else {
                    (-2000f)
                }
            } else {
                velocityY
            }
        }
        if (abs(a) in 0f..20f) {
            loopView.cancelFuture()
            loopView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL)
            return
        }
        val i = (a * 10f / 1000f).toInt()
        loopView.totalScrollY = loopView.totalScrollY - i
        if (!loopView.isLoop) {
            val itemHeight = loopView.itemHeight
            var top = -loopView.initPosition * itemHeight
            var bottom: Float =
                (loopView.getItemCount() - 1 - loopView.initPosition) * itemHeight
            if (loopView.totalScrollY - itemHeight * 0.25 < top) {
                top = loopView.totalScrollY + i
            } else if (loopView.totalScrollY + itemHeight * 0.25 > bottom) {
                bottom = loopView.totalScrollY + i
            }

            if (loopView.totalScrollY <= top) {
                a = 40f
                loopView.totalScrollY = top
            } else if (loopView.totalScrollY >= bottom) {
                loopView.totalScrollY = bottom
                a = -40f
            }
        }
        a = if (a < 0.0f) {
            a + 20f
        } else {
            a - 20f
        }
        loopView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW)
    }
}