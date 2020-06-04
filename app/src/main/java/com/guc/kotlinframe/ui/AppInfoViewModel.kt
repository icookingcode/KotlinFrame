package com.guc.kotlinframe.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.guc.kotlinframe.logic.Repository
import com.guc.kotlinframe.logic.model.AppInfo

/**
 * Created by guc on 2020/6/4.
 * 描述：ViewModel
 */
class AppInfoViewModel : ViewModel() {
    private val tag = MutableLiveData<String>()
    val appInfoList = ArrayList<AppInfo>()
    val appInfo = Transformations.switchMap(tag) {
        Repository.getAppInfo()
    }

    fun getAppInfo() {
        tag.value = "AppInfoViewModel"
    }
}