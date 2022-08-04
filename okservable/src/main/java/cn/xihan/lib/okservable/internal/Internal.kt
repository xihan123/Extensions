package cn.xihan.lib.okservable.internal

/**
 * @项目名 : Extensions
 * @作者 : MissYang
 * @创建时间 : 2022/8/3 1:21
 * @介绍 :
 */
internal inline fun Boolean.ifTrue(block: () -> Unit): Boolean = also { if (it) block() }

internal fun <C : MutableCollection<E>, E> C.removeBulk(elements: Collection<E>, remove: Boolean): Boolean {
    val it = iterator()
    var modified = false
    for (el in it)
        if (el in elements == remove) {
            it.remove()
            modified = true
        }
    return modified
}

internal fun <C : MutableCollection<E>, E> C.removeSingle(element: E): Boolean {
    val it = iterator()
    for (el in it)
        if (el == element) {
            it.remove()
            return true
        }
    return false
}

internal fun <C : MutableList<E>, E> C.removeBulkRandomAccess(elements: Collection<E>, remove: Boolean): Boolean {
    var modified = false
    var i = 0
    while(i < size) {
        if (get(i) in elements == remove) {
            removeAt(i)
            modified = true
        }
        else
            i++
    }
    return modified
}

internal fun <C : MutableList<E>, E> C.removeSingleRandomAccess(element: E): Boolean {
    var i = 0
    while(i < size) {
        if (get(i) == element) {
            removeAt(i)
            return true
        }
        i++
    }
    return false
}