package com.guc.kotlinframe.logic

import androidx.lifecycle.liveData
import com.guc.kotlinframe.logic.model.AppInfo
import com.guc.kotlinframe.logic.network.ApiNetwork
import kotlinx.coroutines.Dispatchers

/**
 * Created by guc on 2020/6/4.
 * 描述：仓库层
 */
object Repository {
    fun getAppInfo() = liveData(Dispatchers.IO) {
        val result = try {
            val resp = ApiNetwork.getAppInfoData()
            Result.success(resp)
        } catch (e: Exception) {
            Result.failure<List<AppInfo>>(e)
        }
        emit(result)
    }
}