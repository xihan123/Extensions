package cn.xihan.lib.okservable.collection

import cn.xihan.lib.okservable.collection.iterator.ObservableMutableIterator
import cn.xihan.lib.okservable.collection.iterator.ObservableMutableIteratorSimple
import cn.xihan.lib.okservable.internal.ifTrue
import cn.xihan.lib.okservable.internal.removeBulk

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/3 1:25
 * @介绍 :
 */
open class ObservableCollectionSimple<C : MutableCollection<E>, E>(protected val base: C, protected val onUpdate: ObservableHandlerSimple) : MutableCollection<E> by base {
    override fun add(element: E): Boolean = base.add(element).ifTrue(onUpdate)

    override fun addAll(elements: Collection<E>): Boolean = base.addAll(elements).ifTrue(onUpdate)

    override fun clear() {
        val previousSize = base.size
        base.clear()
        if (previousSize != base.size)
            onUpdate()
    }

    override fun iterator(): MutableIterator<E> = ObservableMutableIteratorSimple(base.iterator(), onUpdate)

    override fun remove(element: E): Boolean = base.remove(element).ifTrue(onUpdate)

    override fun removeAll(elements: Collection<E>): Boolean = base.removeAll(elements).ifTrue(onUpdate)

    override fun retainAll(elements: Collection<E>): Boolean = base.retainAll(elements).ifTrue(onUpdate)

    override fun equals(other: Any?): Boolean = base == other

    override fun hashCode(): Int = base.hashCode()

    override fun toString(): String = base.toString()
}


abstract class AbstractObservableCollection<C : MutableCollection<E>, E, U : AbstractObservableCollectionHandler<E>>(protected val base: C, protected val onUpdate: U) : MutableCollection<E> by base {
    override fun clear() {
        if(size > 0) {
            val lst = ArrayList(this)
            base.clear()
            onUpdate.onClear(lst)
        }
    }

    override fun equals(other: Any?): Boolean = base == other

    override fun hashCode(): Int = base.hashCode()

    override fun toString(): String = base.toString()
}
open class ObservableCollection<C : MutableCollection<E>, E>(base: C, onUpdate: ObservableCollectionHandler<E>) : AbstractObservableCollection<C, E, ObservableCollectionHandler<E>>(base, onUpdate) {
    override fun add(element: E): Boolean = base.add(element).ifTrue { onUpdate.onAdd(element) }

    override fun addAll(elements: Collection<E>): Boolean {
        var modified = false
        for (el in elements) {
            if (add(el))
                modified = true
        }
        return modified
    }

    override fun iterator(): MutableIterator<E> = ObservableMutableIterator(base.iterator(), onUpdate)

    override fun remove(element: E): Boolean = base.remove(element).ifTrue { onUpdate.onRemove(element) }

    override fun removeAll(elements: Collection<E>): Boolean = removeBulk(elements, true)

    override fun retainAll(elements: Collection<E>): Boolean = removeBulk(elements, false)
}


fun <C : MutableCollection<E>, E> C.observableCollectionSimple(onUpdate: ObservableHandlerSimple): MutableCollection<E> = ObservableCollectionSimple(this, onUpdate)
fun <C : MutableCollection<E>, E> C.observableCollection(onUpdate: ObservableCollectionHandler<E>): MutableCollection<E> = ObservableCollection(this, onUpdate)
inline fun <C : MutableCollection<E>, E> C.observableCollection(crossinline block: ObservableCollectionHandlerScope<E>.() -> Unit): MutableCollection<E> = observableCollection(ObservableCollectionHandlerScope<E>().also(block).createHandler())