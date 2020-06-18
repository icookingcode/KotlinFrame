package com.guc.kframe.widget.pickerview.lib

internal class OnItemSelectedRunnable(val loopView: WheelView) : Runnable {
    override fun run() {
        loopView.onItemSelectedListener?.let {
            it(loopView.selectedItem)
        }
    }

}