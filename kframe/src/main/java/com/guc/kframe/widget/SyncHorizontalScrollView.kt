package com.guc.kframe.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import com.guc.kframe.R

/**
 * Created by guc on 2020/6/9.
 * 描述：实现两个HorizontalScrollView同步滚动
 */
class SyncHorizontalScrollView(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int,
    defRes: Int = R.style.NoEdgeScroll
) :
    HorizontalScrollView(context, attrs, defStyle, defRes) {
    var syncView: View? = null

    constructor(context: Context, attrs: AttributeSet) : this(
        context,
        attrs,
        0,
        R.style.NoEdgeScroll
    )

    constructor(context: Context) : this(context, null, 0, R.style.NoEdgeScroll)

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        syncView?.scrollTo(l, t)
    }
}