package com.guc.kframe.widget.pickerview.adapter

/**
 * Created by guc on 2020/6/15.
 * Description：The simple Array wheel adapter
 */
class ArrayWheelAdapter<T>(var items: List<T>) :
    IWheelAdapter<T> {
    companion object {
        const val DEFAULT_LENGTH = 4
    }

    override fun getItemsCount(): Int = items.size

    override fun getItem(index: Int): T = items[index]

    override fun indexOf(o: Any?): Int = items.indexOf(o)
}