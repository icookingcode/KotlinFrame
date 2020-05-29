package com.guc.kotlinframe.model

import com.guc.kframe.base.SystemManager
import com.guc.kframe.system.SystemHttp
import com.guc.kframe.system.net.KCallback
import com.guc.kframe.system.net.KRequest

/**
 * Created by guc on 2020/5/29.
 * 描述：接口
 */
object Api {

    fun getBooks(tag: Any, callback: KCallback<List<Book>>) {
        val request = KRequest.Builder().apply {
            relativeUrl = "get_data.json"
        }.build()
        getHttpSystem()
            ?.request(tag, request, callback)
    }


    private fun getHttpSystem() = SystemManager.getSystem(SystemHttp::class.java)
}