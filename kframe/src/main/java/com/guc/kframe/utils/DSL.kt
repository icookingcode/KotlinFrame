package com.guc.kframe.utils

/**
 * Created by guc on 2020/6/4.
 * 描述：DSL 定义特有的语法结构
 */
inline fun hashMap(block: HashMap<String, String>.() -> Unit): HashMap<String, String> {
    val map = HashMap<String, String>()
    map.block()
    return map
}