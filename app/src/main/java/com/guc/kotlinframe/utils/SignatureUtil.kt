package com.guc.kotlinframe.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import com.guc.kframe.utils.LogG
import okhttp3.internal.and
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

/**
 * Created by guc on 2021/4/8.
 * Description：签名工具
 */
object SignatureUtil {
    private const val TAG = "SignatureUtil"

    /**
     * 获取签名
     */
    fun getSignature(context: Context): String {
        val pm = context.packageManager
        val pi: PackageInfo
        val sb = StringBuilder()
        try {
            pi = pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            val signatures: Array<Signature> = pi.signatures
            for (signature in signatures) {
                sb.append(signature.toCharsString())
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return sb.toString().apply {
            LogG.loge(TAG, this)
        }
    }

    /**
     * 获取证书SHA1
     */
    fun getCertSHA1(context: Context): String {
        try {
            val pm = context.packageManager
            var pkgInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            val signature = pkgInfo.signatures
            val sign = signature[0]
            val certFactory = CertificateFactory.getInstance("X.509")
            val signBytes = sign.toByteArray()
            ByteArrayInputStream(signBytes).use {
                //获取X509证书
                val cert = certFactory.generateCertificate(it) as X509Certificate
                val sha1 = MessageDigest.getInstance("SHA1")
                val certByte = cert.encoded
                val bs = sha1.digest(certByte)
                return byte2Hex(bs)
            }
        } catch (e: Exception) {
        }
        return "unknown"
    }

    private fun md5Encrypt(str: String?, tag: String = "应用签名") =
        str?.run {
            try {
                val md5 = MessageDigest.getInstance("MD5").apply {
                    reset()
                }
                val bytes = md5.digest(this.toByteArray())
                val result = StringBuilder()
                for (b in bytes) {
                    var temp = Integer.toHexString(b and 0xff)
                    if (temp.length == 1) {
                        temp = "0$temp"
                    }
                    result.append(temp)
                }
                val r = result.toString()
                LogG.loge(TAG, "$tag：$r")
                r
            } catch (e: Exception) {
                ""
            }
        } ?: run {
            ""
        }

    private fun byte2Hex(bs: ByteArray) = StringBuilder().apply {
        for ((i, b) in bs.withIndex()) {
            var temp = Integer.toHexString(b and 0xff)
            if (temp.length == 1) {
                temp = "0$temp"
            }
            this.append(temp)
            if (i < bs.size - 1) this.append(":")
        }
    }.toString()
}