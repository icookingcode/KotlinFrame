package com.guc.kframe.widget.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import com.guc.kframe.R
import kotlinx.android.synthetic.main.layout_dialog_confirm.*

/**
 * Created by guc on 2020/7/14.
 * Description：自定义确认对话框
 */
class DialogConfirm(
    context: Context,
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = false,
    themeResId: Int = R.style.MyCustomDialog
) :
    Dialog(context, themeResId), View.OnClickListener {
    constructor(context: Context) : this(context, false)

    var onClickedListener: OnClickedListener? = null

    init {
        setContentView(R.layout.layout_dialog_confirm)
        setCancelable(cancelable)
        setCanceledOnTouchOutside(canceledOnTouchOutside)
        tvCancel.setOnClickListener(this)
        tvSure.setOnClickListener(this)
    }

    /**
     * 设置提示信息
     *
     * @param msg 提示语
     */
    fun setTipMsg(msg: String) {
        tvContent.text = (if (TextUtils.isEmpty(msg)) "no message!" else msg)
    }

    /**
     * 设置是否有图标
     *
     * @param visible true:有 false:没有
     * @param resId    icon 资源id
     * @return this
     */
    fun setIconAndVisible(
        visible: Boolean,
        resId: Int
    ) {
        if (visible) {
            ivIcon.visibility = View.VISIBLE
            ivIcon.setBackgroundResource(resId)
        } else {
            ivIcon.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvCancel -> onClickedListener?.onCancelClicked()
            R.id.tvSure -> onClickedListener?.onConfirmClicked()
        }
        if (onClickedListener == null) dismiss()
    }

    interface OnClickedListener {
        fun onCancelClicked()
        fun onConfirmClicked()
    }
}