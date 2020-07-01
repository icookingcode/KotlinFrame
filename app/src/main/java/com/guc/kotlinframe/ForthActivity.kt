package com.guc.kotlinframe

import android.os.Bundle
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.utils.LogG
import kotlinx.android.synthetic.main.activity_forth.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class ForthActivity : BaseActivity() {
    private val mExecutor = Executors.newSingleThreadScheduledExecutor()
    private var mFuture: ScheduledFuture<*>? = null
    private var count = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forth)
        mFuture = mExecutor.scheduleWithFixedDelay({
            runOnUiThread {
                tvCounter.text = count.toString()  //不安全，不能在主线程更新UI，即使不崩
            }
//            tvCounter.text = count.toString()  //不安全，不能在主线程更新UI，即使不崩
            count++
        }, 1000, 1000, TimeUnit.MILLISECONDS)

    }

    override fun onDestroy() {
        mFuture?.run {
            if (!isCancelled) cancel(true)
            null
        }
        super.onDestroy()

    }
}