package com.guc.kframe.widget.dialog

import android.app.Dialog
import android.content.Context
import com.guc.kframe.R
import kotlinx.android.synthetic.main.view_loading_dialog.*

/**
 * Created by guc on 2020/5/27.
 * 描述：加载提示
 */
class LoadingDialog(
    context: Context,
    cancelable: Boolean = true,
    themeId: Int = R.style.LoadingDialog
) : Dialog(context, themeId) {
    var canCancel = cancelable
        set(value) {
            setCancelable(value)
            field = value
        }

    init {
        setContentView(R.layout.view_loading_dialog)
    }

    fun setTips(tips: String) {
        tvTips.text = tips
    }
}