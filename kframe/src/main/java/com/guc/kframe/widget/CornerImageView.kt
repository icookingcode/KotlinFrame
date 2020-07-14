package com.guc.kframe.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.guc.kframe.R
import com.guc.kframe.utils.ScreenUtils

/**
 * Created by guc on 2020/6/9.
 * 描述：圆角ImageView
 */
class CornerImageView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    AppCompatImageView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var width = 0f
    private var height = 0f
    private val defaultRadius = 0
    private var isCircle = false
    private var radius = 0
    private var leftTopRadius = 0
    private var rightTopRadius = 0
    private var rightBottomRadius = 0
    private var leftBottomRadius = 0

    init {
        // 读取配置
        val array = context.obtainStyledAttributes(attrs, R.styleable.CornerImageView)
        isCircle = array.getBoolean(R.styleable.CornerImageView_is_circle, false)
        radius = array.getDimensionPixelOffset(R.styleable.CornerImageView_radius, defaultRadius)
        leftTopRadius = array.getDimensionPixelOffset(
            R.styleable.CornerImageView_left_top_radius,
            defaultRadius
        )
        rightTopRadius = array.getDimensionPixelOffset(
            R.styleable.CornerImageView_right_top_radius,
            defaultRadius
        )
        leftBottomRadius = array.getDimensionPixelOffset(
            R.styleable.CornerImageView_left_bottom_radius,
            defaultRadius
        )
        rightBottomRadius = array.getDimensionPixelOffset(
            R.styleable.CornerImageView_right_bottom_radius,
            defaultRadius
        )
        array.recycle()
        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (defaultRadius == leftTopRadius) {
            leftTopRadius = radius
        }
        if (defaultRadius == rightTopRadius) {
            rightTopRadius = radius
        }
        if (defaultRadius == rightBottomRadius) {
            rightBottomRadius = radius
        }
        if (defaultRadius == leftBottomRadius) {
            leftBottomRadius = radius
        }
    }

    fun setCornerRadius(dp: Int) {
        this.radius = ScreenUtils.dp2px(dp)
        this.leftTopRadius = this.radius
        this.leftBottomRadius = this.radius
        this.rightTopRadius = this.radius
        this.rightBottomRadius = this.radius
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        width = getWidth().toFloat()
        height = getHeight().toFloat()
        if (isCircle) {
            val maxRadius = (width.coerceAtMost(height) / 2).toInt()
            radius = if (radius == 0 || radius > maxRadius) maxRadius else radius
        }
    }

    override fun onDraw(canvas: Canvas) {
        val path = Path()
        if (isCircle) {
            path.addCircle(
                width / 2,
                height / 2,
                radius.toFloat(),
                Path.Direction.CCW
            )
        } else {
            //四个角：右上，右下，左下，左上
            path.moveTo(leftTopRadius.toFloat(), 0f)
            path.lineTo(width - rightTopRadius, 0f)
            path.quadTo(width, 0f, width, rightTopRadius.toFloat())
            path.lineTo(width, height - rightBottomRadius)
            path.quadTo(width, height, width - rightBottomRadius, height)
            path.lineTo(leftBottomRadius.toFloat(), height)
            path.quadTo(0f, height, 0f, height - leftBottomRadius)
            path.lineTo(0f, leftTopRadius.toFloat())
            path.quadTo(0f, 0f, leftTopRadius.toFloat(), 0f)
        }
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}