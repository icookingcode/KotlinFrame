package com.guc.kotlinframe

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.widget.pickerview.OptionsPickerView
import com.guc.kframe.widget.pickerview.TimePickerView
import com.guc.kframe.widget.pickerview.view.WheelTime
import kotlinx.android.synthetic.main.activity_third.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ThirdActivity : BaseActivity() {
    private var mOptions: List<String>? = null
    private var mOptions2Opt: List<List<String>>? =
        null
    private lateinit var mOptionPicker: OptionsPickerView<String>
    private lateinit var mOptionPickerDate: TimePickerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        mOptions = mutableListOf(
            "option1",
            "option2",
            "option3",
            "option4",
            "option5",
            "option6",
            "option7",
            "option8"
        )
        mOptions2Opt = ArrayList<List<String>>().apply {
            add(mOptions!!)
            add(mOptions!!)
            add(mOptions!!)
            add(ArrayList<String>())
            add(mOptions!!)
            add(mOptions!!)
            add(mOptions!!)
            add(ArrayList<String>())
        }
        btnSelect.setOnClickListener {
            mOptionPicker = OptionsPickerView<String>(this).apply {
                //参数设置
                titleBackgroundColor = Color.parseColor("#FFFFFF")
            }.create(object :
                OptionsPickerView.OnOptionsSelectListener {
                override fun onOptionsSelect(
                    options1: Int,
                    options2: Int,
                    options3: Int,
                    v: View?
                ) {
                    tvShow.text = mOptions!![options1]
                }
            }).apply {
                setPicker(mOptions!!, mOptions2Opt)
            }
            mOptionPicker.show()

        }

        btnSelectDate.setOnClickListener {
            mOptionPickerDate = TimePickerView(this).apply {
                //设置参数
                type = WheelTime.Type.YEAR_MONTH_DAY //选择日期类型
                titleText = "请选择日期"
                titleBackgroundColor = Color.parseColor("#FFFFFF")
                submitTextColor = Color.parseColor("#1E90FF")
                cancelTextColor = Color.parseColor("#B0C4DE")
                isDialogM = true
            }.create(object : TimePickerView.OnTimeSelectListener {
                @SuppressLint("SimpleDateFormat")
                override fun onTimeSelect(date: Date?, v: View?) {
                    date?.let {
                        tvShow.text = SimpleDateFormat("yyyy-MM-dd").format(it)
                    }
                }
            })
            mOptionPickerDate.show()
        }
    }
}