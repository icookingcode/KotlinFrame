package com.guc.kframe.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.guc.kframe.Engine

/**
 * Created by guc on 2020/4/28.
 * 描述：Toast 封装
 */
object ToastUtil {
    fun toast(
        txt: String,
        isShowAppName: Boolean = false,
        duration: Int = Toast.LENGTH_SHORT,
        context: Context = Engine.context
    ) {
        Toast.makeText(context, txt, duration).apply {
            if (!isShowAppName) {
                setText(txt)
            }
            show()
        }
    }

    fun toast(
        txtId: Int,
        isShowAppName: Boolean = false,
        duration: Int = Toast.LENGTH_SHORT,
        context: Context = Engine.context
    ) {
        toast(context.getString(txtId), isShowAppName, duration, context)
    }

    fun snack(
        view: View,
        txt: String,
        txtAction: String = "取消",
        time: Int = Snackbar.LENGTH_SHORT,
        back: (View) -> Unit
    ) {
        Snackbar.make(view, txt, time).setAction(txtAction) { back(it) }.show()
    }
}