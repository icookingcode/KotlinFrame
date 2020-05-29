package com.guc.kframe

/**
 * Created by guc on 2020/5/28.
 * 描述：应用配置
 */
class Config {
    companion object {
        const val MODEL_DEBUG = 0
        const val MODEL_BETA = 1
        const val MODEL_RELEASE = 2
    }

    var currentMode = MODEL_DEBUG
        set(value) {
            if (value == MODEL_RELEASE) {
                enableSaveCrashLog = false
                logEnable = false
            }
            field = value
        }
    var logEnable = true //开启log
    var enableSaveCrashLog = true; //保存崩溃日志
    var logNativeDir = ""
    var enableCrashReset = false; //关闭崩溃重启
    var urlDebug: String? = null //
    var urlBeta: String? = null //
    var urlRelease: String? = null //

    fun getBaseUrl() = when (currentMode) {
        MODEL_DEBUG -> urlDebug
        MODEL_BETA -> urlBeta
        MODEL_RELEASE -> urlRelease
        else -> urlDebug
    }
}