package com.guc.kframe.utils

import java.lang.reflect.ParameterizedType

/**
 * Created by guc on 2020/5/22.
 * 描述：工具方法
 */
/**
 * 求最大值
 */
fun <T : Comparable<T>> max(vararg nums: T): T {
    if (nums.isEmpty()) throw RuntimeException("Params can not be empty.")
    var maxNum = nums[0]
    for (num in nums) {
        if (num > maxNum) maxNum = num
    }
    return maxNum
}

/**
 * 获取泛型类型
 *
 * @param genType class
 * @return type
 */
fun getParameterizedType(genType: Class<*>?): ParameterizedType? {
    var genType: Class<*>? = genType ?: return null
    return if (genType != null) {
        if (genType.genericSuperclass is ParameterizedType) {
            genType.genericSuperclass as ParameterizedType?
        } else if (genType.superclass
                .also { genType = it } != null && genType != Any::class.java
        ) {
            getParameterizedType(genType)
        } else {
            null
        }
    } else {
        null
    }

}