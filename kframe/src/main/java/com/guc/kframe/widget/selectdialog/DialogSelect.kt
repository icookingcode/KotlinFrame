package com.guc.kframe.widget.selectdialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import com.guc.kframe.R

/**
 * Created by guc on 2020/5/27.
 * 描述：选择框
 */
class DialogSelect<T>(
    context: Context,
    cancelable: Boolean = false,
    cancelListener: DialogInterface.OnCancelListener? = null
) : Dialog(context, cancelable, cancelListener) {
    private var mDatas: List<T>? = null
    private var mDataSel: T? = null

    init {
        setContentView(R.layout.layout_dialog_select)
        val window = window
        window!!.setBackgroundDrawable(ColorDrawable()) //去除默认背景
        window!!.decorView.setPadding(dp2px(30), 0, dp2px(30), 0) //设置左右边距
        val params = window!!.attributes
        params.gravity = Gravity.CENTER
        window!!.attributes = params
    }

    private fun dp2px(dp: Int): Int {
        return (this.context.resources.displayMetrics.density * dp + 0.5).toInt()
    }
}