package cn.xihan.lib.okservable.collection.iterator

import cn.xihan.lib.okservable.collection.ObservableHandlerSimple
import cn.xihan.lib.okservable.collection.ObservableListHandler
import kotlin.math.max

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/3 1:25
 * @介绍 :
 */
class ObservableMutableListIteratorSimple<T>(
    private val base: MutableListIterator<T>,
    private val onUpdate: ObservableHandlerSimple
) : MutableListIterator<T> by base {
    private var lastElement: T? = null

    override fun next(): T = base.next().also { lastElement = it }

    override fun previous(): T = base.previous().also { lastElement = it }

    override fun add(element: T) {
        base.add(element)
        onUpdate()
    }

    override fun remove() {
        base.remove()
        onUpdate()
    }

    override fun set(element: T) {
        base.set(element)
        if (lastElement != element)
            onUpdate()
    }
}

class ObservableMutableListIterator<T>(
    private val base: MutableListIterator<T>,
    private val onUpdate: ObservableListHandler<T>
) : MutableListIterator<T> by base {
    private var lastElement: T? = null
    private var lastElementIndex: Int = -1

    override fun next(): T {
        if (hasNext())
            lastElementIndex = nextIndex()
        return base.next().also {
            lastElement = it
        }
    }

    override fun previous(): T {
        if (hasPrevious())
            lastElementIndex = previousIndex()
        return base.previous().also {
            lastElement = it
        }
    }

    override fun add(element: T) {
        base.add(element)
        onUpdate.onAdd(max(0, lastElementIndex), element)
    }

    override fun remove() {
        base.remove()
        onUpdate.onRemove(lastElementIndex, lastElement as T)
    }

    override fun set(element: T) {
        base.set(element)
        if (lastElement != element)
            onUpdate.onSet(lastElementIndex, lastElement as T, element)
    }
}