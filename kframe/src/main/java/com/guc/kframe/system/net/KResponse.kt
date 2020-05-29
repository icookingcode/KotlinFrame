package com.guc.kframe.system.net

/**
 * Created by guc on 2020/5/28.
 * 描述：网络回调数据
 */
class KResponse<T> {
    companion object {
        const val SUCCESS = 0
        const val FAILURE = -1
    }

    lateinit var url: String
    var code: Int = FAILURE
    var httpCode: Int = FAILURE
    lateinit var msg: String
    lateinit var metaData: String
    var wrapperData: T? = null
    var time: Long = 0L
    var fromCache: Boolean = false
}