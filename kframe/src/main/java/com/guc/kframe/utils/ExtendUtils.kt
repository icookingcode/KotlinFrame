package com.guc.kframe.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.fragment.app.Fragment

/**
 * Created by guc on 2020/5/6.
 * 描述：扩展工具类
 */
//KTX也提供了该方法
fun cvOf(vararg pairs: Pair<String, Any?>) = ContentValues().apply {
    for (pair in pairs) {
        val key = pair.first
        when (val value = pair.second) {
            is Int -> put(key, value)
            is Long -> put(key, value)
            is Short -> put(key, value)
            is Float -> put(key, value)
            is Double -> put(key, value)
            is Boolean -> put(key, value)
            is String -> put(key, value)
            is Byte -> put(key, value)
            is ByteArray -> put(key, value)
            null -> putNull(key)
        }
    }
}

fun SharedPreferences.open(block: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.block()
    editor.apply()
}

fun SharedPreferences.Editor.put(key: String, value: Any): SharedPreferences.Editor {
    when (value) {
        is String -> putString(key, value)
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
    }
    return this
}

//泛型实化
inline fun <reified T> quickStartActivity(
    context: Context,
    block: Intent.() -> Unit
) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}

//泛型实化
inline fun <reified T> Activity.quickStartActivityForResult(
    requestCode: Int = 1024,
    block: Intent.() -> Unit
) {
    val intent = Intent(this, T::class.java)
    intent.block()
    this.startActivityForResult(intent, requestCode)
}

//泛型实化
inline fun <reified T> Fragment.quickStartActivityForResult(
    requestCode: Int = 1024,
    block: Intent.() -> Unit
) {
    val intent = Intent(this.context, T::class.java)
    intent.block()
    this.startActivityForResult(intent, requestCode)
}

/**
 * 给EditText设置输入改变监听
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
