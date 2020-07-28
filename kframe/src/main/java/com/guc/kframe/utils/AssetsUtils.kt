package com.guc.kframe.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by guc on 2020/7/23.
 * Description：
 */
object AssetsUtils {

    /**
     * 将Assets文件读取为字符串
     */
    fun getAssets2String(context: Context, assetFile: String): String {
        val sb = StringBuilder()
        try {
            BufferedReader(
                InputStreamReader(
                    context.assets.open(assetFile)
                )
            ).use {
                it.forEachLine { line ->
                    sb.append(line)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sb.toString()
    }

    inline fun <reified T> getAssets2Object(context: Context, assetFile: String): T {
        return Gson().fromJson(getAssets2String(context, assetFile),
            object : TypeToken<T>() {}.type
        )
    }

}