package com.guc.kframe.widget.pickerview.adapter

/**
 * Created by guc on 2020/6/15.
 * Description：Adapter interface
 */
interface IWheelAdapter<T> {
    /**
     * get items count
     */
    fun getItemsCount(): Int

    /**
     * get the item by index
     */
    fun getItem(index: Int): T

    /**
     * get the index of item
     */
    fun indexOf(o: Any?): Int
}