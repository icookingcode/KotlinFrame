package com.guc.kframe.widget.banner

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by guc on 2020/6/11.
 * Description：Weak Reference
 */
class WeakHandler(callback: Handler.Callback? = null) {
    var exec: ExecHandler = if (callback == null) ExecHandler()
    else ExecHandler(callback = WeakReference(callback))
    private val lock = ReentrantLock()
    val mRunnables: ChainedRef = ChainedRef(lock, Runnable { })

    constructor(looper: Looper) : this() {
        exec = ExecHandler(looper = looper)
    }

    constructor(looper: Looper, callback: Handler.Callback) : this(looper) {
        exec = ExecHandler(WeakReference(callback), looper)
    }

    fun post(r: Runnable): Boolean = exec.post(wrapRunnable(r))


    private fun wrapRunnable(r: Runnable): WeakRunnable {
        val hardRef = ChainedRef(lock, r)
        mRunnables.insertAfter(hardRef)
        return hardRef.wrapper
    }

    fun postAtTime(r: Runnable, uptimeMillis: Long): Boolean {
        return exec.postAtTime(wrapRunnable(r), uptimeMillis)
    }

    fun postAtTime(r: Runnable, token: Any, uptimeMillis: Long): Boolean {
        return exec.postAtTime(wrapRunnable(r), token, uptimeMillis)
    }

    fun postDelayed(r: Runnable, delayMillis: Long): Boolean {
        return exec.postDelayed(wrapRunnable(r), delayMillis)
    }

    fun postAtFrontOfQueue(r: Runnable): Boolean {
        return exec.postAtFrontOfQueue(wrapRunnable(r))
    }

    fun removeCallbacks(r: Runnable) {
        val runnable = mRunnables.remove(r)
        if (runnable != null) {
            exec.removeCallbacks(runnable)
        }
    }

    fun removeCallbacks(r: Runnable, token: Any) {
        val runnable = mRunnables.remove(r)
        if (runnable != null) {
            exec.removeCallbacks(runnable, token)
        }
    }

    fun sendMessage(msg: Message): Boolean {
        return exec.sendMessage(msg)
    }

    fun sendEmptyMessage(what: Int): Boolean {
        return exec.sendEmptyMessage(what)
    }

    fun sendEmptyMessageDelayed(what: Int, delayMillis: Long): Boolean {
        return exec.sendEmptyMessageDelayed(what, delayMillis)
    }

    fun sendEmptyMessageAtTime(what: Int, uptimeMillis: Long): Boolean {
        return exec.sendEmptyMessageAtTime(what, uptimeMillis)
    }

    fun sendMessageDelayed(msg: Message, delayMillis: Long): Boolean {
        return exec.sendMessageDelayed(msg, delayMillis)
    }

    fun sendMessageAtTime(
        msg: Message,
        uptimeMillis: Long
    ): Boolean {
        return exec.sendMessageAtTime(msg, uptimeMillis)
    }

    fun sendMessageAtFrontOfQueue(msg: Message): Boolean {
        return exec.sendMessageAtFrontOfQueue(msg)
    }

    fun removeMessages(what: Int) {
        exec.removeMessages(what)
    }

    fun removeMessages(what: Int, obj: Any) {
        exec.removeMessages(what, obj)
    }

    fun removeCallbacksAndMessages(token: Any) {
        exec.removeCallbacksAndMessages(token)
    }

    fun hasMessages(what: Int): Boolean {
        return exec.hasMessages(what)
    }

    fun hasMessages(what: Int, obj: Any): Boolean {
        return exec.hasMessages(what, obj)
    }

    fun getLooper() = exec.looper

}

class ExecHandler(
    val callback: WeakReference<Callback>? = null,
    looper: Looper = Looper.myLooper()!!
) : Handler(looper) {
    override fun handleMessage(msg: Message) {
        val cb: Callback? = callback?.get()
        cb?.handleMessage(msg)
    }
}

class WeakRunnable(
    private val delegate: WeakReference<Runnable>,
    private val reference: WeakReference<ChainedRef>
) :
    Runnable {

    override fun run() {
        val delegate: Runnable? = delegate.get()
        val reference: ChainedRef? = reference.get()
        reference?.remove()
        delegate?.run()
    }
}

class ChainedRef(
    private val lock: Lock,
    private val runnable: Runnable
) {
    var next: ChainedRef? = null

    var prev: ChainedRef? = null

    val wrapper: WeakRunnable = WeakRunnable(WeakReference(runnable), WeakReference(this))

    fun remove(): WeakRunnable {
        lock.lock()
        try {
            if (prev != null) {
                prev!!.next = next
            }
            if (next != null) {
                next!!.prev = prev
            }
            prev = null
            next = null
        } finally {
            lock.unlock()
        }
        return wrapper
    }

    fun insertAfter(candidate: ChainedRef) {
        lock.lock()
        try {
            if (next != null) {
                next!!.prev = candidate
            }
            candidate.next = next
            next = candidate
            candidate.prev = this
        } finally {
            lock.unlock()
        }
    }

    fun remove(obj: Runnable): WeakRunnable? {
        lock.lock()
        try {
            var curr = next // Skipping head
            while (curr != null) {
                if (curr.runnable === obj) { // We do comparison exactly how Handler does inside
                    return curr.remove()
                }
                curr = curr.next
            }
        } finally {
            lock.unlock()
        }
        return null
    }
}