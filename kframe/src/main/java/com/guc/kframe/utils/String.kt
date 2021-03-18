package com.guc.kframe.utils

import android.util.Base64
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Created by guc on 2020/4/30.
 * 描述：String 类扩展
 */

/**
 * 重新定义字符串的乘法
 */
operator fun String.times(n: Int) = repeat(n)

/**
 * 扩展函数-统计字符串中字母的个数
 */
fun String.letterCount(): Int {
    var count = 0
    for (char in this) {
        if (char.isLetter()) count++
    }
    return count
}

/**
 * urlDecoder
 */
fun String.urlDecoder(enc: String = "utf-8"): String {
    return URLDecoder.decode(this, enc)
}

/**
 * urlEncoder
 */
fun String.urlEncoder(enc: String = "utf-8"): String {
    return URLEncoder.encode(this, enc)
}

/**
 * 数组编码成base64字符串
 */
fun ByteArray.encodeToBase64String(flags: Int = Base64.DEFAULT): String {
    return Base64.encodeToString(this, flags)
}

/**
 * base64字符串解码成byte数组
 */
fun String.base64DecodeToByteArray(flags: Int = Base64.DEFAULT): ByteArray {
    return Base64.decode(this, flags)
}
