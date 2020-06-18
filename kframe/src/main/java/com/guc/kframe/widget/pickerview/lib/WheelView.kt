package com.guc.kframe.widget.pickerview.lib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import com.guc.kframe.R
import com.guc.kframe.widget.pickerview.adapter.IWheelAdapter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.math.*

typealias OnItemSelectedListener = ((index: Int) -> Unit)?

/**
 * Created by guc on 2020/6/15.
 * Description：wheel view
 */
class WheelView(context: Context, var attributeSet: AttributeSet?) : View(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    companion object {
        const val VELOCITY_FLING = 5 // scroll velocity
        const val SCALE_CONTENT = 0.8f //scale size of the value not in center
    }

    var handler = MessageHandler(this)
    var onItemSelectedListener: OnItemSelectedListener = null

    //Timer
    private val mExecutor = Executors.newSingleThreadScheduledExecutor()
    lateinit var paintOuterText: Paint
    lateinit var paintCenterText: Paint
    lateinit var paintIndicator: Paint
    var adapter: IWheelAdapter<*>? = null
        set(value) {
            field = value
            remeasure()
            invalidate()
        }

    var textSize: Float = 0f
        set(value) {
            if (value > 0) {
                field = context.resources.displayMetrics.density * value
                if (::paintOuterText.isInitialized) paintOuterText.textSize = field
                if (::paintCenterText.isInitialized) paintCenterText.textSize = field
            }
        }
    private var maxTextWidth: Int = 0
    private var maxTextHeight: Int = 0
    var itemHeight = 0f; // item height
    var typeface = Typeface.MONOSPACE // style of font
        set(value) {
            field = value
            if (::paintOuterText.isInitialized) paintOuterText.typeface = value
            if (::paintCenterText.isInitialized) paintCenterText.typeface = value
        }
    var textColorOut = -0x575758
        set(value) {
            if (value != 0) {
                field = value
                if (::paintOuterText.isInitialized) paintOuterText.color = value
            }
        }
    var textColorCenter = -0xd5d5d6
        set(value) {
            if (value != 0) {
                field = value
                if (::paintCenterText.isInitialized) paintCenterText.color = value
            }
        }
    var dividerColor = -0x2a2a2b
        set(value) {
            if (value != 0) {
                field = value
                if (::paintIndicator.isInitialized) paintIndicator.color = value
            }
        }
    var lineSpacingMultiplier = 1.6f // line spacing multiplier
        set(value) {
            field = when {
                value < 1.2 -> 1.2f
                value > 2.0 -> 2.0f
                else -> value
            }
        }
    var isLoop = false
    private var firstLineY: Float = 0f  // the y coordinates of the first line
    private var secondLineY: Float = 0f   // the y coordinates of the second line
    private var centerY: Float = 0f  // // the y coordinates of the center line
    var totalScrollY: Float = 0f  // // the total scroll height
    var initPosition: Int = -1  // // the position of the init selection
    private var preCurrentIndex: Int =
        0  // // the scroll offset ,for recording the numbers of the item scrolled
    private var change: Int = 0
    private var itemsVisible: Int = 11
    private var mHeight: Int = 0 // the height of this view
    private var mWidth: Int = 0 // the width of this view
    private var halfCircumference: Int = 0 // the half circumference
    private var radius: Int = 0 // the radius
    private var startTime: Long = 0
    private var widthMeasureSpec: Int = 0
    var dividerType: DividerType? = null
    lateinit var gestureDetector: GestureDetector
    var isOptions: Boolean = false
    var isCenterLabel: Boolean = false
    private var mFuture: ScheduledFuture<*>? = null
    var label: String = ""
    var selectedItem: Int = 0
        set(value) {
            initPosition = value
            totalScrollY = 0f
            invalidate()
            field = value
        }
    private var mOffset = 0
    private var previousY = 0f
    var mGravity: Int = Gravity.CENTER
    var drawCenterContentStart: Int = 0 //the start position of the center item selected
    var drawOutCenterContentStart: Int = 0 //the start position of the outer items not selected
    var centerContentOffset: Float = 0f //the offset of the content

    init {
        textSize = resources.getDimensionPixelSize(R.dimen.text_m).toFloat()

        val den = resources.displayMetrics.density
        centerContentOffset = if (den < 1) {// adapter screen density 0.75/1.0/1.5/2.0/3.0
            2.4f
        } else if (den >= 1 && den < 1.5) {
            3.6f
        } else if (den >= 1.5 && den < 2) {
            4.5f
        } else if (den >= 2 && den < 3) {
            6f
        } else {
            den * 2.5f
        }
        initAttrs()
        initLoopView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        this.widthMeasureSpec = widthMeasureSpec
        remeasure()
        setMeasuredDimension(mWidth, mHeight)
    }


    /**
     * init attrs params
     */
    private fun initAttrs() {
        val a = attributeSet?.run {
            val array = context.obtainStyledAttributes(attributeSet, R.styleable.WheelView)
            array
        }
        a?.apply {
            mGravity = getInt(R.styleable.WheelView_pickerview_gravity, Gravity.CENTER)
            textColorOut = a.getColor(R.styleable.WheelView_pickerview_textColorOut, textColorOut)
            textColorCenter =
                a.getColor(R.styleable.WheelView_pickerview_textColorCenter, textColorCenter)
            dividerColor = a.getColor(R.styleable.WheelView_pickerview_dividerColor, dividerColor)
            textSize =
                a.getDimensionPixelOffset(
                    R.styleable.WheelView_pickerview_textSize,
                    textSize.toInt()
                ).toFloat()
            lineSpacingMultiplier = a.getFloat(
                R.styleable.WheelView_pickerview_lineSpacingMultiplier,
                lineSpacingMultiplier
            )
        }?.recycle()
    }

    /**
     * init loop view params
     */
    private fun initLoopView() {
        handler = MessageHandler(this)
        gestureDetector = GestureDetector(context, LoopViewGestureListener(this))
        gestureDetector.setIsLongpressEnabled(false)
        isLoop = true  // loop
        initPosition = -1
        initPaints()
    }

    /**
     * init paints
     */
    private fun initPaints() {
        paintOuterText = Paint().apply {
            color = textColorOut
            isAntiAlias = true
            typeface = this@WheelView.typeface
            textSize = this@WheelView.textSize
        }
        paintCenterText = Paint().apply {
            color = textColorCenter
            isAntiAlias = true
            typeface = this@WheelView.typeface
            textSize = this@WheelView.textSize
            textScaleX = 1.1f
        }
        paintIndicator = Paint().apply {
            color = dividerColor
            isAntiAlias = true
        }
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    /**
     * remeasure view
     */
    private fun remeasure() {
        if (adapter == null) return
        measureTextWidthHeight()
        halfCircumference = ((itemsVisible - 1) * itemHeight).toInt() //
        mHeight = ((halfCircumference * 2) / PI).toInt()
        radius = (halfCircumference / PI).toInt()
        mWidth = MeasureSpec.getSize(widthMeasureSpec)//the width of the view
        //calculate the position of the dividers
        firstLineY = (mHeight - itemHeight) / 2
        secondLineY = (mHeight + itemHeight) / 2
        centerY = secondLineY - (itemHeight - maxTextHeight) / 2 - centerContentOffset
        //get init position
        if (initPosition == -1) {
            initPosition = if (isLoop) (adapter.let { it?.getItemsCount() ?: 0 } + 1) / 2 else 0
        }
        preCurrentIndex = initPosition
    }

    /**
     * measure text width and height
     */
    private fun measureTextWidthHeight() {
        if (adapter == null) return
        val rect = Rect()
        for (index in 0 until adapter.let { it?.getItemsCount() ?: 0 }) {
            val s1 = getContentText(getIItem(index))
            paintCenterText.getTextBounds(s1, 0, s1.length, rect)
            maxTextWidth = if (rect.width() > maxTextWidth) rect.width() else maxTextWidth
            paintCenterText.getTextBounds("\u661F\u671F", 0, 2, rect) //the code of  星期
            maxTextHeight = rect.height() + 2
        }
        itemHeight = lineSpacingMultiplier * maxTextHeight

    }

    /**
     * get the text to show
     */
    private fun getContentText(item: Any?): String {
        return when (item) {
            null -> ""
            is IPickerViewData -> item.getPickerViewText()
            is Int -> String.format(Locale.getDefault(), "%02d", item)
            else -> item.toString()
        }
    }

    /**
     * smooth scroll
     */
    fun smoothScroll(action: ACTION) {//smooth scroll
        cancelFuture()
        if (action == ACTION.FLING || action == ACTION.DRAG) {
            mOffset = ((totalScrollY % itemHeight + itemHeight) % itemHeight).toInt()
            mOffset = if (mOffset > itemHeight / 2) {
                (itemHeight - mOffset).toInt()
            } else {
                -mOffset
            }
        }
        // move the center text to center
        mFuture = mExecutor.scheduleWithFixedDelay(
            SmoothScrollTimerTask(this, mOffset),
            0,
            10,
            TimeUnit.MILLISECONDS
        )
    }

    /**
     * inertia scroll
     */
    fun scrollBy(velocityY: Float) {
        cancelFuture()
        mFuture = mExecutor.scheduleWithFixedDelay(
            InertiaTimerTask(this, velocityY), 0,
            VELOCITY_FLING.toLong(), TimeUnit.MILLISECONDS
        )

    }

    fun onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(OnItemSelectedRunnable(this), 200L)
        }
    }

    fun cancelFuture() {
        mFuture = mFuture?.run {
            if (!isCancelled) cancel(true)
            null
        }
    }

    fun getItemCount() = adapter.let { it?.getItemsCount() ?: 0 }


    override fun onDraw(canvas: Canvas) {
        if (adapter == null) return
        val visibles = arrayOfNulls<Any>(itemsVisible)//the count of visible items
        change = (totalScrollY / itemHeight).toInt()

        //滚动中实际的预选中的item(即经过了中间位置的item) ＝ 滑动前的位置 ＋ 滑动相对位置
        try {
            preCurrentIndex = initPosition + change % adapter.let { it?.getItemsCount() ?: 0 }
        } catch (e: ArithmeticException) {
            Log.e("WheelView", "出错了！adapter.getItemsCount() == 0，联动数据不匹配")
        }
        if (!isLoop) { //not loop
            if (preCurrentIndex < 0) preCurrentIndex = 0
            if (preCurrentIndex > adapter.let { it?.getItemsCount() ?: 0 } - 1) preCurrentIndex =
                adapter.let { it?.getItemsCount() ?: 0 } - 1
        } else {//loop
            //循环
            if (preCurrentIndex < 0)  //举个例子：如果总数是5，preCurrentIndex ＝ －1，那么preCurrentIndex按循环来说，其实是0的上面，也就是4的位置
                preCurrentIndex += getICount()
            if (preCurrentIndex > getICount() - 1)  //同理上面,自己脑补一下
                preCurrentIndex -= preCurrentIndex
        }
        //跟滚动流畅度有关，总滑动距离与每个item高度取余，即并不是一格格的滚动，每个item不一定滚到对应Rect里的，这个item对应格子的偏移值
        val itemHeightOffset = totalScrollY % itemHeight
        //set array
        var counter = 0
        while (counter < itemsVisible) {
            var index =
                preCurrentIndex - (itemsVisible / 2 - counter) //索引值，即当前在控件中间的item看作数据源的中间，计算出相对源数据源的index值

            //判断是否循环，如果是循环数据源也使用相对循环的position获取对应的item值，如果不是循环则超出数据源范围使用""空白字符串填充，在界面上形成空白无数据的item项
            when {
                isLoop -> {
                    index = getLoopMappingIndex(index)
                    visibles[counter] = getIItem(index)
                }
                index < 0 -> {
                    visibles[counter] = ""
                }
                index > getICount() - 1 -> {
                    visibles[counter] = ""
                }
                else -> {
                    visibles[counter] = getIItem(index)
                }
            }
            counter++
        }
        drawTwoLines(canvas)
        drawCenterText(canvas)

        counter = 0
        while (counter < itemsVisible) {
            canvas.save()
            // 弧长 L = itemHeight * counter - itemHeightOffset
            // 求弧度 α = L / r  (弧长/半径) [0,π]
            val radian =
                (itemHeight * counter - itemHeightOffset) / radius.toDouble()
            // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
            // angle [-90°,90°]
            val angle =
                (90.0 - radian / Math.PI * 180.0).toFloat() //item第一项,从90度开始，逐渐递减到 -90度

            // 计算取值可能有细微偏差，保证负90°到90°以外的不绘制
            if (angle >= 90f || angle <= -90f) {
                canvas.restore()
            } else {
                //获取内容文字
                //如果是label每项都显示的模式，并且item内容不为空、label 也不为空
                val contentText =
                    if (!isCenterLabel && !TextUtils.isEmpty(label) && !TextUtils.isEmpty(
                            getContentText(
                                visibles[counter]
                            )
                        )
                    ) {
                        getContentText(visibles[counter]) + label
                    } else {
                        getContentText(visibles[counter])
                    }
                reMeasureTextSize(contentText)
                //计算开始绘制的位置
                measuredCenterContentStart(contentText)
                measuredOutContentStart(contentText)
                val translateY =
                    (radius - cos(radian) * radius - sin(radian) * maxTextHeight / 2.0).toFloat()
                //根据Math.sin(radian)来更改canvas坐标系原点，然后缩放画布，使得文字高度进行缩放，形成弧形3d视觉差
                canvas.translate(0.0f, translateY)
                canvas.scale(1.0f, sin(radian).toFloat())
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // 条目经过第一条线
                    canvas.save()
                    canvas.clipRect(0f, 0f, measuredWidth.toFloat(), firstLineY - translateY)
                    canvas.scale(
                        1.0f,
                        sin(radian).toFloat() * SCALE_CONTENT
                    )
                    canvas.drawText(
                        contentText,
                        drawOutCenterContentStart.toFloat(),
                        maxTextHeight.toFloat(),
                        paintOuterText
                    )
                    canvas.restore()
                    canvas.save()
                    canvas.clipRect(
                        0f,
                        firstLineY - translateY,
                        measuredWidth.toFloat(),
                        itemHeight
                    )
                    canvas.scale(1.0f, sin(radian).toFloat() * 1.0f)
                    canvas.drawText(
                        contentText,
                        drawCenterContentStart.toFloat(),
                        maxTextHeight - centerContentOffset,
                        paintCenterText
                    )
                    canvas.restore()
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // 条目经过第二条线
                    canvas.save()
                    canvas.clipRect(0f, 0f, measuredWidth.toFloat(), secondLineY - translateY)
                    canvas.scale(1.0f, sin(radian).toFloat() * 1.0f)
                    canvas.drawText(
                        contentText,
                        drawCenterContentStart.toFloat(),
                        maxTextHeight - centerContentOffset,
                        paintCenterText
                    )
                    canvas.restore()
                    canvas.save()
                    canvas.clipRect(
                        0f,
                        secondLineY - translateY,
                        measuredWidth.toFloat(),
                        itemHeight
                    )
                    canvas.scale(
                        1.0f,
                        sin(radian).toFloat() * SCALE_CONTENT
                    )
                    canvas.drawText(
                        contentText,
                        drawOutCenterContentStart.toFloat(),
                        maxTextHeight.toFloat(),
                        paintOuterText
                    )
                    canvas.restore()
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // 中间条目
                    //canvas.clipRect(0, 0, measuredWidth,   maxTextHeight);
                    //让文字居中
                    val y: Float =
                        maxTextHeight - centerContentOffset //因为圆弧角换算的向下取值，导致角度稍微有点偏差，加上画笔的基线会偏上，因此需要偏移量修正一下
                    canvas.drawText(
                        contentText,
                        drawCenterContentStart.toFloat(),
                        y,
                        paintCenterText
                    )
                    val preSelectedItem = indexOf(visibles[counter])
                    selectedItem = preSelectedItem
                } else {
                    // 其他条目
                    canvas.save()
                    canvas.clipRect(0, 0, measuredWidth, itemHeight.toInt())
                    canvas.scale(
                        1.0f,
                        sin(radian).toFloat() * SCALE_CONTENT
                    )
                    canvas.drawText(
                        contentText,
                        drawOutCenterContentStart.toFloat(),
                        maxTextHeight.toFloat(),
                        paintOuterText
                    )
                    canvas.restore()
                }
                canvas.restore()
                paintCenterText.textSize = textSize
            }
            counter++
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventConsumed = gestureDetector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTime = System.currentTimeMillis()
                cancelFuture()
                previousY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = previousY - event.rawY
                previousY = event.rawY
                totalScrollY += dy

                // 边界处理。
                if (!isLoop) {
                    var top = -initPosition * itemHeight
                    var bottom =
                        (adapter!!.getItemsCount() - 1 - initPosition) * itemHeight
                    if (totalScrollY - itemHeight * 0.25 < top) {
                        top = totalScrollY - dy
                    } else if (totalScrollY + itemHeight * 0.25 > bottom) {
                        bottom = totalScrollY - dy
                    }
                    if (totalScrollY < top) {
                        totalScrollY = top
                    } else if (totalScrollY > bottom) {
                        totalScrollY = bottom
                    }
                }
            }

            else -> if (!eventConsumed) {
                val y = event.y
                val l =
                    acos((radius - y) / radius.toDouble()) * radius
                val circlePosition = ((l + itemHeight / 2) / itemHeight).toInt()
                val extraOffset =
                    (totalScrollY % itemHeight + itemHeight) % itemHeight
                mOffset = ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset).toInt()
                if (System.currentTimeMillis() - startTime > 120) {
                    smoothScroll(ACTION.DRAG)
                } else {
                    smoothScroll(ACTION.CLICK)
                }
            }
        }
        invalidate()
        return true
    }

    /**
     * 绘制两条线
     */
    private fun drawTwoLines(canvas: Canvas) {

        //绘制中间两条横线
        if (dividerType == DividerType.WRAP) { //横线长度仅包裹内容
            var startX: Float
            val endX: Float
            startX = if (TextUtils.isEmpty(label)) { //隐藏Label的情况
                (measuredWidth - maxTextWidth) / 2 - 12.toFloat()
            } else {
                (measuredWidth - maxTextWidth) / 4 - 12.toFloat()
            }
            if (startX <= 0) { //如果超过了WheelView的边缘
                startX = 10f
            }
            endX = measuredWidth - startX
            canvas.drawLine(startX, firstLineY, endX, firstLineY, paintIndicator)
            canvas.drawLine(startX, secondLineY, endX, secondLineY, paintIndicator)
        } else {
            canvas.drawLine(0.0f, firstLineY, measuredWidth.toFloat(), firstLineY, paintIndicator)
            canvas.drawLine(0.0f, secondLineY, measuredWidth.toFloat(), secondLineY, paintIndicator)
        }
    }

    private fun drawCenterText(canvas: Canvas) {

        //只显示选中项Label文字的模式，并且Label文字不为空，则进行绘制
        if (!TextUtils.isEmpty(label) && isCenterLabel) {
            //绘制文字，靠右并留出空隙
            val drawRightContentStart: Int = measuredWidth - getTextWidth(paintCenterText, label)
            canvas.drawText(
                label,
                drawRightContentStart - centerContentOffset,
                centerY,
                paintCenterText
            )
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun measuredCenterContentStart(content: String) {
        val rect = Rect()
        paintCenterText.getTextBounds(content, 0, content.length, rect)
        when (mGravity) {
            Gravity.CENTER -> drawCenterContentStart =
                if (isOptions || label == "" || !isCenterLabel) {
                    ((measuredWidth - rect.width()) * 0.5).toInt()
                } else { //只显示中间label时，时间选择器内容偏左一点，留出空间绘制单位标签
                    ((measuredWidth - rect.width()) * 0.25).toInt()
                }
            Gravity.LEFT, Gravity.START -> drawCenterContentStart = 0
            Gravity.RIGHT, Gravity.END -> drawCenterContentStart =
                measuredWidth - rect.width() - centerContentOffset.toInt()
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun measuredOutContentStart(content: String) {
        val rect = Rect()
        paintOuterText.getTextBounds(content, 0, content.length, rect)
        when (mGravity) {
            Gravity.CENTER -> drawOutCenterContentStart =
                if (isOptions || label == "" || !isCenterLabel) {
                    ((measuredWidth - rect.width()) * 0.5).toInt()
                } else { //只显示中间label时，时间选择器内容偏左一点，留出空间绘制单位标签
                    ((measuredWidth - rect.width()) * 0.25).toInt()
                }
            Gravity.LEFT, Gravity.START -> drawOutCenterContentStart = 0
            Gravity.RIGHT, Gravity.END -> drawOutCenterContentStart =
                measuredWidth - rect.width() - centerContentOffset.toInt()
        }
    }

    /**
     * 计算文字宽度
     */
    private fun getTextWidth(paint: Paint, str: String?): Int {
        var iRet = 0
        if (str != null && str.isNotEmpty()) {
            val len = str.length
            val widths = FloatArray(len)
            paint.getTextWidths(str, widths)
            for (j in 0 until len) {
                iRet += ceil(widths[j].toDouble()).toInt()
            }
        }
        return iRet
    }

    /**
     *递归计算出对应的index
     */
    private fun getLoopMappingIndex(index: Int): Int {
        var i = index
        if (index < 0) {
            i += adapter!!.getItemsCount()
            i = getLoopMappingIndex(i)
        } else if (index > adapter!!.getItemsCount() - 1) {
            i -= adapter!!.getItemsCount()
            i = getLoopMappingIndex(i)
        }
        return i
    }

    /**
     * 根据文字的长度 重新设置文字的大小 让其能完全显示
     *
     * @param contentText
     */
    private fun reMeasureTextSize(contentText: String) {
        val rect = Rect()
        paintCenterText.getTextBounds(contentText, 0, contentText.length, rect)
        var width = rect.width()
        var size = textSize.toInt()
        while (width > measuredWidth) {
            size--
            //设置2条横线中间的文字大小
            paintCenterText.textSize = size.toFloat()
            paintCenterText.getTextBounds(contentText, 0, contentText.length, rect)
            width = rect.width()
        }
        //设置2条横线外面的文字大小
        paintOuterText.textSize = size.toFloat()
    }

    private fun getICount(): Int = adapter.let { it?.getItemsCount() ?: 0 }
    private fun getIItem(index: Int) = adapter.let { it?.getItem(index) }
    private fun indexOf(o: Any?) = adapter.let { it?.indexOf(o) ?: -1 }
    enum class ACTION {
        //点击，滑翔(滑到尽头)，拖拽事件
        CLICK, FLING, DRAG
    }

    enum class DividerType {
        //分割线类型
        FILL, WRAP
    }
}