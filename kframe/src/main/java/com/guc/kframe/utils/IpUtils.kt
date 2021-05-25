package com.guc.kframe.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission
import java.net.Inet4Address
import java.net.NetworkInterface

/**
 * Created by guc on 2021/4/15.
 * Description：获取IP工具
 */
object IPUtils {

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun getIPAddress(context: Context): String {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.run {
            if (this.isConnected) {
                var ip = "未获取到"
                when (type) {
                    ConnectivityManager.TYPE_MOBILE -> {
                        try {
                            val en = NetworkInterface.getNetworkInterfaces()
                            while (en.hasMoreElements()) {
                                val intf = en.nextElement()
                                for (inetIp in intf.inetAddresses) {
                                    if (!inetIp.isLoopbackAddress && inetIp is Inet4Address) {
                                        return inetIp.hostAddress
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                    ConnectivityManager.TYPE_WIFI -> {
                        (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).let {
                            val wifiInfo = it.connectionInfo
                            ip = intIp2String(wifiInfo.ipAddress)//得到IPV4地址
                        }
                    }
                }
                ip
            } else {
                "无网络"
            }
        } ?: run {
            "无网络"
        }
    }

    private fun intIp2String(ip: Int) = (ip and 0xFF).toString() + "." +
            (ip shr 8 and 0xFF) + "." +
            (ip shr 16 and 0xFF) + "." +
            (ip shr 24 and 0xFF)


}