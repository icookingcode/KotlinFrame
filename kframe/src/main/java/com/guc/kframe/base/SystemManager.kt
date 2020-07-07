package com.guc.kframe.base

/**
 * Created by guc on 2020/5/28.
 * 描述：管理
 */
object SystemManager {

    private val mSystemPool = HashMap<String, BaseSystem>()

    fun <T> destroySystem(clazz: Class<T>) {
        mSystemPool[clazz.name]?.destroySystem()
        mSystemPool.remove(clazz.name)
    }

    fun destroyAllSystem() {
        val iterator: Iterator<Map.Entry<String, BaseSystem>> =
            mSystemPool.entries.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            next.value.destroySystem()
        }
        mSystemPool.clear()
    }

    inline fun <reified T : BaseSystem> getSystem() = getSystem(T::class.java)

    fun <T : BaseSystem> getSystem(className: Class<T>?): T? {
        if (className == null) {
            return null
        }
        var instance = mSystemPool[className.name] as T?
        if (instance == null) {
            try {
                instance = className.newInstance()
                instance!!.createSystem()
                mSystemPool[className.name] = instance
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
        return instance
    }
}