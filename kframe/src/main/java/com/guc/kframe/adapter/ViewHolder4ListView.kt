package com.guc.kframe.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by guc on 2020/4/29.
 * 描述：通用ListView的ViewHolder
 */
class ViewHolder4ListView(
    val parent: View,
    private val views: SparseArray<View> = SparseArray()
) {
    init {
        parent.tag = this
    }

    companion object {
        @JvmStatic
        fun get(
            context: Context,
            convertView: View?,
            parent: View?,
            layoutId: Int
        ): ViewHolder4ListView {
            val holder: ViewHolder4ListView
            holder = if (convertView == null) {
                val item = LayoutInflater.from(context).inflate(layoutId, null, false)
                ViewHolder4ListView(item)
            } else {
                convertView.tag as ViewHolder4ListView
            }
            return holder
        }
    }

    fun <T : View> getView(resId: Int): T {
        var view = views.get(resId)
        if (view == null) {
            view = parent.findViewById(resId)
            views.put(resId, view)
        }
        return view as T
    }

    fun setText(resId: Int, content: CharSequence?): ViewHolder4ListView {
        getView<TextView>(resId).text = content
        return this
    }

    fun setTextColor(resId: Int, colorResId: Int) {
        getView<TextView>(resId).setTextColor(parent.context.resources.getColor(colorResId))
    }

    fun setImageResource(resId: Int, imageResId: Int): ViewHolder4ListView {
        getView<ImageView>(resId).setImageResource(imageResId)
        return this
    }

    fun setVisible(resId: Int, visible: Boolean) {
        getView<View>(resId).visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setImageDrawable(resId: Int, drawable: Drawable?) {
        getView<ImageView>(resId).setImageDrawable(drawable)
    }
}