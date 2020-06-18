package com.guc.kframe.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView

/**
 * Created by guc on 2020/6/15.
 * Description：宽度自适应ListView
 */
class AdaptiveWithListView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    ListView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(
                getMaxWidthOfChildren() + paddingLeft + paddingRight,
                MeasureSpec.EXACTLY
            ), heightMeasureSpec
        )
    }

    private fun getMaxWidthOfChildren(): Int {
        var maxWidth = 0
        var view: View? = null
        val count = adapter.count
        for (i in 0 until count) {
            view = adapter.getView(i, view, this)
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            if (view.measuredWidth > maxWidth) maxWidth = view.measuredWidth
        }
        return maxWidth
    }
}