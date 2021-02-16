package com.james.common.utils.exts


/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 判空操作
 */
inline fun <T> Collection<T>?.isNotEmpty(): Boolean {
    return this != null && !this.isEmpty()
}

/**
 * 删除集合内第一个符合条件的元素
 */
fun <T> MutableCollection<T>?.removeIf1(condition: (T) -> Boolean): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val iterator = iterator()
    while (iterator.hasNext()) {
        if (condition(iterator.next())) {
            iterator.remove()
            return true
        }
    }
    return false
}

/**
 * 删除集合内所有符合条件的元素
 */
fun <T> MutableCollection<T>?.removeAllIf(condition: (T) -> Boolean): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    var removed = false
    val iterator = iterator()
    while (iterator.hasNext()) {
        if (condition(iterator.next())) {
            iterator.remove()
            removed = true
        }
    }
    return removed
}
