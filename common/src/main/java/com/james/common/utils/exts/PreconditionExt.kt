package com.james.common.utils.exts

import android.widget.TextView
import com.james.common.exception.BizException

/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 条件校验
 */
inline fun checkBoolean(value: Boolean, lazyMessage: () -> String) {
    if (!value) {
        val message = lazyMessage()
        throw BizException(message)
    }
}

inline fun  checkNotBlack(value: String?, lazyMessage: () -> String): String {
    if (value.isNullOrBlank()) {
        val message = lazyMessage()
        throw BizException(message)
    } else {
        return value
    }
}

inline fun  checkNotBlack(textView: TextView, lazyMessage: () -> String): String {
    val value = textView.text.toString().trim()
    if (value.isBlank()) {
        val message = lazyMessage()
        throw BizException(message)
    } else {
        return value
    }
}

inline fun <T : Any> checkNotNull(value: T?, lazyMessage: () -> String): T {
    if (value == null) {
        val message = lazyMessage()
        throw BizException(message)
    } else {
        return value
    }
}

fun String?.check(): String {
    return this ?: ""
}

fun String?.check(default: String): String {
    return this ?: default
}

fun String?.checkNotBlank(default: String): String {
    return if (this.isNotBlank()) this!! else default
}

fun String?.isNotBlank(): Boolean {
    return this != null && this.isNotEmpty()
}

