package com.guc.kframe.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView

/**
 * Created by guc on 2020/6/9.
 * 描述：解决ScrollView中嵌套gridview显示不正常的问题（1行半）
 */
class FixedGridView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    GridView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                Int.MAX_VALUE shr 2,
                MeasureSpec.AT_MOST
            )
        )
    }
}