package com.guc.kotlinframe

import android.os.Bundle
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.ui.SelectPictureActivity
import com.guc.kframe.widget.LoadingDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadingDialog(this).show()
        tvSelPicture.setOnClickListener { SelectPictureActivity.start(this) }
    }
}
