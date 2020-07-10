package com.guc.kotlinframe

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.utils.KeyWordUtils
import com.guc.kframe.utils.TimeFormatUtils
import com.guc.kframe.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_forth.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ForthActivity : BaseActivity() {
    private val mExecutor = Executors.newSingleThreadScheduledExecutor()
    private var mFuture: ScheduledFuture<*>? = null
    private var count = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forth)
        mFuture = mExecutor.scheduleWithFixedDelay({
            runOnUiThread {
                tvCounter.text = TimeFormatUtils.date2String(Date())
            }
//            tvCounter.text = count.toString()  //不安全，不能在主线程更新UI，即使不崩
            count++
        }, 1000, 1000, TimeUnit.MILLISECONDS)

        btnFormat.setOnClickListener {
            timeFormat()
        }

        animTest()
        searchViewTest()
    }

    private fun searchViewTest() {
        searchView.onConfirmClicked = { isEmpty, key ->
            if (!isEmpty) {
                ToastUtil.toast(key ?: "哈哈")
            }
        }
    }

    private fun animTest() {
        tvFloat.text = KeyWordUtils.handleText(Color.RED, "漂浮TextView", "TextView")
        startAnimator(tvFloat, 1000)
    }

    private fun timeFormat() {
        val timeString: String = etTimeString.text.toString()
        val oldPattern: String = etOldPattern.text.toString()
        val newPattern: String = etNewPattern.text.toString()
        etTimeString.setText(TimeFormatUtils.formatConversion(timeString, oldPattern, newPattern))
    }

    override fun onDestroy() {
        mFuture?.run {
            if (!isCancelled) cancel(true)
            null
        }
        tvFloat.clearAnimation()
        super.onDestroy()

    }

    @SuppressLint("WrongConstant")
    fun startAnimator(view: View?, delay: Long) {
        val translationX: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "translationX", 0f, 5f, 0f, -5f)
        translationX.repeatMode = ValueAnimator.INFINITE
        translationX.repeatCount = -1
        val translationY: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "translationY", 0f, 5f, 0f, -5f)
        translationY.repeatMode = ValueAnimator.INFINITE
        translationY.repeatCount = -1
        val animatorSet = AnimatorSet() //组合动画
        animatorSet.playTogether(translationX, translationY) //设置动画
        animatorSet.duration = 1500 //设置动画时间
        animatorSet.startDelay = delay //启动
        animatorSet.start()
    }
}