package com.guc.kframe.widget.pickerview.adapter

/**
 * Created by guc on 2020/6/15.
 * Description：Numeric Wheel adapter.
 */
class NumericWheelAdapter(val minValue: Int, val maxValue: Int) :
    IWheelAdapter<Int> {
    companion object {
        const val DEFAULT_MIN_VALUE = 0
        const val DEFAULT_MAX_VALUE = 9
    }

    constructor() : this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE)

    override fun getItemsCount(): Int = maxValue - minValue + 1

    override fun getItem(index: Int): Int = if (index >= 0 && index < getItemsCount()) {
        minValue + index
    } else {
        -1
    }

    override fun indexOf(o: Any?): Int = o as Int - minValue
}