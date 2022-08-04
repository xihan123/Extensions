package cn.xihan.lib.okservable.collection

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/3 1:27
 * @介绍 :
 */
open class ObservableSetSimple<C : MutableSet<E>, E>(base: C, onUpdate: ObservableHandlerSimple) : ObservableCollectionSimple<C, E>(base, onUpdate), MutableSet<E>

open class ObservableSet<C : MutableSet<E>, E>(base: C, onUpdate: ObservableCollectionHandler<E>) : ObservableCollection<C, E>(base, onUpdate), MutableSet<E>


fun <C : MutableSet<E>, E> C.observableSetSimple(onUpdate: ObservableHandlerSimple): MutableSet<E> = ObservableSetSimple(this, onUpdate)
fun <C : MutableSet<E>, E> C.observableSet(onUpdate: ObservableCollectionHandler<E>): MutableSet<E> = ObservableSet(this, onUpdate)
inline fun <C : MutableSet<E>, E> C.observableSet(crossinline block: ObservableCollectionHandlerScope<E>.() -> Unit): MutableSet<E> = observableSet(ObservableCollectionHandlerScope<E>().also(block).createHandler())