package com.guc.kframe.system

import android.os.Build
import com.guc.kframe.Engine
import com.guc.kframe.base.BaseSystem
import com.guc.kframe.utils.AppTools
import com.guc.kframe.utils.FileUtils
import com.guc.kframe.utils.hashMap
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Created by guc on 2020/5/29.
 * 描述：程序错误处理系统
 */
class SystemCrash : BaseSystem() {
    var defaultHandler: Thread.UncaughtExceptionHandler? = null
    var baseMsg: String? = null
    override fun initSystem() {
        baseMsg = getBaseMessage()
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            run {
                if (Engine.config.enableSaveCrashLog) {
                    var msg = getCrashLog(baseMsg, e)
                    var logFile =
                        Engine.config.logNativeDir + "/crash-${System.currentTimeMillis()}.log"
                    FileUtils.writeStr2File(logFile, msg, true)
                }
                if (Engine.config.enableCrashReset) {
                    //崩溃重启
                } else {
                    defaultHandler?.uncaughtException(t, e)
                }
            }
        }
    }


    override fun destroy() {
        baseMsg = null
        defaultHandler = null
    }

    //获取崩溃日志
    private fun getCrashLog(baseMsg: String?, e: Throwable): String = baseMsg?.run {
        baseMsg + getExceptionInfo(e)
    } ?: getExceptionInfo(e)

    //获取异常信息
    private fun getExceptionInfo(e: Throwable): String {
        val mStringWriter = StringWriter()
        val mPrintWriter = PrintWriter(mStringWriter)
        e.printStackTrace(mPrintWriter)
        mPrintWriter.close()
        return mStringWriter.toString()
    }

    private fun getBaseMessage(): String? {
        val builder = StringBuilder()
        for ((key, value) in getBaseMessageMap().entries) {
            builder.append(key).append(" = ").append(value).append("\n")
        }
        return builder.toString()
    }

    private fun getBaseMessageMap() = hashMap {
        this["versionName"] = AppTools.getVersionName()
        this["versionCode"] = "" + AppTools.getVersionCode()
        this["MODEL"] = "" + Build.MODEL
        this["SDK_INT"] = "" + Build.VERSION.SDK_INT
        this["PRODUCT"] = "" + Build.PRODUCT
    }
}