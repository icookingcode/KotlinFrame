package com.guc.kframe.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.guc.kframe.R
import kotlinx.android.synthetic.main.view_empty.view.*

/**
 * Created by guc on 2020/7/17.
 * Description：空提示视图
 */
class EmptyView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    FrameLayout(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var mHint1: CharSequence = ""
        set(value) {
            tvHint1.text = value
            field = value
        }
    private var mHint2: CharSequence = ""
        set(value) {
            tvHint2.text = value
            field = value
        }
    private var mHintIconId = 0
        set(value) {
            ivIcon.setImageResource(value)
            field = value
        }
    private var isMulLineHint = false
        set(value) {
            tvHint2.visibility = if (value) View.VISIBLE else View.GONE
            field = value
        }
    private var mTopMargin = 0
        set(value) {
            val params = ivIcon.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, value, 0, 0)
            field = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_empty, this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.EmptyView)
        mHintIconId = a.getResourceId(R.styleable.EmptyView_hintIcon, R.drawable.icon_empty)
        mHint1 = a.getText(R.styleable.EmptyView_hint1) ?: context.getString(R.string.str_nul)
        mHint2 = a.getText(R.styleable.EmptyView_hint2) ?: context.getString(R.string.str_nul)
        isMulLineHint = a.getBoolean(R.styleable.EmptyView_mulLineHint, false)
        mTopMargin = a.getDimensionPixelSize(R.styleable.EmptyView_topIconMargin, dp2px(160f))
        a.recycle()
    }

    private fun dp2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}