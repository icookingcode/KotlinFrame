package com.guc.kframe.widget.pickerview.view

import android.graphics.Typeface
import android.view.View
import com.guc.kframe.R
import com.guc.kframe.widget.pickerview.adapter.ArrayWheelAdapter
import com.guc.kframe.widget.pickerview.lib.OnItemSelectedListener
import com.guc.kframe.widget.pickerview.lib.WheelView

/**
 * Created by guc on 2020/6/16.
 * Description：
 */
class WheelOptions<T>(view: View, val linkage: Boolean) {
    //文字的颜色和分割线的颜色
    var textColorOut = 0
    var textColorCenter = 0
    var dividerColor = 0

    // 条目间距倍数
    var lineSpacingMultiplier = 1.6f

    private var mOptions1Items: List<T>? = null
    private var mOptions2Items: List<List<T>>? = null
    private var mOptions3Items: List<List<List<T>>>? = null

    private var N_mOptions2Items: List<T>? = null
    private var N_mOptions3Items: List<T>? = null

    private val wv_option1: WheelView = view.findViewById(R.id.options1)
    private val wv_option2: WheelView = view.findViewById(R.id.options2)
    private val wv_option3: WheelView = view.findViewById(R.id.options3)

    var wheelListener_option1: OnItemSelectedListener = null
    var wheelListener_option2: OnItemSelectedListener = null
    var dividerType: WheelView.DividerType = WheelView.DividerType.WRAP

    fun setPicker(
        options1Items: List<T>,
        options2Items: List<List<T>>?,
        options3Items: List<List<List<T>>>?
    ) {
        mOptions1Items = options1Items
        mOptions2Items = options2Items
        mOptions3Items = options3Items
        var len: Int = ArrayWheelAdapter.DEFAULT_LENGTH
        if (mOptions3Items == null) len = 8
        if (mOptions2Items == null) len = 12
        //选项1
        wv_option1.adapter = ArrayWheelAdapter(mOptions1Items!!)
        wv_option1.selectedItem = 0
        //选项2
        if (mOptions2Items != null) {
            wv_option2.adapter = ArrayWheelAdapter(mOptions2Items!![0])
            wv_option2.selectedItem = wv_option1.selectedItem
        }
        //选项3
        if (mOptions3Items != null) {
            wv_option3.adapter = ArrayWheelAdapter(mOptions3Items!![0][0])
            wv_option3.selectedItem = wv_option3.selectedItem
        }
        wv_option1.isOptions = true
        wv_option2.isOptions = true
        wv_option3.isOptions = true

        if (mOptions2Items == null) wv_option2.visibility = View.GONE
        if (mOptions3Items == null) wv_option3.visibility = View.GONE
        //联动监听
        wheelListener_option1 = { index ->
            var opt2Select = 0
            if (mOptions2Items != null) {
                opt2Select = wv_option2.selectedItem
                //新opt2的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                opt2Select =
                    if (opt2Select >= mOptions2Items!![index].size - 1) mOptions2Items!![index].size - 1 else opt2Select
                wv_option2.adapter = ArrayWheelAdapter(mOptions2Items!![index])
                wv_option2.selectedItem = opt2Select
            }
            if (mOptions3Items != null) {
                wheelListener_option2!!(opt2Select)
            }
        }
        wheelListener_option2 = { i ->
            if (mOptions3Items != null) {
                var opt1Select = wv_option1.selectedItem
                opt1Select =
                    if (opt1Select >= mOptions3Items!!.size - 1) mOptions3Items!!.size - 1 else opt1Select
                var index =
                    if (i >= mOptions2Items!![opt1Select].size - 1) mOptions2Items!![opt1Select].size - 1 else i
                var opt3 = wv_option3.selectedItem//上一个opt3的选中位置
                //新opt3的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                opt3 = if (opt3 >= mOptions3Items!![opt1Select][index].size - 1
                ) mOptions3Items!![opt1Select][index].size - 1 else opt3
                wv_option3.adapter =
                    ArrayWheelAdapter(
                        mOptions3Items!![wv_option1.selectedItem][index]
                    )
                wv_option3.selectedItem = opt3
            }
        }
        // 添加联动监听

