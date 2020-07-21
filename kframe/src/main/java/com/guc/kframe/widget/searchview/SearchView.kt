package com.guc.kframe.widget.searchview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.guc.kframe.R
import com.guc.kframe.utils.ToastUtil
import kotlinx.android.synthetic.main.view_search.view.*

/**
 * Created by guc on 2020/7/10.
 * Description：搜索View
 */
class SearchView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    companion object {
        const val DEFAULT_NULL_TIPS = "您尚未输入，请输入"
        const val DEFAULT_HINT_TIPS = "请输入"
    }

    var isCheckNull = true // 是否检测输入为空
    var onConfirmClicked: ((Boolean, String?) -> Unit)? = null
    var hintText: CharSequence
    var nullTip: CharSequence

    init {
        LayoutInflater.from(context).inflate(R.layout.view_search, this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.SearchView)
        hintText = a.getText(R.styleable.SearchView_hintText) ?: DEFAULT_HINT_TIPS
        nullTip = a.getText(R.styleable.SearchView_nullTip) ?: DEFAULT_NULL_TIPS
        isCheckNull = a.getBoolean(R.styleable.SearchView_isCheckNull, true)
        a.recycle()
        initView()
    }

    private fun initView() {
        etKeyWord.hint = hintText
        etKeyWord.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_ENTER || actionId == KeyEvent.KEYCODE_HOME) {
                search()
            }
            false
        }
        tvGo.setOnClickListener {
            hideSoftInput()
            search()
        }
    }

    private fun search() {
        val keyword = etKeyWord.text.toString().trim()
        val isEmpty = TextUtils.isEmpty(keyword)
        if (isCheckNull) {
            if (isEmpty) {
                ToastUtil.toast(nullTip.toString())
                return
            }
        }
        onConfirmClicked?.let {
            it(isEmpty, keyword)
        }


    }

    private fun hideSoftInput() {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //隐藏软键盘
        imm.hideSoftInputFromWindow(etKeyWord.windowToken, 0)
    }
}