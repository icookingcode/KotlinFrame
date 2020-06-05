package com.guc.kframe.system

import androidx.fragment.app.FragmentActivity
import com.guc.kframe.base.BaseSystem
import com.guc.kframe.system.permission.InvisibleFragment
import com.guc.kframe.system.permission.PermissionCallback

/**
 * Created by guc on 2020/6/5.
 * 描述：权限
 */
class SystemPermission : BaseSystem() {
    companion object {
        private const val TAG = "InvisibleFragment"
    }

    override fun initSystem() {
    }

    fun request(
        activity: FragmentActivity,
        vararg permissions: String,
        callback: PermissionCallback
    ) {
        val fragmentManager = activity.supportFragmentManager
        val existedFragment = fragmentManager.findFragmentByTag(TAG)
        val fragment = if (existedFragment != null) {
            existedFragment as InvisibleFragment
        } else {
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment, TAG).commitNow()
            invisibleFragment
        }
        fragment.requestNow(callback, *permissions)
    }

    override fun destroy() {
    }
}