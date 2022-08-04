package cn.xihan.lib.okservable.collection.iterator

import cn.xihan.lib.okservable.collection.ObservableCollectionHandler
import cn.xihan.lib.okservable.collection.ObservableHandlerSimple

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/3 1:24
 * @介绍 :
 */
class ObservableMutableIteratorSimple<T>(private val base: MutableIterator<T>, private val onUpdate: ObservableHandlerSimple) : MutableIterator<T> by base {
    override fun remove() {
        base.remove()
        onUpdate()
    }
}

class ObservableMutableIterator<T>(private val base: MutableIterator<T>, private val onUpdate: ObservableCollectionHandler<T>) : MutableIterator<T> by base {
    private var lastElement: T? = null

    override fun next(): T = base.next().also { lastElement = it }

    override fun remove() {
        base.remove()
        onUpdate.onRemove(lastElement as T)
    }
}