package com.guc.kframe.utils

import android.content.Context
import android.os.Environment
import com.guc.kframe.Engine
import java.io.File
import java.math.BigDecimal

/**
 * Created by guc on 2020/7/9.
 * Description：缓存管理工具
 * cacheDir / externalCacheDir
 */
object CacheManagerUtils {
    /**
     * 获取缓存大小
     * @context Context
     */
    fun getTotalCacheSize(context: Context = Engine.context): String {
        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState() == (Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.externalCacheDir)
        }
        return getFormatSize(cacheSize.toDouble())
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    fun clearAllCache(context: Context = Engine.context) {
        deleteFile(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteFile(context.externalCacheDir)
        }
    }

    /**
     * 删除文件或文件夹
     */
    fun deleteFile(file: File?): Boolean {
        if (file == null) return false
        if (file.isFile) {
            return file.delete()
        } else {
            val children: Array<String> = file.list() ?: Array(0) { "" }
            for (child in children) {
                val success = deleteFile(File(file, child))
                if (!success) return false
            }
            file.delete()
        }
        return true
    }

    /**
     * 获取文件大小
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */
    fun getFolderSize(file: File?): Long {
        var size = 0L
        if (file != null) {
            if (file.isFile) {
                size = file.length()
            } else {
                val children: Array<String> = file.list() ?: Array(0) { "" }
                for (child in children) {
                    size += getFolderSize(File(file, child))
                }
            }
        }
        return size
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return "0K"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte)
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString().toString() + "K"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte)
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString().toString() + "M"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte)
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString().toString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
            .toString() + "TB"
    }
}