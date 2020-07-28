package com.guc.kframe.widget.pickerview

import android.content.Context
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
import com.guc.kframe.widget.pickerview.view.WheelTime
import java.text.ParseException
import java.util.*

/**
 * Created by guc on 2020/6/18.
 * Description：
 */
class TimePickerView(context: Context) : BasePickerView(context), View.OnClickListener {
    companion object {
        private const val TAG_SUBMIT = "submit"
        private const val TAG_CANCEL = "cancel"
    }

    var layoutRes = R.layout.pickerview_time
    var timeSelectListener: OnTimeSelectListener? = null
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
    var buttonTextSize = 17 //确定取消按钮大小
    var titleTextSize = 18 //标题文字大小
    var contentTextSize = 18 //内容文字大小
    var type: WheelTime.Type = WheelTime.Type.ALL //显示类型 默认全部显示

    var gravityContent = Gravity.CENTER //内容显示位置 默认居中
    var isCenterLabel = false

    var date: Calendar? = null//当前选中时间
    var startDate: Calendar? = null//开始时间
    var endDate: Calendar? = null//终止时间
    var startYear //开始年份
            = 0
    var endYear //结尾年份
            = 0
    private var cyclic //是否循环
            = false
    private var textColorOut //分割线以外的文字颜色
            = 0
    private var textColorCenter //分割线之间的文字颜色
            = 0
    private var dividerColor //分割线的颜色
            = 0

    // 条目间距倍数 默认1.6
    private var lineSpacingMultiplier = 1.6f
    var isDialogM: Boolean = false
        //是否是对话框模式
        set(value) {
            field = value
            if (value) {
                gravity = Gravity.CENTER
            }
        }
    private var labelYear: String? = null
    private var labelMonth: String? = null
    private var labelDay: String? = null
    private var labelHours: String? = null
    private var labelMinutes: String? = null
    private var labelSeconds: String? = null
    private var dividerType //分隔线类型
            : WheelView.DividerType = WheelView.DividerType.FILL
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button //确定、取消按钮

    private lateinit var tvTitle: TextView
    private lateinit var rv_top_bar: RelativeLayout
    private lateinit var wheelTime: WheelTime

    fun create(onTimeSelectListener: OnTimeSelectListener? = null): TimePickerView {
        this.timeSelectListener = onTimeSelectListener
        initViews()
        init()
        initEvents()
        if (customListener == null) {
            LayoutInflater.from(context).inflate(R.layout.pickerview_time, contentContainer)

            //顶部标题
            tvTitle = findViewById(R.id.tvTitle) as TextView

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

            //设置文字颜色
            btnSubmit.setTextColor(if (submitTextColor == 0) pickerview_timebtn_nor else submitTextColor)
            btnCancel.setTextColor(if (cancelTextColor == 0) pickerview_timebtn_nor else cancelTextColor)
            btnSubmit.setBackgroundColor(submitButtonBackgroundColor)
            btnCancel.setBackgroundColor(cancelButtonBackgroundColor)
            tvTitle.setTextColor(if (titleTextColor == 0) pickerview_topbar_title else titleTextColor)

            //设置文字大小
            btnSubmit.textSize = buttonTextSize.toFloat()
            btnCancel.textSize = buttonTextSize.toFloat()
            tvTitle.textSize = titleTextSize.toFloat()
            rv_top_bar = findViewById(R.id.rv_topbar) as RelativeLayout
            rv_top_bar.setBackgroundColor(if (titleBackgroundColor == 0) pickerview_bg_topbar else titleBackgroundColor)
        } else {
            customListener!!.customLayout(
                LayoutInflater.from(context).inflate(layoutRes, contentContainer)
            )
        }
        // 时间转轮 自定义控件
        // 时间转轮 自定义控件
        val timePickerView = findViewById(R.id.timepicker) as LinearLayout

        timePickerView.setBackgroundColor(if (wheelBackgroundColor == 0) bgColor_default else wheelBackgroundColor)

        wheelTime = WheelTime(timePickerView, type, gravityContent, contentTextSize)

        if (startYear != 0 && endYear != 0 && startYear <= endYear) {
            setRange()
        }

        if (startDate != null && endDate != null) {
            if (startDate!!.timeInMillis <= endDate!!.timeInMillis) {
                setRangDate()
            }
        } else if (startDate != null && endDate == null) {
            setRangDate()
        } else if (startDate == null && endDate != null) {
            setRangDate()
        }

        setTime()
        wheelTime.setLabels(
            labelYear,
            labelMonth,
            labelDay,
            labelHours,
            labelMinutes,
            labelSeconds
        )

        setOutSideCancelable(cancelable)
        wheelTime.setCyclic(cyclic)
        wheelTime.dividerColor = dividerColor
        wheelTime.dividerType = dividerType
        wheelTime.lineSpacingMultiplier = lineSpacingMultiplier
        wheelTime.textColorOut = textColorOut
        wheelTime.textColorCenter = textColorCenter
        wheelTime.isCenterLabel(isCenterLabel)
        return this
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

    override fun isDialog(): Boolean {
        return isDialogM
    }

    interface OnTimeSelectListener {
        fun onTimeSelect(date: Date?, v: View?)
    }

    /**
     * 设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private fun setRange() {
        wheelTime.startYear = startYear
        wheelTime.endYear = endYear
    }

    /**
     * 设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private fun setRangDate() {
        wheelTime.setRangDate(startDate, endDate)
        //如果设置了时间范围
        if (startDate != null && endDate != null) {
            //判断一下默认时间是否设置了，或者是否在起始终止时间范围内
            if (date == null || date!!.timeInMillis < startDate!!.timeInMillis || date!!.timeInMillis > endDate!!.timeInMillis
            ) {
                date = startDate
            }
        } else if (startDate != null) {
            //没有设置默认选中时间,那就拿开始时间当默认时间
            date = startDate
        } else if (endDate != null) {
            date = endDate
        }
    }

    /**
     * 设置选中时间,默认选中当前时间
     */
    private fun setTime() {
        val year: Int
        val month: Int
        val day: Int
        val hours: Int
        val minute: Int
        val seconds: Int
        val calendar = Calendar.getInstance()
        if (date == null) {
            calendar.timeInMillis = System.currentTimeMillis()
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
            hours = calendar[Calendar.HOUR_OF_DAY]
            minute = calendar[Calendar.MINUTE]
            seconds = calendar[Calendar.SECOND]
        } else {
            year = date!![Calendar.YEAR]
            month = date!![Calendar.MONTH]
            day = date!![Calendar.DAY_OF_MONTH]
            hours = date!![Calendar.HOUR_OF_DAY]
            minute = date!![Calendar.MINUTE]
            seconds = date!![Calendar.SECOND]
        }
        wheelTime.setPicker(year, month, day, hours, minute, seconds)
    }

    private fun returnData() {
        if (timeSelectListener != null) {
            try {
                val date: Date? = WheelTime.dateFormat.parse(wheelTime.getTime())
                timeSelectListener!!.onTimeSelect(date, clickView)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        dismiss()
    }

}