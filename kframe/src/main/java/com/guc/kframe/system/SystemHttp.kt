package com.guc.kframe.system

import com.google.gson.Gson
import com.guc.kframe.Constant
import com.guc.kframe.base.BaseSystem
import com.guc.kframe.system.net.KCallback
import com.guc.kframe.system.net.KRequest
import com.guc.kframe.system.net.KResponse
import com.guc.kframe.system.net.KTag
import com.guc.kframe.utils.LogG
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Created by guc on 2020/5/28.
 * 描述：网络请求框架
 */
class SystemHttp : BaseSystem() {

    lateinit var client: OkHttpClient
    private lateinit var poolCall: HashMap<Any, ArrayList<Call>>
    private lateinit var gson: Gson

    companion object {
        const val TAG = "SystemHttp"
        const val MSG1 = "无法连接服务器,请检查网络"
        const val MSG2 = "网络请求失败,请稍后再试"
    }

    override fun initSystem() {
        client = createOkHttpClient()
        poolCall = HashMap()
        gson = Gson()
    }

    override fun destroy() {
    }

    fun <T> request(tag: Any, request: KRequest, callback: KCallback<T>) {
        callback.onStart()
        Observable.create(ObservableOnSubscribe<KResponse<T>> {
            val call = client.newCall(request.getRequest(tag))
            inputCallToPool(tag, call)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    if (!getKTag(call).hasCanceled) {
                        removeCall(tag, call)
                        it.onNext(getKResponse<T>(null, request, callback.respType, MSG1))
                        it.onComplete()
                    }

                }

                override fun onResponse(call: Call, response: Response) {
                    if (!getKTag(call).hasCanceled) {
                        removeCall(tag, call)
                        it.onNext(getKResponse<T>(response, request, callback.respType))
                        it.onComplete()
                    }
                }

            })
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<KResponse<T>> {
                override fun onComplete() {
                    callback.onComplete()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(response: KResponse<T>) {
                    if (response.code === KResponse.SUCCESS) {
                        callback.onSuccess(response.wrapperData, response)
                    } else {
                        callback.onFailure(response)
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    callback.onFailure(
                        getKResponse(
                            null,
                            request,
                            callback.respType,
                            MSG2
                        )
                    )

                }
            })
    }

    //cancel request
    fun cancelRequest(tag: Any) {
        poolCall[tag]?.let {
            for (call in it) {
                call.cancel()
            }
        }
        poolCall.remove(tag)
    }

    //获取返回结果
    private fun <T> getKResponse(
        resp: Response?,
        request: KRequest,
        type: Type,
        msg: String = ""
    ): KResponse<T> {
        return KResponse<T>().apply {
            url = request.url
            fromCache = request.fromCache
            time = System.currentTimeMillis()
            if (resp == null) {
                httpCode = KResponse.FAILURE
                code = KResponse.FAILURE
                this.msg = msg
            } else {
                httpCode = resp.code
                if (resp.isSuccessful) {//请求成功
                    try {
                        val respJson = resp.body!!.string()
                        metaData = respJson //原始数据
                        code = KResponse.SUCCESS
                        if (request.isWrapperResponse) {
                            wrapperData = gson.fromJson(respJson, type)
                        }
                        this.msg = respJson
                    } catch (e: Exception) {
                        e.printStackTrace()
                        code = KResponse.FAILURE
                        this.msg = "数据转换错误"
                    }
                } else {
                    code = KResponse.FAILURE
                    this.msg = "获取数据失败"
                }
            }

        }
    }

    private fun getKTag(call: Call): KTag = call.request().tag() as KTag

    private fun inputCallToPool(tag: Any, call: Call) {
        var calls: ArrayList<Call>? = poolCall[tag]
        if (calls != null) {
            calls.add(call)
        } else {
            calls = ArrayList()
            calls.add(call)
            poolCall[tag] = calls
        }
    }

    fun createOkHttpClient(interceptors: List<Interceptor>? = null): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor() { log ->
                run {
                    LogG.logi(TAG, log)
                }
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).apply {
            if (interceptors != null) {
                for (interceptor in interceptors) {
                    addInterceptor(interceptor)
                }
            }
        }
            .connectTimeout(Constant.TIME_OUT_CONNECT.toLong(), TimeUnit.SECONDS)
            .readTimeout(Constant.TIME_OUT_READ.toLong(), TimeUnit.SECONDS)
            .writeTimeout(Constant.TIME_OUT_WRITE.toLong(), TimeUnit.SECONDS)
            .build()

    private fun removeCall(tag: Any, call: Call) {
        poolCall[tag]?.remove(call)
    }
}