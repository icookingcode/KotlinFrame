package com.guc.kotlinframe

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.guc.kframe.adapter.CommonAdapter4Rcv
import com.guc.kframe.adapter.ViewHolder4RecyclerView
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.system.net.KCallback
import com.guc.kframe.system.net.KResponse
import com.guc.kframe.utils.ToastUtil
import com.guc.kframe.widget.selectdialog.DialogSelect
import com.guc.kotlinframe.logic.model.AppInfo
import com.guc.kotlinframe.logic.network.Api
import com.guc.kotlinframe.ui.AppInfoViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application).create(AppInfoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rcvContent.layoutManager = LinearLayoutManager(this)
        rcvContent.adapter = object : CommonAdapter4Rcv<AppInfo>(viewModel.appInfoList) {
            override fun getRootView(parent: ViewGroup, viewType: Int): View {
                return layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            }

            override fun bindData(
                viewHolder: ViewHolder4RecyclerView,
                position: Int,
                data: AppInfo,
                itemType: Int
            ) {
                viewHolder.setText(android.R.id.text1, data.toString())
            }

        }
        tvSelPicture.setOnClickListener {
            viewModel.getAppInfo()
        }
        tvClear.setOnClickListener {
            viewModel.appInfoList.clear()
            rcvContent.adapter?.notifyDataSetChanged()
        }
        viewModel.appInfo.observe(this, Observer { result ->
            val apps = result.getOrNull()
            if (apps != null) {
                viewModel.appInfoList.clear()
                viewModel.appInfoList.addAll(apps)
                rcvContent.adapter?.notifyDataSetChanged()
            } else {
                ToastUtil.toast("未能获取数据")
                result.exceptionOrNull()?.printStackTrace()
            }
        })
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
        Api.getBooks(this, object : KCallback<List<AppInfo>>() {
            override fun onSuccess(data: List<AppInfo>?, resp: KResponse<List<AppInfo>>) {
                super.onSuccess(data, resp)
                tvSelPicture.text = resp.metaData
            }
        })
    }
}
