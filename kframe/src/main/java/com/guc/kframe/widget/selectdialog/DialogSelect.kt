package com.guc.kframe.widget.selectdialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import com.guc.kframe.R
import com.guc.kframe.utils.ToastUtil
import kotlinx.android.synthetic.main.layout_dialog_select.*

/**
 * Created by guc on 2020/5/27.
 * 描述：选择框
 */
class DialogSelect<T>(
    context: Context,
    private var isSingle: Boolean = true,
    cancelable: Boolean = true,
    cancelListener: DialogInterface.OnCancelListener? = null,
    val callback: ((Boolean, List<T>?) -> Unit)?
) : Dialog(context, cancelable, cancelListener) {
    var datas: List<T>? = null
        set(value) {
            if (value != null) {
                mAdapter = SelectAdapter(value, isSingle)
                loadSelectors()
            }
            field = value
        }
    private var mDataSel: T? = null
    private var mAdapter: SelectAdapter<T>? = null

    init {
        setContentView(R.layout.layout_dialog_select)
        val window = window
        window!!.setBackgroundDrawable(ColorDrawable()) //去除默认背景
        window.decorView.setPadding(dp2px(30), 0, dp2px(30), 0) //设置左右边距
        val params = window.attributes
        params.gravity = Gravity.CENTER
        window.attributes = params
        setCancelable(cancelable)
        initView()
    }

    private fun initView() {
        tvSure.setOnClickListener {

            callback?.let { cb ->
                mAdapter?.let {
                    val list = it.getSelected()
                    val isSel = list.isNotEmpty()
                    cb(isSel, list)
                    if (isSel) dismiss() else ToastUtil.toast("您尚未选择")
                } ?: run {
                    cb(false, null)
                }
            }
        }
    }

    private fun loadSelectors() {
        rlvContent.layoutManager = LinearLayoutManager(context)
        rlvContent.adapter = mAdapter
    }

    private fun dp2px(dp: Int): Int {
        return (this.context.resources.displayMetrics.density * dp + 0.5).toInt()
    }
}