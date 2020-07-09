package com.guc.kotlinframe

import com.guc.kframe.utils.CacheManagerUtils
import com.guc.kframe.utils.hashMap
import org.junit.Test
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testHashMap() {
        val map = hashMap {
            this["guc"] = "你好"
            this["ok"] = "确定"
        }
        println(map)
    }

    @Test
    fun testCacheUtil() {
        val cache = CacheManagerUtils.getFolderSize(File("G:\\study"))
        println(cache)
        println(CacheManagerUtils.getFormatSize(cache.toDouble()))
    }
}
