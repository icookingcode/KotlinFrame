package com.guc.kframe.widget.pickerview.view

import android.annotation.SuppressLint
import android.view.View
import com.guc.kframe.R
import com.guc.kframe.widget.pickerview.adapter.NumericWheelAdapter
import com.guc.kframe.widget.pickerview.lib.OnItemSelectedListener
import com.guc.kframe.widget.pickerview.lib.WheelView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by guc on 2020/6/17.
 * Description：
 */
class WheelTime(
    val view: View,
    val type: Type = Type.ALL,
    val gravity: Int,
    val textSize: Int = 18
) {
    companion object {
        private const val DEFAULT_START_YEAR = 1900
        private const val DEFAULT_END_YEAR = 2100
        private const val DEFAULT_START_MONTH = 1
        private const val DEFAULT_END_MONTH = 12
        private const val DEFAULT_START_DAY = 1
        private const val DEFAULT_END_DAY = 31

        @SuppressLint("SimpleDateFormat")
        var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }

    //文字的颜色和分割线的颜色
    var textColorOut = 0
        set(value) {
            field = value
            setTextColorOut()
        }
    var textColorCenter = 0
        set(value) {
            field = value
            setTextColorCenter()
        }
    var dividerColor = 0
        set(value) {
            field = value
            setDividerColor()
        }

    // 条目间距倍数
    var lineSpacingMultiplier = 1.6f
        set(value) {
            field = value
            setLineSpacingMultiplier()
        }
    private lateinit var wv_year: WheelView
    private lateinit var wv_month: WheelView
    private lateinit var wv_day: WheelView
    private lateinit var wv_hours: WheelView
    private lateinit var wv_mins: WheelView
    private lateinit var wv_seconds: WheelView
    var startYear = DEFAULT_START_YEAR
    var endYear = DEFAULT_END_YEAR
    private var startMonth = DEFAULT_START_MONTH
    private var endMonth = DEFAULT_END_MONTH
    private var startDay = DEFAULT_START_DAY
    private var endDay = DEFAULT_END_DAY //表示31天的

    private var currentYear = 0

    // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
    var dividerType: WheelView.DividerType = WheelView.DividerType.FILL
        set(value) {
            if (value !== null) {
                field = value
                setDividerType()
            }
        }

    fun setPicker(year: Int, month: Int, day: Int) {
        this.setPicker(year, month, day, 0, 0, 0)
    }

    fun setPicker(year: Int, month: Int, day: Int, h: Int, m: Int, s: Int) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        val months_big =
            arrayOf("1", "3", "5", "7", "8", "10", "12")
        val months_little = arrayOf("4", "6", "9", "11")
        val list_big =
            Arrays.asList(*months_big)
        val list_little =
            Arrays.asList(*months_little)

        /*  final Context context = view.getContext();*/
        currentYear = year
        // 年
        wv_year = view.findViewById<View>(R.id.year) as WheelView
        wv_year.adapter = NumericWheelAdapter(startYear, endYear) // 设置"年"的显示数据
        /*wv_year.setLabel(context.getString(R.string.pickerview_year));// 添加文字*/
        wv_year.selectedItem = (year - startYear) // 初始化时显示的数据
        wv_year.mGravity = gravity
        // 月
        wv_month = view.findViewById<View>(R.id.month) as WheelView
        if (startYear == endYear) { //开始年等于终止年
            wv_month.adapter = NumericWheelAdapter(startMonth, endMonth)
            wv_month.selectedItem = (month + 1 - startMonth)
        } else if (year == startYear) {
            //起始日期的月份控制
            wv_month.adapter = (NumericWheelAdapter(startMonth, 12))
            wv_month.selectedItem = (month + 1 - startMonth)
        } else if (year == endYear) {
            //终止日期的月份控制
            wv_month.adapter = NumericWheelAdapter(1, endMonth)
            wv_month.selectedItem = month
        } else {
            wv_month.adapter = NumericWheelAdapter(1, 12)
            wv_month.selectedItem = month
        }
        /*   wv_month.setLabel(context.getString(R.string.pickerview_month));*/
        wv_month.mGravity =
            gravity
        // 日
        wv_day = view.findViewById<View>(R.id.day) as WheelView
        if (startYear == endYear && startMonth == endMonth) {
            if (list_big.contains((month + 1).toString())) {
                if (endDay > 31) {
                    endDay = 31
                }
                wv_day.adapter = NumericWheelAdapter(startDay, endDay)
            } else if (list_little.contains((month + 1).toString())) {
                if (endDay > 30) {
                    endDay = 30
                }
                wv_day.adapter = NumericWheelAdapter(startDay, endDay)
            } else {
                // 闰年
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29
                    }
                    wv_day.adapter = NumericWheelAdapter(startDay, endDay)
                } else {
                    if (endDay > 28) {
                        endDay = 28
                    }
                    wv_day.adapter = NumericWheelAdapter(startDay, endDay)
                }
            }
            wv_day.selectedItem = day - startDay
        } else if (year == startYear && month + 1 == startMonth) {
            // 起始日期的天数控制
            if (list_big.contains((month + 1).toString())) {
                wv_day.adapter = NumericWheelAdapter(startDay, 31)
            } else if (list_little.contains((month + 1).toString())) {
                wv_day.adapter = NumericWheelAdapter(startDay, 30)
            } else {
                // 闰年
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    wv_day.adapter = NumericWheelAdapter(startDay, 29)
                } else {
                    wv_day.adapter = NumericWheelAdapter(startDay, 28)
                }
            }
            wv_day.selectedItem = day - startDay
        } else if (year == endYear && month + 1 == endMonth) {
            // 终止日期的天数控制
            if (list_big.contains((month + 1).toString())) {
                if (endDay > 31) {
                    endDay = 31
                }
                wv_day.adapter = NumericWheelAdapter(1, endDay)
            } else if (list_little.contains((month + 1).toString())) {
                if (endDay > 30) {
                    endDay = 30
                }
                wv_day.adapter = NumericWheelAdapter(1, endDay)
            } else {
                // 闰年
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29
                    }
                    wv_day.adapter = NumericWheelAdapter(1, endDay)
                } else {
                    if (endDay > 28) {
                        endDay = 28
                    }
                    wv_day.adapter = NumericWheelAdapter(1, endDay)
                }
            }
            wv_day.selectedItem = (day - 1)
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains((month + 1).toString())) {
                wv_day.adapter = NumericWheelAdapter(1, 31)
            } else if (list_little.contains((month + 1).toString())) {
                wv_day.adapter = NumericWheelAdapter(1, 30)
            } else {
                // 闰年
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    wv_day.adapter = (NumericWheelAdapter(1, 29))
                } else {
                    wv_day.adapter = NumericWheelAdapter(1, 28)
                }
            }
            wv_day.selectedItem = day - 1
        }

        /* wv_day.setLabel(context.getString(R.string.pickerview_day));*/
        wv_day.mGravity = gravity
        //时
        wv_hours = view.findViewById<View>(R.id.hour) as WheelView
        wv_hours.adapter = NumericWheelAdapter(0, 23)
        /*  wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字*/
        wv_hours.selectedItem = h
        wv_hours.mGravity = gravity
        //分
        wv_mins = view.findViewById<View>(R.id.min) as WheelView
        wv_mins.adapter = NumericWheelAdapter(0, 59)
        /* wv_mins.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字*/
        wv_mins.selectedItem = m
        wv_mins.mGravity = gravity
        //秒
        wv_seconds = view.findViewById<View>(R.id.second) as WheelView
        wv_seconds.adapter = NumericWheelAdapter(0, 59)
        /* wv_seconds.setLabel(context.getString(R.string.pickerview_seconds));// 添加文字*/
        wv_seconds.selectedItem = s
        wv_seconds.mGravity = gravity

        // 添加"年"监听
        val wheelListener_year: OnItemSelectedListener = { index ->
            val year_num = index + startYear
            currentYear = year_num
            var currentMonthItem: Int = wv_month.selectedItem //记录上一次的item位置
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (startYear == endYear) {
                //重新设置月份
                wv_month.adapter = NumericWheelAdapter(startMonth, endMonth)
                if (currentMonthItem > wv_month.adapter!!.getItemsCount() - 1) {
                    currentMonthItem = wv_month.adapter!!.getItemsCount() - 1
                    wv_month.selectedItem = currentMonthItem
                }
                val monthNum = currentMonthItem + startMonth
                if (startMonth == endMonth) {
                    //重新设置日
                    setReDay(year_num, monthNum, startDay, endDay, list_big, list_little)
                } else if (monthNum == startMonth) {
                    //重新设置日
                    setReDay(year_num, monthNum, startDay, 31, list_big, list_little)
                } else {
                    //重新设置日
                    setReDay(year_num, monthNum, 1, 31, list_big, list_little)
                }
            } else if (year_num == startYear) { //等于开始的年
                //重新设置月份
                wv_month.adapter = NumericWheelAdapter(startMonth, 12)
                if (currentMonthItem > wv_month.getItemCount() - 1) {
                    currentMonthItem = wv_month.getItemCount() - 1
                    wv_month.selectedItem = currentMonthItem
                }
                val month = currentMonthItem + startMonth
                if (month == startMonth) {

                    //重新设置日
                    setReDay(year_num, month, startDay, 31, list_big, list_little)
                } else {
                    //重新设置日
                    setReDay(year_num, month, 1, 31, list_big, list_little)
                }
            } else if (year_num == endYear) {
                //重新设置月份
                wv_month.adapter = NumericWheelAdapter(1, endMonth)
                if (currentMonthItem > wv_month.getItemCount() - 1) {
                    currentMonthItem = wv_month.getItemCount() - 1
                    wv_month.selectedItem = currentMonthItem
                }
                val monthNum = currentMonthItem + 1
                if (monthNum == endMonth) {
                    //重新设置日
                    setReDay(year_num, monthNum, 1, endDay, list_big, list_little)
                } else {
                    //重新设置日
                    setReDay(year_num, monthNum, 1, 31, list_big, list_little)
                }
            } else {
                //重新设置月份
                wv_month.adapter = NumericWheelAdapter(1, 12)
                //重新设置日
                setReDay(year_num, wv_month.selectedItem + 1, 1, 31, list_big, list_little)
            }
        }
        // 添加"月"监听
        val wheelListener_month: OnItemSelectedListener = { index: Int ->
            var month_num = index + 1
            if (startYear == endYear) {
                month_num = month_num + startMonth - 1
                if (startMonth == endMonth) {
                    //重新设置日
                    setReDay(currentYear, month_num, startDay, endDay, list_big, list_little)
                } else if (startMonth == month_num) {

                    //重新设置日
                    setReDay(currentYear, month_num, startDay, 31, list_big, list_little)
                } else if (endMonth == month_num) {
                    setReDay(currentYear, month_num, 1, endDay, list_big, list_little)
                } else {
                    setReDay(currentYear, month_num, 1, 31, list_big, list_little)
                }
            } else if (currentYear == startYear) {
                month_num = month_num + startMonth - 1
                if (month_num == startMonth) {
                    //重新设置日
                    setReDay(currentYear, month_num, startDay, 31, list_big, list_little)
                } else {
                    //重新设置日
                    setReDay(currentYear, month_num, 1, 31, list_big, list_little)
                }
            } else if (currentYear == endYear) {
                if (month_num == endMonth) {
                    //重新设置日
                    setReDay(
                        currentYear,
                        wv_month.selectedItem + 1,
                        1,
                        endDay,
                        list_big,
                        list_little
                    )
                } else {
                    setReDay(
                        currentYear,
                        wv_month.selectedItem + 1,
                        1,
                        31,
                        list_big,
                        list_little
                    )
                }
            } else {
                //重新设置日
                setReDay(currentYear, month_num, 1, 31, list_big, list_little)
            }
        }
        wv_year.onItemSelectedListener = wheelListener_year
        wv_month.onItemSelectedListener = wheelListener_month
        when (type) {
            Type.ALL -> {
            }
            Type.YEAR_MONTH_DAY -> {
                /* textSize = textSize * 4;*/wv_hours.visibility = View.GONE
                wv_mins.visibility = View.GONE
                wv_seconds.visibility = View.GONE
            }
            Type.HOURS_MINS -> {
                /*textSize = textSize * 4;*/wv_year.visibility = View.GONE
                wv_month.visibility = View.GONE
                wv_day.visibility = View.GONE
                wv_seconds.visibility = View.GONE
            }
            Type.MONTH_DAY_HOUR_MIN -> {
                /* textSize = textSize * 3;*/wv_year.visibility = View.GONE
                wv_seconds.visibility = View.GONE
            }
            Type.YEAR_MONTH -> {
                /* textSize = textSize * 4;*/wv_day.visibility = View.GONE
                wv_hours.visibility = View.GONE
                wv_mins.visibility = View.GONE
                wv_seconds.visibility = View.GONE
                /* textSize = textSize * 4;*/wv_seconds.visibility = View.GONE
            }
            Type.YEAR_MONTH_DAY_HOUR_MIN -> wv_seconds.visibility = View.GONE
        }
        setContentTextSize()
    }

    /**
     * Label 是否只显示中间选中项的
     *
     * @param isCenterLabel
     */
    fun isCenterLabel(isCenterLabel: Boolean) {
        wv_day.isCenterLabel = isCenterLabel
        wv_month.isCenterLabel = isCenterLabel
        wv_year.isCenterLabel = isCenterLabel
        wv_hours.isCenterLabel = isCenterLabel
        wv_mins.isCenterLabel = isCenterLabel
        wv_seconds.isCenterLabel = isCenterLabel
    }

    private fun setReDay(
        year_num: Int,
        monthNum: Int,
        startD: Int,
        endD: Int,
        list_big: List<String>,
        list_little: List<String>
    ) {
        var endD = endD
        var currentItem: Int = wv_day.selectedItem
        if (list_big
                .contains(monthNum.toString())
        ) {
            if (endD > 31) {
                endD = 31
            }
            wv_day.adapter = (NumericWheelAdapter(startD, endD))
        } else if (list_little.contains(monthNum.toString())) {
            if (endD > 30) {
                endD = 30
            }
            wv_day.adapter = NumericWheelAdapter(startD, endD)
        } else {
            if (year_num % 4 == 0 && year_num % 100 != 0
                || year_num % 400 == 0
            ) {
                if (endD > 29) {
                    endD = 29
                }
                wv_day.adapter = NumericWheelAdapter(startD, endD)
            } else {
                if (endD > 28) {
                    endD = 28
                }
                wv_day.adapter = NumericWheelAdapter(startD, endD)
            }
        }
        if (currentItem > wv_day.adapter!!.getItemsCount() - 1) {
            currentItem = wv_day.adapter!!.getItemsCount() - 1
            wv_day.selectedItem = currentItem
        }
    }

    private fun setContentTextSize() {
        wv_day.textSize = textSize.toFloat()
        wv_month.textSize = textSize.toFloat()
        wv_year.textSize = textSize.toFloat()
        wv_hours.textSize = textSize.toFloat()
        wv_mins.textSize = textSize.toFloat()
        wv_seconds.textSize = textSize.toFloat()
    }

    private fun setTextColorOut() {
        wv_day.textColorOut = textColorOut
        wv_month.textColorOut = textColorOut
        wv_year.textColorOut = textColorOut
        wv_hours.textColorOut = textColorOut
        wv_mins.textColorOut = textColorOut
        wv_seconds.textColorOut = textColorOut
    }

    private fun setTextColorCenter() {
        wv_day.textColorCenter = textColorCenter
        wv_month.textColorCenter = textColorCenter
        wv_year.textColorCenter = textColorCenter
        wv_hours.textColorCenter = textColorCenter
        wv_mins.textColorCenter = textColorCenter
        wv_seconds.textColorCenter = textColorCenter
    }

    private fun setDividerColor() {
        wv_day.dividerColor = dividerColor
        wv_month.dividerColor = dividerColor
        wv_year.dividerColor = dividerColor
        wv_hours.dividerColor = dividerColor
        wv_mins.dividerColor = dividerColor
        wv_seconds.dividerColor = dividerColor
    }

    private fun setDividerType() {
        wv_day.dividerType = dividerType
        wv_month.dividerType = dividerType
        wv_year.dividerType = dividerType
        wv_hours.dividerType = dividerType
        wv_mins.dividerType = dividerType
        wv_seconds.dividerType = dividerType
    }

    private fun setLineSpacingMultiplier() {
        wv_day.lineSpacingMultiplier = lineSpacingMultiplier
        wv_month.lineSpacingMultiplier = lineSpacingMultiplier
        wv_year.lineSpacingMultiplier = lineSpacingMultiplier
        wv_hours.lineSpacingMultiplier = lineSpacingMultiplier
        wv_mins.lineSpacingMultiplier = lineSpacingMultiplier
        wv_seconds.lineSpacingMultiplier = lineSpacingMultiplier
    }

    fun setLabels(
        label_year: String?,
        label_month: String?,
        label_day: String?,
        label_hours: String?,
        label_mins: String?,
        label_seconds: String?
    ) {
        if (label_year != null) {
            wv_year.label = label_year
        } else {
            wv_year.label = view.context.getString(R.string.pickerview_year)
        }
        if (label_month != null) {
            wv_month.label = label_month
        } else {
            wv_month.label = view.context.getString(R.string.pickerview_month)
        }
        if (label_day != null) {
            wv_day.label = label_day
        } else {
            wv_day.label = view.context.getString(R.string.pickerview_day)
        }
        if (label_hours != null) {
            wv_hours.label = label_hours
        } else {
            wv_hours.label = view.context.getString(R.string.pickerview_hours)
        }
        if (label_mins != null) {
            wv_mins.label = label_mins
        } else {
            wv_mins.label = view.context.getString(R.string.pickerview_minutes)
        }
        if (label_seconds != null) {
            wv_seconds.label = label_seconds
        } else {
            wv_seconds.label = view.context.getString(R.string.pickerview_seconds)
        }
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    fun setCyclic(cyclic: Boolean) {
        wv_year.isLoop = cyclic
        wv_month.isLoop = cyclic
        wv_day.isLoop = cyclic
        wv_hours.isLoop = cyclic
        wv_mins.isLoop = cyclic
        wv_seconds.isLoop = cyclic
    }

    /**
     * 获取当前时间
     */
    fun getTime(): String {
        val sb = StringBuffer()
        if (currentYear == startYear) {
            if (wv_month.selectedItem + startMonth === startMonth) {
                sb.append(wv_year.selectedItem + startYear).append("-")
                    .append(wv_month.selectedItem + startMonth).append("-")
                    .append(wv_day.selectedItem + startDay).append(" ")
                    .append(wv_hours.selectedItem).append(":")
                    .append(wv_mins.selectedItem).append(":")
                    .append(wv_seconds.selectedItem)
            } else {
                sb.append(wv_year.selectedItem + startYear).append("-")
                    .append(wv_month.selectedItem + startMonth).append("-")
                    .append(wv_day.selectedItem + 1).append(" ")
                    .append(wv_hours.selectedItem).append(":")
                    .append(wv_mins.selectedItem).append(":")
                    .append(wv_seconds.selectedItem)
            }
        } else {
            sb.append(wv_year.selectedItem + startYear).append("-")
                .append(wv_month.selectedItem + 1).append("-")
                .append(wv_day.selectedItem + 1).append(" ")
                .append(wv_hours.selectedItem).append(":")
                .append(wv_mins.selectedItem).append(":")
                .append(wv_seconds.selectedItem)
        }
        return sb.toString()
    }

    /**
     * 通过Calendar设置日期段
     */
    fun setRangDate(
        startDate: Calendar?,
        endDate: Calendar?
    ) {
        if (startDate == null && endDate != null) {
            val year = endDate[Calendar.YEAR]
            val month = endDate[Calendar.MONTH] + 1
            val day = endDate[Calendar.DAY_OF_MONTH]
            if (year > startYear) {
                endYear = year
                endMonth = month
                endDay = day
            } else if (year == startYear) {
                if (month > startMonth) {
                    endYear = year
                    endMonth = month
                    endDay = day
                } else if (month == startMonth) {
                    if (month > startDay) {
                        endYear = year
                        endMonth = month
                        endDay = day
                    }
                }
            }
        } else if (startDate != null && endDate == null) {
            val year = startDate[Calendar.YEAR]
            val month = startDate[Calendar.MONTH] + 1
            val day = startDate[Calendar.DAY_OF_MONTH]
            if (year < endYear) {
                startMonth = month
                startDay = day
                startYear = year
            } else if (year == endYear) {
                if (month < endMonth) {
                    startMonth = month
                    startDay = day
                    startYear = year
                } else if (month == endMonth) {
                    if (day < endDay) {
                        startMonth = month
                        startDay = day
                        startYear = year
                    }
                }
            }
        } else if (startDate != null && endDate != null) {
            startYear = startDate[Calendar.YEAR]
            endYear = endDate[Calendar.YEAR]
            startMonth = startDate[Calendar.MONTH] + 1
            endMonth = endDate[Calendar.MONTH] + 1
            startDay = startDate[Calendar.DAY_OF_MONTH]
            endDay = endDate[Calendar.DAY_OF_MONTH]
        }
    }

    enum class Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH, YEAR_MONTH_DAY_HOUR_MIN
    } // 六种选择模式，年月日时分秒，年月日，时分，月日时分，年月，年月日时分

}