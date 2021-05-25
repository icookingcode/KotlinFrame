package com.guc.kframe.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.guc.kframe.Constant
import com.guc.kframe.R
import com.guc.kframe.system.SystemHttp
import com.guc.kframe.system.SystemWaterMark
import com.guc.kframe.utils.LogG
import com.guc.kframe.widget.dialog.LoadingDialog

/**
 * Created by guc on 2020/4/28.
 * 描述：基类
 */
open class BaseActivity : AppCompatActivity() {
    private var loadingDialog: LoadingDialog? = null
    protected lateinit var context: Context
    lateinit var receiver: ForceOfflineReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        supportActionBar?.hide()
        ActivityCollector.addActivity(this)
        LogG.logi("BaseActivity", "taskId : $taskId  ${javaClass.simpleName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
        getSystem<SystemHttp>()?.cancelRequest(this)
    }

    override fun onStart() {
        super.onStart()
        getSystem<SystemWaterMark>()?.onActivityStart(this)
    }

    override fun onResume() {
        super.onResume()
        receiver = ForceOfflineReceiver()
        val intentFilter = IntentFilter(Constant.ACTION_FORCE_OFFLINE)
        registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    inner class ForceOfflineReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context, p1: Intent?) {
            AlertDialog.Builder(p0).apply {
                setTitle("警告")
                setMessage("确定要退出软件？")
                setCancelable(false)
                setPositiveButton("确定") { _, _ ->
                    ActivityCollector.finishAll()
                }
                show()
            }
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    protected fun setImmerseStatusBar(isDarkText: Boolean = false) { //目前只适配5.0以上系统
        // 设置图片沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var option =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            val decorView = window.decorView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isDarkText) {
                option = option or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = option
        }
    }

    /**
     * 设置状态栏为黑字亮色背景（默认白色）
     */
    protected fun setLightStatusBar(statusBarColorResId: Int = R.color.colorWhite) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window
                .decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            this.window.statusBarColor =
                ResourcesCompat.getColor(resources, statusBarColorResId, null)

        }
    }

    /**
     * 显示加载提示框
     */
    protected fun showLoading(isShow: Boolean, msg: String = getString(R.string.common_loading)) {
        if (isShow) loadingDialog = loadingDialog.let {
            it ?: LoadingDialog(this)
        }.apply {
            setTips(msg)
            if (!isShowing) {
                show()
            }
        }
        else loadingDialog?.dismiss()
    }

    fun <T : BaseSystem> getSystem(className: Class<T>?): T? = SystemManager.getSystem(className)
    inline fun <reified T : BaseSystem> getSystem(): T? = getSystem(T::class.java)
}