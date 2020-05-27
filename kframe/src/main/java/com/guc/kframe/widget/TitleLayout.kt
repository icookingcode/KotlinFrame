package com.guc.kframe.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.guc.kframe.R
import kotlinx.android.synthetic.main.layout_title.view.*

/**
 * Created by guc on 2020/4/28.
 * 描述：自定义标题栏
 */
class TitleLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
    View.OnClickListener {
    companion object {
        const val LEFT_TYPE_NONE = 0
        const val LEFT_TYPE_FINISH = 1
    }

    var onMoreClicked: ((View?) -> Unit)? = null

    var title: CharSequence = ""
        set(value) {
            titleText.text = value
            field = value
        }

    var leftType: Int = LEFT_TYPE_NONE;

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_title, this)
        val array = context.obtainStyledAttributes(attrs, R.styleable.TitleLayout)
        leftType = array.getInt(R.styleable.TitleLayout_leftType, LEFT_TYPE_NONE)
        title = array.getString(R.styleable.TitleLayout_title) ?: "标题"
        array.recycle()

        initView()

        titleMore.setOnClickListener(this)
    }

    private fun initView() {
        when (leftType) {
            LEFT_TYPE_NONE -> titleBack.visibility = View.GONE
            LEFT_TYPE_FINISH -> {
                titleBack.visibility = View.VISIBLE
                titleBack.setOnClickListener(this)
            }

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.titleBack -> (context as Activity).finish()
            R.id.titleMore -> onMoreClicked?.let { it(v) }
        }
    }
}