package com.guc.kframe.widget.textview

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.guc.kframe.R

/**
 * Created by guc on 2020/6/8.
 * 描述：说明 和 内容同行 但样式不一样
 */
class RichTextView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr) {
    private var contentDefault = ""
    private var mTitle: CharSequence? = null
    private var mContent: CharSequence? = null
        set(value) {
            field = value ?: "暂无"
        }
    private var mTitleTextColor = 0
    private var mContentTextColor = 0

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RichTextView)
        mTitle = a.getText(R.styleable.RichTextView_title)
        mContent = a.getText(R.styleable.RichTextView_content)
        val default = a.getText(R.styleable.RichTextView_contentDefault)
        contentDefault = default?.toString() ?: ""
        mTitleTextColor = a.getColor(
            R.styleable.RichTextView_titleTextColor,
            Color.parseColor("#666666")
        )
        mContentTextColor = a.getColor(
            R.styleable.RichTextView_contentTextColor,
            Color.parseColor("#333333")
        )
        a.recycle()
        setContent(mContent.toString())
    }

    fun setContent(content: String?) {
        mContent = content
        text = if (mTitle == null || "" == mTitle) {
            mTitle = ""
            content
        } else {
            val str = mTitle.toString() + mContent
            val spannableString = SpannableString(str)
            spannableString.setSpan(
                ForegroundColorSpan(mTitleTextColor),
                0,
                mTitle!!.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(mContentTextColor),
                mTitle!!.length,
                str.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString
        }
    }
}
