package com.guc.kframe.system.net

/**
 * Created by guc on 2020/5/28.
 * 描述：回调接口约束
 */
interface Callback<T> {
    fun onStart()
    fun onSuccess(data: T?, resp: KResponse<T>)
    fun onFailure(resp: KResponse<T>)
    fun onComplete()
}