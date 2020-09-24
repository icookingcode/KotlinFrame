package com.guc.kframe.base

import android.os.Bundle
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment

/**
 * Created by guc on 2020/7/8.
 * Description：lazy fragment
 */
abstract class BaseLazyFragment : Fragment() {
    //该页面是否准备完毕（onCreateView()方法已调用）
    private var isPrepared = false

    //懒加载方法是否已加载
    private var isLazyLoaded = false
    private var isVisibleToUser = false
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isPrepared = true
        lazyLoad()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        lazyLoad()
    }

    private fun lazyLoad() {
        if (isVisibleToUser && isPrepared && !isLazyLoaded) {
            onLazyLoad()
            isLazyLoaded = true
        }
    }

    @UiThread
    abstract fun onLazyLoad()


    fun <T : BaseSystem> getSystem(className: Class<T>?): T? = SystemManager.getSystem(className)
    inline fun <reified T : BaseSystem> getSystem(): T? = getSystem(T::class.java)
}