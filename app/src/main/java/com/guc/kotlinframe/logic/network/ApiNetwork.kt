package com.guc.kotlinframe.logic.network

import com.guc.kotlinframe.logic.model.AppInfo
import com.guc.kotlinframe.logic.model.BaseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by guc on 2020/5/21.
 * 描述：Api接口
 */
object ApiNetwork {
    private val service = ServiceCreator.create<AppInfoService>()
    suspend fun getAppInfoData(): List<AppInfo> = service.getAppInfoData().await()
    suspend fun getAppInfoData2(): BaseModel<List<AppInfo>> = service.getAppInfoData2().await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

            })
        }
    }
}