package com.guc.kframe.adapter

import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by guc on 2020/4/29.
 * 描述：通用ListView的ViewHolder
 */
class ViewHolder4ListView(
    private val parent: View,
    private val views: SparseArray<View> = SparseArray()
) {

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