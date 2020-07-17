package com.guc.kframe.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import com.guc.kframe.Engine
import java.io.File


/**
 * Created by guc on 2020/5/29.
 * 描述：App基本信息工具
 */
object AppTools {
    //获取App版本名称
    fun getVersionName() = getVersionName(Engine.context.packageName)

    //获取版本号
    fun getVersionCode() = getVersionCode(Engine.context.packageName)

    //安装应用
    fun installApp(apkFilePath: String?) {
        val file = File(apkFilePath)
        if (!file.exists()) return
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri? = FileUtils.getUriForFile(file)
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        intent.setDataAndType(data, type)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Engine.context.startActivity(intent)
    }

    /**
     * 启动APP
     */
    fun startAPP(appPackageName: String, context: Context = Engine.context) {
        try {
            val intent: Intent? = context.packageManager.getLaunchIntentForPackage(appPackageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            ToastUtil.toast("未安装")
        }
    }

    /**
     * 检测是否安装某应用
     */
    fun checkAppInstalled(pkgName: String, context: Context = Engine.context): Boolean {
        if (pkgName.isEmpty()) {
            return false
        }
        val packageManager = context.packageManager
        // 获取所有已安装程序的包信息
        val infoList = packageManager.getInstalledPackages(0)
        if (infoList.isEmpty())
            return false
        for (info in infoList) {
            if (pkgName == info.packageName) {
                return true
            }
        }
        return false
    }

    private fun getVersionName(packageName: String): String =
        if (TextUtils.isEmpty(packageName)) "" else {
            try {
                val pm: PackageManager = Engine.context.packageManager
                val pi = pm.getPackageInfo(packageName, 0)
                pi?.versionName ?: ""
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                ""
            }
        }

    private fun getVersionCode(packageName: String): Int =
        if (TextUtils.isEmpty(packageName)) 0 else {
            try {
                val pm: PackageManager = Engine.context.packageManager
                val pi = pm.getPackageInfo(packageName, 0)
                pi?.versionCode ?: 0
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                0
            }
        }
}