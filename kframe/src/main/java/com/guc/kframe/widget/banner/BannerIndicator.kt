package com.guc.kframe.widget.banner

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.guc.kframe.R
import com.guc.kframe.utils.ScreenUtils

/**
 * Created by guc on 2020/7/20.
 * Description：轮播指示器
 */
class BannerIndicator(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var dashGap = 0
    private var position = 0
    private var sliderWidth = 0
    private var sliderHeight = 0
    private var sliderAlign = 0
    private val selectColor = Color.parseColor("#517aff")
    private val unSelectColor = Color.parseColor("#ebebeb")
    private var isLoop = false //true:循环指示，数据比真实数据多两条，第一条，放真实数据最后一条；最后一条放真实数据第一条

    init {
        orientation = HORIZONTAL
        //获取自定义属性
        val array = context.obtainStyledAttributes(attrs, R.styleable.BannerIndicator)
        dashGap =
            array.getDimension(R.styleable.BannerIndicator_gap, ScreenUtils.dp2px(15).toFloat())
                .toInt()
        sliderWidth = array.getDimension(
            R.styleable.BannerIndicator_slider_width,
            ScreenUtils.dp2px(12).toFloat()
        ).toInt()
        sliderHeight = array.getDimension(
            R.styleable.BannerIndicator_slider_height,
            ScreenUtils.dp2px(4).toFloat()
        ).toInt()
        sliderAlign = array.getInt(R.styleable.BannerIndicator_slider_align, 0)
        isLoop = array.getBoolean(R.styleable.BannerIndicator_isLoop, false)
        gravity = when (sliderAlign) {
            0 -> Gravity.CENTER
            1 -> Gravity.START
            2 -> Gravity.END
            else -> Gravity.CENTER
        }
        array.recycle()
    }

    //和viewpager联动,根据viewpager页面动态生成相应的小圆点
    fun setUpWithViewPager(viewPager: ViewPager?) {
        removeAllViews()
        if (viewPager == null) return
        if (viewPager.adapter == null || viewPager.adapter!!.count < 2
        ) {
            if (isLoop) {
                return
            }
        }
        position = 0
        for (i in 0 until if (isLoop) viewPager.adapter!!.count - 2 else viewPager.adapter!!.count) {
            val itemView = ImageView(context)
            val layoutParams = LayoutParams(
                sliderWidth,
                sliderHeight
            )
            //            //设置小圆点之间的距离
            if (i > 0) {
                layoutParams.setMargins(dashGap, 0, 0, 0)
                itemView.setBackgroundColor(unSelectColor)
            } else {
                layoutParams.setMargins(0, 0, 0, 0)
                itemView.setBackgroundColor(selectColor)
            }
            itemView.layoutParams = layoutParams
            addView(itemView)
            //默认第一个指示器增大
            setSelect(0)
            if (isLoop) {
                viewPager.setCurrentItem(1, false)
            }
        }
        //根据viewpager页面切换事件，设置指示器大小变化
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (isLoop) {
                    val pos: Int = viewPager.adapter!!.count
                    if (position == pos - 1 || position == 0) { //当切换到第0个或者最后一个不做处理
                        if (position == pos - 1) viewPager.setCurrentItem(1, false)
                        if (position == 0) viewPager.setCurrentItem(pos - 2, false)
                        return
                    }
                    if (this@BannerIndicator.position != position - 1) {
                        resetSize(this@BannerIndicator.position, position - 1)
                        this@BannerIndicator.position = position - 1
                    }
                } else {
                    resetSize(this@BannerIndicator.position, position)
                    this@BannerIndicator.position = position
                }

            }
        })
    }

    //重置指示器样式
    private fun resetSize(last: Int, current: Int) {
        setSelect(current)
        setLast(last)
    }

    private fun setLast(last: Int) {
        val objectAnimator =
            ObjectAnimator.ofInt(getChildAt(last), "backgroundColor", selectColor, unSelectColor)
        objectAnimator.duration = 300
        objectAnimator.setEvaluator(ArgbEvaluator())
        objectAnimator.start()
    }

    private fun setSelect(current: Int) {
        getChildAt(current).setBackgroundColor(selectColor)
        val objectAnimator =
            ObjectAnimator.ofInt(getChildAt(current), "backgroundColor", unSelectColor, selectColor)
        objectAnimator.duration = 300
        objectAnimator.setEvaluator(ArgbEvaluator())
        objectAnimator.start()
    }

}
