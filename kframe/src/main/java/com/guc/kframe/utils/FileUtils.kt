package com.guc.kframe.utils

import android.content.Context
import android.content.res.AssetManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.guc.kframe.Engine
import com.guc.kframe.R
import java.io.*

/**
 * Created by guc on 2020/5/6.
 * 描述：文件操作工具
 */
object FileUtils {
    /**
     * 保存字符串到文字
     * 文件保存到 /data/data/<package name>/files/目录下
     */
    fun writeStr2AppFile(
        inputText: String,
        fileName: String,
        context: Context = Engine.context,
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return try {
            val output = context.openFileOutput(fileName, mode)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {//kotlin内置的扩展函数，保证lambda表达式中代码执行完毕后自动关闭外层的数据流
                it.write(inputText)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 将信息写入文件
     *
     * @param filePath 文件路径
     * @param content  待保存内容
     * @param append   是否追加
     * @return true：保存成功  false:保存失败
     */
    fun writeStr2File(
        filePath: String,
        content: String,
        append: Boolean
    ): Boolean {
        val f = File(filePath)
        if (!f.exists()) {
            f.parentFile?.mkdirs()
            f.createNewFile()
        }
        return try {
            val output = FileOutputStream(filePath, append)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {//kotlin内置的扩展函数，保证lambda表达式中代码执行完毕后自动关闭外层的数据流
                it.write(content)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 读取文件中内容
     * /data/data/<package name>/files/
     * @param fileName 文件名
     */
    fun readAppFile2String(fileName: String, context: Context = Engine.context): String {
        return try {
            val content = StringBuilder()
            val output = context.openFileInput(fileName)
            val reader = BufferedReader(InputStreamReader(output))
            reader.use {//kotlin内置的扩展函数，保证lambda表达式中代码执行完毕后自动关闭外层的数据流
                reader.forEachLine {
                    content.append(it)
                }
            }
            content.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            "IOException"
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            "FileNotFoundException"
        }
    }

    /**
     * 读取文件中内容
     */
    fun readFile2String(file: String, context: Context = Engine.context): String {
        return try {
            val content = StringBuilder()
            val output = FileInputStream(file)
            val reader = BufferedReader(InputStreamReader(output))
            reader.use {//kotlin内置的扩展函数，保证lambda表达式中代码执行完毕后自动关闭外层的数据流
                reader.forEachLine {
                    content.append(it)
                }
            }
            content.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            "IOException"
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            "FileNotFoundException"
        }
    }

    /**
     * 读取Assets下文件
     */
    fun readAssets2String(assetManager: AssetManager, fileName: String): String {
        return try {
            val content = java.lang.StringBuilder()
            val inputStream = assetManager.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.use {//kotlin内置的扩展函数，保证lambda表达式中代码执行完毕后自动关闭外层的数据流
                reader.forEachLine {
                    content.append(it)
                }
            }
            content.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            "FileNotFoundException"
        }
    }

    /**
     * 读取Assets下文件
     */
    fun readRaw2String(context: Context = Engine.context, rawResId: Int): String {
        return try {
            val content = java.lang.StringBuilder()
            val inputStream = context.resources.openRawResource(rawResId)
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.use {//kotlin内置的扩展函数，保证lambda表达式中代码执行完毕后自动关闭外层的数据流
                reader.forEachLine {
                    content.append(it)
                }
            }
            content.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            "FileNotFoundException"
        }
    }

    /**
     * 读取Assets下文件
     */
    fun readRaw2String(context: Context = Engine.context, rawName: String): String {
        return try {
            val content = java.lang.StringBuilder()
            val field = R.raw::class.java.getField(rawName)
            val inputStream = context.resources.openRawResource(field.getInt(null))
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.use {//kotlin内置的扩展函数，保证lambda表达式中代码执行完毕后自动关闭外层的数据流
                reader.forEachLine {
                    content.append(it)
                }
            }
            content.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            "FileNotFoundException"
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
            "NoSuchFieldException"
        }
    }

    fun getUriForFile(file: File): Uri? {
        return if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(
                Engine.context,
                Engine.context.packageName + ".fileProvider", file
            )
        } else {
            Uri.fromFile(file)
        }
    }

    /**
     * @param file 文件或文件夹
     * @param isDir 修复如果是文件夹时，文件夹创建失败问题
     */
    fun createFile(file: File, isDir: Boolean) {
        if (file.isDirectory || isDir) {
            file.mkdirs()
        } else {
            file.parentFile?.mkdirs()
            if (!file.exists()) {
                file.createNewFile()
            }
        }
    }
}