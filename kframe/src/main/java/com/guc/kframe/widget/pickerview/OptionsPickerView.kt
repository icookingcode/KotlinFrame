package com.guc.kframe.widget.pickerview

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.guc.kframe.R
import com.guc.kframe.widget.pickerview.lib.CustomListener
import com.guc.kframe.widget.pickerview.lib.WheelView
import com.guc.kframe.widget.pickerview.view.BasePickerView
import com.guc.kframe.widget.pickerview.view.WheelOptions

/**
 * Created by guc on 2020/6/17.
 * Description：
 */
class OptionsPickerView<T>(context: Context) : BasePickerView(context), View.OnClickListener {
    companion object {
        private const val TAG_SUBMIT = "submit"
        private const val TAG_CANCEL = "cancel"
    }

    var layoutRes = R.layout.pickerview_options
    var optionsSelectListener: OnOptionsSelectListener? = null
    var customListener: CustomListener? = null
    var submitText: String? = null //确定按钮文字
    var cancelText: String? = null //取消按钮文字
    var titleText: String = "" //标题文字
    var submitTextColor //确定按钮颜色
            = 0
    var cancelTextColor //取消按钮颜色
            = 0
    var submitButtonBackgroundColor = 0x1E90FF
    var cancelButtonBackgroundColor = 0xD3D3D3
    var titleTextColor //标题颜色
            = 0
    var wheelBackgroundColor //滚轮背景颜色
            = 0
    var titleBackgroundColor //标题背景颜色
            = 0
    var buttonTextColor = 17 //确定取消按钮大小
    var titleTextSize = 18 //标题文字大小
    var contentTextSize = 18 //内容文字大小
    var linkage = true //是否联动
    var isCenterLabel = true //是否只显示中间的label
    var textColorOut //分割线以外的文字颜色
            = 0
    var textColorCenter //分割线之间的文字颜色
            = 0
    var dividerColor //分割线的颜色
            = 0

    // 条目间距倍数 默认1.6
    var lineSpacingMultiplier = 1.6f
    var isDialogM: Boolean = false
        //是否是对话框模式 Boolean = false
        set(value) {
            field = value
            if (value) {
                gravity = Gravity.CENTER
            }
        }
    var label1: String? = null
    var label2: String? = null
    var label3: String? = null
    var cyclic1 = false //是否循环，默认否
    var cyclic2 = false
    var cyclic3 = false
    var font: Typeface? = null
    var option1 //默认选中项
            = 0
    var option2 = 0
    var option3 = 0
    var dividerType //分隔线类型
            : WheelView.DividerType = WheelView.DividerType.FILL

    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button //确定、取消按钮

    private lateinit var tvTitle: TextView
    private lateinit var rv_top_bar: RelativeLayout
    private lateinit var wheelOptions: WheelOptions<T>

    fun create(optionsSelectListener: OnOptionsSelectListener? = null): OptionsPickerView<T> {
        this.optionsSelectListener = optionsSelectListener
        cancelable = (cancelable)
        initViews()
        init()
        initEvents()
        if (customListener == null) {
            LayoutInflater.from(context).inflate(R.layout.pickerview_options, contentContainer)

            //顶部标题
            tvTitle = findViewById(R.id.tvTitle) as TextView
            rv_top_bar = findViewById(R.id.rv_topbar) as RelativeLayout

            //确定和取消按钮
            btnSubmit = findViewById(R.id.btnSubmit) as Button
            btnCancel = findViewById(R.id.btnCancel) as Button
            btnSubmit.tag = TAG_SUBMIT
            btnCancel.tag = TAG_CANCEL
            btnSubmit.setOnClickListener(this)
            btnCancel.setOnClickListener(this)

            //设置文字
            btnSubmit.text =
                if (TextUtils.isEmpty(submitText)) context.resources
                    .getString(R.string.pickerview_submit) else submitText
            btnCancel.text =
                if (TextUtils.isEmpty(cancelText)) context.resources
                    .getString(R.string.pickerview_cancel) else cancelText
            tvTitle.text = if (TextUtils.isEmpty(titleText)) "" else titleText //默认为空

            //设置color
            btnSubmit.setTextColor(if (submitTextColor == 0) pickerview_timebtn_nor else submitTextColor)
            btnCancel.setTextColor(if (cancelTextColor == 0) pickerview_timebtn_nor else cancelTextColor)
            btnSubmit.setBackgroundColor(submitButtonBackgroundColor)
            btnCancel.setBackgroundColor(cancelButtonBackgroundColor)
            tvTitle.setTextColor(if (titleTextColor == 0) pickerview_topbar_title else titleTextColor)
            rv_top_bar.setBackgroundColor(if (titleBackgroundColor == 0) pickerview_bg_topbar else titleBackgroundColor)

            //设置文字大小
            btnSubmit.textSize = buttonTextColor.toFloat()
            btnCancel.textSize = buttonTextColor.toFloat()
            tvTitle.textSize = titleTextSize.toFloat()
            tvTitle.text = titleText
        } else {
            customListener!!.customLayout(
                LayoutInflater.from(context).inflate(layoutRes, contentContainer)
            )
        }


        // ----滚轮布局
        val optionsPicker = findViewById(R.id.optionspicker) as LinearLayout
        optionsPicker.setBackgroundColor(if (wheelBackgroundColor == 0) bgColor_default else wheelBackgroundColor)

        wheelOptions = WheelOptions(optionsPicker, linkage)
        wheelOptions.setTextContentSize(contentTextSize)
        wheelOptions.setLabels(label1, label2, label3)
        wheelOptions.setCyclic(cyclic1, cyclic2, cyclic3)
        wheelOptions.setTypeface(font)

        setOutSideCancelable(cancelable)
        tvTitle.text = titleText
        wheelOptions.dividerColor = dividerColor
        wheelOptions.dividerType = dividerType
        wheelOptions.lineSpacingMultiplier = lineSpacingMultiplier
        wheelOptions.textColorOut = textColorOut
        wheelOptions.textColorCenter = textColorCenter
        wheelOptions.isCenterLabel(isCenterLabel)
        return this
    }

    fun setPicker(
        options1Items: List<T>,
        options2Items: List<List<T>>? = null,
        options3Items: List<List<List<T>>>? = null
    ) {
        if (::wheelOptions.isInitialized) {
            wheelOptions.setPicker(options1Items, options2Items, options3Items)
            setCurrentItems()
        }
    }

    //不联动情况下调用
    fun setNPicker(
        options1Items: List<T>,
        options2Items: List<T>? = null,
        options3Items: List<T>? = null
    ) {
        if (::wheelOptions.isInitialized) {
            wheelOptions.setNPicker(options1Items, options2Items, options3Items)
            setCurrentItems()
        }
    }

    override fun onClick(v: View?) {
        when (v?.tag) {
            TAG_SUBMIT -> {
                returnData()
            }
            TAG_CANCEL -> {
                dismiss()
            }
        }
    }

    override fun isDialog() = isDialogM
    private fun returnData() {
        if (optionsSelectListener != null) {
            val optionsCurrentItems = wheelOptions.getCurrentItems()
            optionsSelectListener!!.onOptionsSelect(
                optionsCurrentItems[0],
                optionsCurrentItems[1],
                optionsCurrentItems[2],
                clickView
            )
        }
        dismiss()
    }

    private fun setCurrentItems() {
        if (wheelOptions != null) {
            wheelOptions.setCurrentItems(option1, option2, option3)
        }
    }

    interface OnOptionsSelectListener {
        fun onOptionsSelect(
            options1: Int,
            options2: Int,
            options3: Int,
            v: View?
        )
    }


}