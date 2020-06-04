package com.guc.kotlinframe

import android.os.Bundle
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.system.net.KCallback
import com.guc.kframe.system.net.KResponse
import com.guc.kframe.widget.selectdialog.DialogSelect
import com.guc.kotlinframe.model.Api
import com.guc.kotlinframe.model.Book
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvSelPicture.setOnClickListener {
            getNetData()
        }
    }

    fun showDialog() {
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

    fun getNetData() {
        Api.getBooks(this, object : KCallback<List<Book>>() {
            override fun onSuccess(data: List<Book>?, resp: KResponse<List<Book>>) {
                super.onSuccess(data, resp)
                tvSelPicture.text = resp.metaData
            }
        })
    }
}
