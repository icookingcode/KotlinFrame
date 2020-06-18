package com.guc.kframe.widget.pickerview.lib

import java.util.*
import kotlin.math.abs

/**
 * Created by guc on 2020/6/15.
 * Description：smooth scroll
 */
class SmoothScrollTimerTask(private val loopView: WheelView, private var offset: Int) :
    TimerTask() {
    private var realTotalOffSet = Int.MAX_VALUE
    private var realOffSet = 0

    override fun run() {
        if (realTotalOffSet == Int.MAX_VALUE) {
            realTotalOffSet = offset
        }
        realOffSet = (realTotalOffSet * 0.1).toInt() // divide into ten copies
        if (realOffSet == 0) {
            realOffSet = if (realTotalOffSet < 0) {
                -1
            } else {
                1
            }
        }
        if (abs(realTotalOffSet) <= 1) {
            loopView.cancelFuture()
            loopView.handler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED)
        } else {
            loopView.totalScrollY = loopView.totalScrollY + realOffSet

            if (!loopView.isLoop) {
                val itemHeight = loopView.itemHeight
                val top = -loopView.initPosition * itemHeight
                val bottom = (loopView.getItemCount() - 1 - loopView.initPosition) * itemHeight
                if (loopView.totalScrollY <= top || loopView.totalScrollY >= bottom) {
                    loopView.totalScrollY = loopView.totalScrollY - realOffSet
                    loopView.cancelFuture()
                    loopView.handler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED)
                    return
                }

            }
            loopView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW)
            realTotalOffSet -= realOffSet
        }
    }
}