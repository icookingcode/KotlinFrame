package com.guc.kframe.system.net

import android.text.TextUtils
import com.google.gson.Gson
import com.guc.kframe.Engine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * Created by guc on 2020/5/28.
 * 描述：自己封装的请求
 */
class KRequest(private val builder: Builder) {

    companion object {
        const val TYPE_GET = 0
        const val TYPE_POST = 1
        const val TYPE_MULTIPART = 2
        const val TYPE_SPECIAL = 3
        const val TYPE_JSON = 4
        val MEDIA_TYPE_PNG: MediaType? = "image/png".toMediaTypeOrNull()
    }

    val url: String = builder.url ?: ""
    val requestType = builder.requestType
    private val params = builder.params
    private val headers: Map<String, String>? = builder.headers
    private val filePaths = builder.filePaths
    val isWrapperResponse = builder.isWrapperResponse
    val fromCache = builder.fromCache
    val fileParamName = builder.fileParamName  //文件上传文件名 默认:file
    val jsonParam = builder.jsonParam
    fun getRequest(tag: Any): Request {
        val builder2 = Request.Builder()
        builder2.tag(KTag(tag))
        when (requestType) {
            TYPE_GET -> {//get
                builder2.get()
                builder2.url(getGetUrl(url, params))
            }
            TYPE_POST -> {//post
                builder2.url(url)
                var body = getPostRequestBody(params)
                builder2.post(body = body)
            }
            TYPE_MULTIPART -> {//multipart
                builder2.url(url)
                var body = getMultipartRequestBody(params, filePaths)
                builder2.post(body = body)
            }
            TYPE_SPECIAL -> {//json
                builder2.url(url)
                val gson = Gson()
                val p = gson.toJson(params)
                val body = p.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
                builder2.post(body)
                builder2.header("Content-type", "application/json;charset=UTF-8")
            }
            TYPE_JSON -> {
                builder2.url(url)
                val body =
                    jsonParam?.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
                        ?: throw IllegalArgumentException("json param cannot be null")
                builder2.post(body)
                builder2.header("Content-type", "application/json;charset=UTF-8")
            }
        }
        if (!headers.isNullOrEmpty()) {
            headers.forEach { entry ->
                builder2.addHeader(entry.key, entry.value)
            }
        }
        return builder2.build()
    }

    /**
     * Multipart请求
     */
    private fun getMultipartRequestBody(
        params: Map<String, String>,
        filePaths: List<String>
    ): RequestBody {
        var formBody = MultipartBody.Builder()
        for (entry in params) {
            formBody.addFormDataPart(entry.key, entry.value)
        }
        for (fp in filePaths) {
            val file = File(fp)
            formBody.addFormDataPart(fileParamName, file.name, file.asRequestBody(MEDIA_TYPE_PNG))
        }
        return formBody.build()
    }

    /**
     * post请求
     */
    private fun getPostRequestBody(params: Map<String, String>): RequestBody {
        var formBody = FormBody.Builder()
        for (entry in params) {
            formBody.add(entry.key, entry.value)
        }
        return formBody.build()
    }

    /**
     * get请求
     */
    private fun getGetUrl(url: String, params: Map<String, String>): String {
        val sb = StringBuilder()
        sb.append(url)
        if (params.isNotEmpty()) {
            sb.append("?")
        }
        for (entry in params) {
            sb.append("${entry.key}=${entry.value}&")
        }
        if (sb.lastIndexOf("&") == sb.length - 1) {
            sb.replace(sb.length - 1, sb.length, "")
        }
        return sb.toString()
    }

    class Builder {
        var baseUrl: String? = Engine.config.getBaseUrl()
        var relativeUrl: String? = null
        var url: String? = null
        var requestType = TYPE_GET
        var params: Map<String, String> = HashMap()
        var headers: Map<String, String>? = null
        var filePaths: List<String> = ArrayList()
        var isWrapperResponse = true
        var fromCache = false
        var fileParamName = "file"
        var jsonParam: String? = null
        fun build(urlCustom: String? = null): KRequest {
            return urlCustom?.let {
                url = it
                KRequest(this)
            } ?: let {
                url = if (TextUtils.isEmpty(relativeUrl)) {
                    baseUrl
                } else {
                    baseUrl + relativeUrl
                }
                KRequest(this)
            }
        }
    }
}