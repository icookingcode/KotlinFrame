package com.guc.kframe.widget.pickerview.lib

import android.os.Handler
import android.os.Message

/**
 * Created by guc on 2020/6/15.
 * Description：
 */
class MessageHandler(private val loopView: WheelView) : Handler() {
    companion object {
        const val WHAT_INVALIDATE_LOOP_VIEW = 1000
        const val WHAT_SMOOTH_SCROLL = 2000
        const val WHAT_ITEM_SELECTED = 3000
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when (msg.what) {
            WHAT_INVALIDATE_LOOP_VIEW -> loopView.invalidate()
            WHAT_SMOOTH_SCROLL -> loopView.smoothScroll(WheelView.ACTION.FLING)
            WHAT_ITEM_SELECTED -> loopView.onItemSelected()
        }
    }
}