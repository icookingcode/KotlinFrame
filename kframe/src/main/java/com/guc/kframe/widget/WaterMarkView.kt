package com.guc.kframe.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.guc.kframe.R
import kotlin.math.*

/**
 * Created by guc on 2020/6/12.
 * Description：水印
 */
class WaterMarkView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    companion object {
        const val TAG = "WaterMarkView"
        const val DEFAULT_RADIAN: Float = (PI / 6).toFloat()
    }

    var markText: CharSequence = "水印"
    private val path = Path()
    private var lineHeight = 0  //px
    private var markerTextSize = 0f
    private var singleMarkerWidth = 0
    private var singleMarkerHeight = 0
    private var markerSpace = 0//间距
    private var deltaFixSpace = 0//修正间距
    var radian = DEFAULT_RADIAN//弧度
    private var repeatCountX = 1
    private var repeatCountY = 1
    private var repeatSpace = 1

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.DKGRAY
        style = Paint.Style.FILL
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.WaterMarkView)
        markText = a.getString(R.styleable.WaterMarkView_markerText) ?: "水印"
        lineHeight = a.getDimensionPixelOffset(
            R.styleable.WaterMarkView_lineHeight,
            dp2px(50f)
        )
        markerTextSize = a.getDimension(R.styleable.WaterMarkView_markerTextSize, 48f)
        markerSpace =
            a.getDimension(R.styleable.WaterMarkView_markerSpace, dp2px(30f).toFloat()).toInt()
        deltaFixSpace =
            a.getDimension(R.styleable.WaterMarkView_fixSpace, markerSpace.toFloat() / 2).toInt()
        radian = a.getFloat(R.styleable.WaterMarkView_radian, radian)
        a.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rectText = Rect()
        paint.textSize = markerTextSize
        paint.getTextBounds(markText.toString(), 0, markText.toString().length, rectText)
        singleMarkerWidth = rectText.width()
        singleMarkerHeight = rectText.height()
        val space = "a"
        paint.getTextBounds(space, 0, space.length, rectText)
        repeatSpace = markerSpace / rectText.width()
        repeatCountX = ceil(width * 1.0 / cos(radian) / (singleMarkerWidth + markerSpace)).toInt()
        repeatCountY = floor(height * 1.0 / lineHeight).toInt()
        val itemStdHeight = getItemHeight()
        var start = 1
        var end = repeatCountY
        if (radian>0) end+= width/lineHeight else start-= width/lineHeight
        for (i in start..end) {
            path.reset()
            path.moveTo(0f + deltaFixSpace, i * lineHeight.toFloat())
            val x = getEndX(itemStdHeight, i) + deltaFixSpace
            val y = getEndY(itemStdHeight, i)
            path.lineTo(x, y)
            canvas.drawTextOnPath(getLineText(), path, 0f, 0f, paint)
        }

    }

    private fun isEnoughHeight(itemHeight: Float, times: Int) = itemHeight <= times * lineHeight

    private fun getEndX(itemHeight: Float, times: Int): Float =
        if (isEnoughHeight(
                itemHeight,
                times
            )
        ) width.toFloat() else (lineHeight * times * 1.0f / tan(
            radian
        ))

    private fun getEndY(itemHeight: Float, times: Int): Float =
        if (isEnoughHeight(itemHeight, times)) (lineHeight * times - itemHeight) else 0f

    private fun getItemHeight(): Float {
        return width * tan(radian)
    }

    private fun getLineText(): String {
        val sb = StringBuilder()
        repeat(repeatCountX) {
            sb.append(markText)
            repeat(repeatSpace) {
                sb.append(" ")
            }
        }
        return sb.toString()
    }

    private fun dp2px(dipValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}