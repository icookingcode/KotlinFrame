package com.guc.kotlinframe

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.guc.kframe.adapter.CommonAdapter4Rcv
import com.guc.kframe.adapter.ViewHolder4RecyclerView
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.system.SystemDownload
import com.guc.kframe.system.SystemPermission
import com.guc.kframe.system.download.DownloadTask
import com.guc.kframe.system.download.Task
import com.guc.kframe.system.net.KCallback
import com.guc.kframe.system.net.KResponse
import com.guc.kframe.system.update.BeanVersion
import com.guc.kframe.system.update.DialogUpdate
import com.guc.kframe.utils.ToastUtil
import com.guc.kframe.widget.selectdialog.DialogSelect
import com.guc.kotlinframe.logic.model.AppInfo
import com.guc.kotlinframe.logic.network.Api
import com.guc.kotlinframe.ui.AppInfoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

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
        tvSelFav.setOnClickListener {
            showDialog()
        }
        viewModel.appInfo.observe(this, Observer { result ->
            val apps = result.getOrNull()
            if (apps != null) {
                viewModel.appInfoList.addAll(apps)
                rcvContent.adapter?.notifyDataSetChanged()
            } else {
                ToastUtil.toast("未能获取数据")
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            thread {
                Thread.sleep(1000)
                runOnUiThread {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        tvRequestCall.setOnClickListener {
            getSystem(SystemPermission::class.java)?.request(
                this,
                Manifest.permission.CALL_PHONE
            ) { allGranted, _ ->
                if (allGranted) {
                    ToastUtil.toast("同意该权限")
                } else {
                    ToastUtil.toast("不同意")
                }
            }
        }
        tvShowDialog.setOnClickListener {
            getSystem(SystemPermission::class.java)?.request(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) { bool, _ ->
                if (bool) {
                    run {
                        val dialogUpdate = DialogUpdate.getInstanceWithArguments {
                            putParcelable(
                                DialogUpdate.DATA,
                                BeanVersion().apply {
                                    fileUrl =
                                        "https://down.qq.com/qqweb/QQ_1/android_apk/Android_8.3.6.4590_537064458.apk"
                                })
                        }
                        dialogUpdate.show(supportFragmentManager, "dialog")
                    }
                } else {
                    ToastUtil.toast("没有存储权限")
                }

            }

        }

        looperText.setTipList(listOf("你好啊", "guc"))
        syncView.syncView = syncView2
        syncView2.syncView = syncView

//        banner.setImages(
//            listOf(
//                "https://img.51miz.com/Element/00/58/81/79/73ede7c5_E588179_39772d60.jpg!/quality/90/unsharp/true/compress/true/format/jpg",
//                "https://img.51miz.com/Element/00/59/31/31/517f23f9_E593131_a0cd1b59.jpg!/quality/90/unsharp/true/compress/true/format/jpg",
//                "https://img.51miz.com/Element/00/59/47/06/40456783_E594706_33c392a8.jpg!/quality/90/unsharp/true/compress/true/format/jpg"
//            )
//        ).setAutoPlay(true).start()

        val task =
            Task("https://down.qq.com/qqweb/QQ_1/android_apk/Android_8.3.6.4590_537064458.apk")
        tvDownload.setOnClickListener {
            getSystem(SystemDownload::class.java)?.download(task) {
                when (it.status) {
                    DownloadTask.STATUS_WAIT ->
                        tvDownload.text = "等待下载"
                    DownloadTask.STATUS_SUCCESS ->
                        tvDownload.text = "下载成功"
                    DownloadTask.STATUS_CANCEL ->
                        tvDownload.text = "取消下载"
                    DownloadTask.STATUS_PAUSED ->
                        tvDownload.text = "暂停下载"
                    DownloadTask.STATUS_LOADING ->
                        tvDownload.text = "下载中...${it.progress}/100"
                }
            }
        }
        tvPaused.setOnClickListener {
            getSystem(SystemDownload::class.java)?.pause(task)
        }
    }

    fun showDialog() {
        val dialog = DialogSelect<String>(this, false) { isSel, selDatas ->
            run {
                if (isSel) selDatas?.apply {
                    tvSelFav.text = this[0]
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
