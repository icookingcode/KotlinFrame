package com.guc.kotlinframe.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by guc on 2020/6/4.
 * 描述：
 */
object ServiceCreator {
    private const val BASE_URL = "http://192.168.181.128:80/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(service: Class<T>): T = retrofit.create(service)
    inline fun <reified T> create(): T = create(T::class.java)
}