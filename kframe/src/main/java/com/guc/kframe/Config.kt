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
    var logEnable = true //开启log
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