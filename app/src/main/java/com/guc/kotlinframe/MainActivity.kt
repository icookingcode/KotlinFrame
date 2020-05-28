package com.guc.kotlinframe

import android.os.Bundle
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.widget.selectdialog.DialogSelect
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvSelPicture.setOnClickListener {
            val dialog = DialogSelect<String>(this, false) { isSel, selDatas ->
                run {
                    if (isSel) selDatas?.apply {
                        tvSelPicture.text = this[0]
                    }
                }
            }
            dialog.datas = listOf("Kotlin", "Java", "JavaScript", "c", "c++")
            dialog.show()
        }
    }
}
