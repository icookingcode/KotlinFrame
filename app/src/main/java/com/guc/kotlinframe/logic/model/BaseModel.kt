package com.guc.kotlinframe.logic.model

/**
 * Created by guc on 2020/7/8.
 * Description：
 */
open class BaseModel<T>(val code: Int, val msg: String) {
    var data: T? = null
}