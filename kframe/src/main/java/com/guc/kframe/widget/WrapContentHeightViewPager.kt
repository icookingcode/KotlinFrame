package com.guc.kframe.widget

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

/**
 * Created by guc on 2020/7/27.
 * Description：高度包裹的ViewPager
 */
class WrapContentHeightViewPager(context: Context, attrs: AttributeSet?) :
    ViewPager(context, attrs) {
    constructor(context: Context) : this(context, null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var height = 0
        //下面遍历所有child的高度
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(
                widthMeasureSpec,
                getChildMeasureSpec(
                    heightMeasureSpec,
                    0, child.layoutParams.height
                )
            );// getChildMeasureSpec获取到child具体的高度
            val h = child.measuredHeight
            //采用最大的view的高度。
            if (h > height) {
                height = h
            }
        }
        setMeasuredDimension(measuredWidth, height);
    }


}