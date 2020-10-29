package com.guc.kotlinframe

import android.content.Intent
import android.os.Bundle
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.utils.LogG
import kotlinx.android.synthetic.main.activity_notice_detail.*

class NoticeDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_detail)
        LogG.loge("NoticeDetailActivity", "onCreate")
        tvShow.text = intent.getStringExtra("data")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogG.loge("NoticeDetailActivity", "onNewIntent")
    }
}