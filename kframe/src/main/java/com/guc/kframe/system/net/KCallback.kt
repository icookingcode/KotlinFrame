package com.guc.kframe.system.net

import com.guc.kframe.utils.getParameterizedType
import java.lang.reflect.Type

/**
 * Created by guc on 2020/5/28.
 * 描述：网络请求回调
 */
abstract class KCallback<T> : Callback<T> {
    lateinit var respType: Type;

    init {
        //获取接口泛型T的class，Type，必须要在子类才能获取Interface的T
        val genType = getParameterizedType(javaClass)
        if (genType != null) {
            val params = genType.actualTypeArguments
            if (params.isNotEmpty()) {
                respType = params[0]
            }
        }
    }

    override fun onStart() {
    }

    override fun onSuccess(data: T?, resp: KResponse<T>) {
    }

    override fun onFailure(resp: KResponse<T>) {
    }

    override fun onComplete() {
    }


}