        // 添加联动监听
        if (options2Items != null && linkage) wv_option1.onItemSelectedListener =
            wheelListener_option1
        if (options3Items != null && linkage) wv_option2.onItemSelectedListener =
            wheelListener_option2

    }

    fun setNPicker(options1Items: List<T>, options2Items: List<T>?, options3Items: List<T>?) {
        mOptions1Items = options1Items
        N_mOptions2Items = options2Items
        N_mOptions3Items = options3Items
        var len = ArrayWheelAdapter.DEFAULT_LENGTH
        if (N_mOptions3Items == null) len = 8
        if (N_mOptions2Items == null) len = 12
        // 选项1
        wv_option1.adapter = ArrayWheelAdapter(mOptions1Items!!) // 设置显示数据
        wv_option1.selectedItem = 0 // 初始化时显示的数据
        // 选项2
        if (N_mOptions2Items != null) {
            wv_option2.adapter = ArrayWheelAdapter(N_mOptions2Items!!) // 设置显示数据
            wv_option2.selectedItem = (wv_option1.selectedItem) // 初始化时显示的数据
        }
        // 选项3
        if (N_mOptions3Items != null) {
            wv_option3.adapter = ArrayWheelAdapter(N_mOptions3Items!!) // 设置显示数据
            wv_option3.selectedItem = wv_option3.selectedItem
        }
        wv_option1.isOptions = true
        wv_option2.isOptions = true
        wv_option3.isOptions = true
        if (N_mOptions2Items == null) wv_option2.visibility = View.GONE
        if (N_mOptions3Items == null) wv_option3.visibility = View.GONE
    }

    /**
     * set text size
     */
    fun setTextContentSize(textSize: Int) {
        wv_option1.textSize = textSize.toFloat()
        wv_option2.textSize = textSize.toFloat()
        wv_option3.textSize = textSize.toFloat()
    }

    /**
     * 设置选项的单位
     *
     * @param label1 单位
     * @param label2 单位
     * @param label3 单位
     */
    fun setLabels(
        label1: String?,
        label2: String?,
        label3: String?
    ) {
        if (label1 != null) wv_option1.label = label1
        if (label2 != null) wv_option2.label = label2
        if (label3 != null) wv_option3.label = label3
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    fun setCyclic(cyclic: Boolean) {
        wv_option1.isLoop = cyclic
        wv_option2.isLoop = cyclic
        wv_option3.isLoop = cyclic
    }

    /**
     * 分别设置第一二三级是否循环滚动
     *
     * @param cyclic1,cyclic2,cyclic3 是否循环
     */
    fun setCyclic(
        cyclic1: Boolean,
        cyclic2: Boolean,
        cyclic3: Boolean
    ) {
        wv_option1.isLoop = cyclic1
        wv_option2.isLoop = cyclic2
        wv_option3.isLoop = cyclic3
    }

    /**
     * 设置字体样式
     *
     * @param font 系统提供的几种样式
     */
    fun setTypeface(font: Typeface?) {
        wv_option1.typeface = font
        wv_option2.typeface = font
        wv_option3.typeface = font
    }

    /**
     * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2。
     * 在快速滑动未停止时，点击确定按钮，会进行判断，如果匹配数据越界，则设为0，防止index出错导致崩溃。
     *
     * @return 索引数组
     */
    fun getCurrentItems(): IntArray {
        val currentItems = IntArray(3)
        currentItems[0] = wv_option1.selectedItem
        if (mOptions2Items != null && mOptions2Items!!.size > 0) { //非空判断
            currentItems[1] =
                if (wv_option2.selectedItem > mOptions2Items!![currentItems[0]].size - 1) 0 else wv_option2.selectedItem
        } else {
            currentItems[1] = wv_option2.selectedItem
        }
        if (mOptions3Items != null && mOptions3Items!!.size > 0) { //非空判断
            currentItems[2] =
                if (wv_option3.selectedItem > mOptions3Items!![currentItems[0]][currentItems[1]].size - 1
                ) 0 else wv_option3.selectedItem
        } else {
            currentItems[2] = wv_option3.selectedItem
        }
        return currentItems
    }

    fun setCurrentItems(option1: Int, option2: Int, option3: Int) {
        if (linkage) {
            itemSelected(option1, option2, option3)
        }
        wv_option1.selectedItem = option1
        wv_option2.selectedItem = option2
        wv_option3.selectedItem = option3
    }

    /**
     * 设置间距倍数,但是只能在1.2-2.0f之间
     *
     * @param lineSpacingMultiplier
     */
    fun setMLineSpacingMultiplier(lineSpacingMultiplier: Float) {
        this.lineSpacingMultiplier = lineSpacingMultiplier
        setLineSpacingMultiplier()
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */
    fun setMDividerColor(dividerColor: Int) {
        this.dividerColor = dividerColor
        setDividerColor()
    }

    /**
     * 设置分割线的类型
     *
     * @param dividerType
     */
    fun setMDividerType(dividerType: WheelView.DividerType) {
        this.dividerType = dividerType
        setDividerType()
    }

    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter
     */
    fun setMTextColorCenter(textColorCenter: Int) {
        this.textColorCenter = textColorCenter
        setTextColorCenter()
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    fun setMTextColorOut(textColorOut: Int) {
        this.textColorOut = textColorOut
        setTextColorOut()
    }

    /**
     * Label 是否只显示中间选中项的
     *
     * @param isCenterLabel
     */
    fun isCenterLabel(isCenterLabel: Boolean) {
        wv_option1.isCenterLabel = isCenterLabel
        wv_option2.isCenterLabel = isCenterLabel
        wv_option3.isCenterLabel = isCenterLabel
    }

    private fun setTextColorOut() {
        wv_option1.textColorOut = textColorOut
        wv_option2.textColorOut = textColorOut
        wv_option3.textColorOut = textColorOut
    }

    private fun setTextColorCenter() {
        wv_option1.textColorCenter = textColorCenter
        wv_option2.textColorCenter = textColorCenter
        wv_option3.textColorCenter = textColorCenter
    }

    private fun setDividerType() {
        wv_option1.dividerType = dividerType
        wv_option2.dividerType = dividerType
        wv_option3.dividerType = dividerType
    }

    private fun setDividerColor() {
        wv_option1.dividerColor = dividerColor
        wv_option2.dividerColor = dividerColor
        wv_option3.dividerColor = dividerColor
    }

    private fun setLineSpacingMultiplier() {
        wv_option1.lineSpacingMultiplier = lineSpacingMultiplier
        wv_option2.lineSpacingMultiplier = lineSpacingMultiplier
        wv_option3.lineSpacingMultiplier = lineSpacingMultiplier
    }

    private fun itemSelected(opt1Select: Int, opt2Select: Int, opt3Select: Int) {
        if (mOptions2Items != null) {
            wv_option2.adapter =
                ArrayWheelAdapter(
                    mOptions2Items!![opt1Select]
                )
            wv_option2.selectedItem = opt2Select
        }
        if (mOptions3Items != null) {
            wv_option3.adapter =
                ArrayWheelAdapter(
                    mOptions3Items!![opt1Select][opt2Select]
                )
            wv_option3.selectedItem = opt3Select
        }
    }
}