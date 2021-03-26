package com.guc.kframe.system.permission

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.guc.kframe.Engine

typealias PermissionCallback = (Boolean, List<String>) -> Unit

/**
 * Created by guc on 2020/6/5.
 * 描述：不可见的Fragment
 * 用于申请权限
 */
class InvisibleFragment : Fragment() {
    companion object {
        private const val REQUEST_CODE = 1
        private const val REQUEST_CODE_SETTING = 2
        private const val DEF_DENIED_MESSAGE = "您拒绝权限申请，此功能将不能正常使用，您可以去设置页面重新授权"
        private const val DEF_DENIED_CLOSE_BTN_TEXT = "关闭"
        private const val DEF_RATIONAL_MESSAGE = "此功能需要您授权，否则将不能正常使用"
        private const val DEF_RATIONAL_BTN_TEXT = "去设置"
    }

    private var callback: PermissionCallback? = null
    private var permissionsL: Array<String>? = null
    private var isAlwaysRequest = false
    fun requestNow(
        cb: PermissionCallback,
        alwaysRequest: Boolean = false,
        vararg permissions: String
    ) {
        callback = cb
        isAlwaysRequest = alwaysRequest
        permissionsL = Array(permissions.size) { permissions[it] }
        var rationale = false  //是否显示申请理由提示框
        //如果有拒绝则提示申请理由提示框，否则直接向系统请求权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        if (isAlwaysRequest) {
            requestPermissions(permissions, REQUEST_CODE)
            return
        }
        for (permission in permissions) {
            rationale = rationale || shouldShowRequestPermissionRationale(permission)
        }
        if (rationale) {//显示拒绝提示框
            showRationalDialog(*permissions)
        } else {
            requestPermissions(permissions, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SETTING -> permissionsL?.let {
                callback?.let { cb ->
                    requestNow(cb, isAlwaysRequest, *it)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            val deniedList = ArrayList<String>()
            for ((index, result) in grantResults.withIndex()) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permissions[index])
                }
            }
            val allGranted = deniedList.isEmpty()
            if (allGranted) {
                callback?.let { it(allGranted, deniedList) }
            } else {
                showDeniedDialog(deniedList, callback)
            }
        }
    }

    /**
     * 拒绝权限提示框
     *
     * @param permissions
     */
    @Synchronized
    private fun showDeniedDialog(permissions: List<String>, cb: PermissionCallback?) {
        android.app.AlertDialog.Builder(context)
            .setMessage(DEF_DENIED_MESSAGE)
            .setCancelable(false)
            .setNegativeButton(
                DEF_DENIED_CLOSE_BTN_TEXT
            ) { _, _ ->
                cb?.let { it(false, permissions) }
            }
            .setPositiveButton(
                DEF_RATIONAL_BTN_TEXT
            ) { _, _ -> startSetting() }.show()
    }

    /**
     *拒绝提示框
     */
    @Synchronized
    private fun showRationalDialog(vararg permissions: String) {
        android.app.AlertDialog.Builder(context)
            .setMessage(DEF_RATIONAL_MESSAGE)
            .setCancelable(false)
            .setNegativeButton(
                DEF_DENIED_CLOSE_BTN_TEXT
            ) { _: DialogInterface?, _: Int -> activity?.finish() }
            .setPositiveButton(
                DEF_RATIONAL_BTN_TEXT
            ) { _: DialogInterface?, _: Int -> startSetting() }.show()
    }

    /**
     * 跳转到设置界面
     */
    private fun startSetting() {
        try {
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:${Engine.context.packageName}"))
            startActivityForResult(intent, REQUEST_CODE_SETTING)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            try {
                val intent =
                    Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                startActivityForResult(intent, REQUEST_CODE_SETTING)
            } catch (e1: Exception) {
                e1.printStackTrace()
            }
        }
    }
}