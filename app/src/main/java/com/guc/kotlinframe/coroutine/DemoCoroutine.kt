package com.guc.kotlinframe.coroutine

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by guc on 2020/7/9.
 * Description：Kotlin 协程使用
 */
private const val TAG = "coroutine"
fun main() {
    println("$TAG main-thread-start")
    GlobalScope.launch {//不阻塞当前线程，一般不用
        println("$TAG GlobalScope-start")
        delay(2000)
        println("$TAG GlobalScope-end")
    }
    Thread.sleep(1000)
    println("$TAG main-thread-end")

    runBlocking {//阻塞线程
        println("$TAG runBlocking-start")
        printDot()
        println("$TAG runBlocking-end")
    }

    runBlocking {
        val start = System.currentTimeMillis()
        val resut1 = async {
            delay(1000)
            2
        }
        val resut2 = async { // 立即执行
            delay(1000)
            8
        }
        println("result is ${resut1.await() * resut2.await()}") //await() 等待执行结果，若已执行完，立即返回结果
        val end = System.currentTimeMillis()
        println("cost ${end - start}")
        println("result is ${resut1.await() * resut2.await()}")
        val end2 = System.currentTimeMillis()
        println("cost ${end2 - end}")
    }

    val job = Job()
    val scope = CoroutineScope(job)
    scope.launch {//不阻塞线程
        val result = withContext(Dispatchers.Default) {//基本等价于async{}.await()
            println("----------withContext-----------")
            delay(1000)
            5 + 5
        }
        println("result $result")
    }
    scope.launch {//不阻塞线程
        val result = withContext(Dispatchers.Default) {//基本等价于async{}.await()
            println("----------withContext-----------")
            delay(1000)
            5 + 6
        }
        println("result $result")
    }
    Thread.sleep(2000)
    println("----------suspendCoroutine-----------")
    scope.launch {
        try {
            val result = request(10)
            println(result)
        } catch (e: Exception) {
            println(e.message)
        }
    }
    scope.launch {
        try {
            val result = request(10)
            println(result)
        } catch (e: Exception) {
            println(e.message)
        }
    }
    Thread.sleep(2000)
    job.cancel()

}

suspend fun printDot() = coroutineScope {//只阻塞当前协程
    launch {
        for (i in 1..3) {
            println(".")
            delay(1000)
        }
    }
}

suspend fun request(param: Int): String {
    return suspendCoroutine { c -> //线程中执行
        val num = (1..20).random()
        if (param > num) {
            c.resume("成功 $param > $num") //回调协程
        } else {
            c.resumeWithException(Exception("失败$param <= $num"))
        }
    }
}