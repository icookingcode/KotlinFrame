package com.guc.kframe.system.update

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.guc.kframe.Engine
import com.guc.kframe.R
import com.guc.kframe.system.download.DownloadTask
import com.guc.kframe.system.download.Task
import com.guc.kframe.utils.AppTools
import com.guc.kframe.utils.FormatterUtils
import com.guc.kframe.utils.ToastUtil
import kotlinx.android.synthetic.main.layout_dialog_update.*

/**
 * Created by guc on 2020/6/5.
 * 描述：升级检测下载框
 */
class DialogUpdate : DialogFragment(), View.OnClickListener {
    companion object {
        const val DATA = "data"
        fun getInstanceWithArguments(block: Bundle.() -> Unit): DialogUpdate =
            DialogUpdate().apply {
                arguments = Bundle().apply(block)
            }
    }

    private var beanVersion: BeanVersion? = null
    private var task: Task? = null
    private var downloadTask: DownloadTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogAlphaInOut)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window!!.decorView.setPadding(dp2px(30), 0, dp2px(30), 0) //设置左右边距
        return inflater.inflate(R.layout.layout_dialog_update, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beanVersion = arguments?.getParcelable(DATA)
        task = Task(beanVersion?.fileUrl ?: "", downloadPath = beanVersion?.mSaveFileDir)
        btnIgnore.setOnClickListener(this)
        btnUpdate.setOnClickListener(this)
        ivClose.setOnClickListener(this)
        setData()
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        beanVersion?.apply {
            tvTitle.text = "是否升级到${this.newVersion}版本？"
            val i = this.fileSize.toFloat() / (1024 * 1024)
            val updateInfo = FormatterUtils.format("新版本大小：%.2fM\n\n${this.updateJournal}", i)
            tvContent.text = updateInfo
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnIgnore -> dismiss()
            R.id.btnUpdate -> {
                showProgress()
                downloadTask = DownloadTask(Engine.context) {
                    if (downloadTask?.isCanceled != true) {
                        tvProgress.text = "${it.progress}%/100%"
                        progressBar.progress = it.progress
                        tvSpeed.text = it.speed
                    }
                    if (it.status == DownloadTask.STATUS_SUCCESS) {
                        if (downloadTask?.isCanceled != true) {
                            tvProgress.text = "下载完成"
                        }
                        AppTools.installApp(it.filePath)
                    }
                }
                downloadTask?.execute(task)
            }
            R.id.ivClose -> {
                downloadTask?.isCanceled = true
                ToastUtil.toast("取消下载")
                this.dismiss()
            }
        }
    }

    private fun showProgress(isShow: Boolean = true) {
        progressLayout.visibility = (if (isShow) View.VISIBLE else View.GONE)
        btnUpdate.visibility = (if (isShow) View.VISIBLE else View.GONE)
        btnUpdate.visibility = (if (isShow) View.GONE else View.VISIBLE)
    }

    private fun dp2px(dp: Int): Int {
        return (Engine.context.resources.displayMetrics.density * dp + 0.5).toInt()
    }
}