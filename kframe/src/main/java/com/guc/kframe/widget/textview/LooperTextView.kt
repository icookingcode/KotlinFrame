package com.guc.kframe.widget.textview

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.TextView

/**
 * Created by guc on 2020/6/8.
 * 描述：轮播展示textView
 */
class LooperTextView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    companion object {

        private const val ANIM_DELAYED_MILLIONS = 3 * 1000

        /**
         * 动画持续时长
         */
        private const val ANIM_DURATION = 1 * 1000
        private const val DEFAULT_TEXT_COLOR = "#2F4F4F"
        private const val DEFAULT_TEXT_SIZE = 16
        private const val TIP_PREFIX = " "
    }

    private var tipList: List<String>? = null
    private var curTipIndex = 0
    private var lastTimeMillis: Long = 0
    private lateinit var tv_tip_out: TextView
    private lateinit var tv_tip_in: TextView
    private var anim_out: Animation? = null
    private var anim_in: Animation? = null

    init {
        initTipFrame()
        initAnimation()
    }

    private fun initTipFrame() {
        tv_tip_out = newTextView()
        tv_tip_in = newTextView()
        addView(tv_tip_in)
        addView(tv_tip_out)
    }

    private fun initAnimation() {
        anim_out = newAnimation(0f, -1f)
        anim_in = newAnimation(1f, 0f)
        anim_in!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                updateTipAndPlayAnimationWithCheck()
            }
        })
    }

    private fun updateTipAndPlayAnimationWithCheck() {
        if (System.currentTimeMillis() - lastTimeMillis < 1000) {
            return
        }
        lastTimeMillis = System.currentTimeMillis()
        updateTipAndPlayAnimation()
    }

    private fun updateTipAndPlayAnimation() {
        if (curTipIndex % 2 == 0) {
            updateTip(tv_tip_out)
            tv_tip_in.startAnimation(anim_out)
            tv_tip_out.startAnimation(anim_in)
            bringChildToFront(tv_tip_in)
        } else {
            updateTip(tv_tip_in)
            tv_tip_out.startAnimation(anim_out)
            tv_tip_in.startAnimation(anim_in)
            bringChildToFront(tv_tip_out)
        }
    }

    private fun updateTip(tipView: TextView) {
        val tip: String = getNextTip()
        if (!TextUtils.isEmpty(tip)) {
            tipView.text = tip + TIP_PREFIX
        }
    }

    /**
     * 获取下一条消息
     *
     * @return
     */
    private fun getNextTip(): String {
        return if (isListEmpty(tipList)) "" else tipList!![curTipIndex++ % tipList!!.size]
    }

    fun setTipList(tipList: List<String>) {
        this.tipList = tipList
        curTipIndex = 0
        updateTip(tv_tip_out!!)
        updateTipAndPlayAnimation()
    }

    /**
     * @return tipList == null -1; 返回下标
     */
    fun getCurTipIndex(): Int {
        return if (isListEmpty(tipList)) -1 else curTipIndex % tipList!!.size
    }

    private fun isListEmpty(list: List<*>?): Boolean {
        return list == null || list.isEmpty()
    }

    private fun newAnimation(
        fromYValue: Float,
        toYValue: Float
    ): Animation? {
        val anim: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            fromYValue,
            Animation.RELATIVE_TO_SELF,
            toYValue
        )
        anim.duration = ANIM_DURATION.toLong()
        anim.startOffset = ANIM_DELAYED_MILLIONS.toLong()
        anim.interpolator = DecelerateInterpolator()
        return anim
    }

    private fun newTextView(): TextView {
        val textView = TextView(context)
        val lp = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
            Gravity.CENTER_VERTICAL
        )
        textView.layoutParams = lp
        textView.compoundDrawablePadding = 10
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setLines(2)
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.setTextColor(Color.parseColor(DEFAULT_TEXT_COLOR))
        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_TEXT_SIZE.toFloat()
        )
        return textView
    }
}