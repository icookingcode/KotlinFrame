package com.guc.kframe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by guc on 2020/7/20.
 * Description：自定义item的PagerAdapter
 */
abstract class CommonPagerAdapter<T>(val context: Context, var data: List<T>, val itemResId: Int) :
    PagerAdapter() {
    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun getCount(): Int = data.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =
            LayoutInflater.from(context).inflate(itemResId, null)
        bindData(view, position, data[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    abstract fun bindData(parent: View, position: Int, data: T)
}