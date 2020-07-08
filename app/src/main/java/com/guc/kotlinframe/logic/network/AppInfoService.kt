package com.guc.kotlinframe.logic.network

import com.guc.kotlinframe.logic.model.AppInfo
import com.guc.kotlinframe.logic.model.BaseModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by guc on 2020/6/4.
 * 描述：定义网络接口
 */
interface AppInfoService {
    @GET("get_data.json")
    fun getAppInfoData(): Call<List<AppInfo>>

    @GET("get_data2.json")
    fun getAppInfoData2(): Call<BaseModel<List<AppInfo>>>
}