package com.james.common.utils.exts

import android.view.View
import android.widget.EditText
import android.widget.TextView

/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 操作View的扩展
 */
inline fun View.gone() {
    visibility = View.GONE
}

inline fun View.visiable() {
    visibility = View.VISIBLE
}

inline fun View.show(isShow: Boolean) {
    visibility = if (isShow) View.VISIBLE else View.GONE
}

inline fun View.invisiable() {
    visibility = View.INVISIBLE
}

inline fun TextView.getViewText(): String {
    return this.text.toString().trim()
}

inline fun EditText.getViewText(): String {
    return this.text.toString().trim()
}


// --------------------- View 防多次点击 ---------------------

fun <T : View> T.singleClick(time: Long = 800, block: (T) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        val duration = currentTime - triggerDelay
        triggerDelay = currentTime
        if (duration >= time) {
            block(it as T)
        }
    }
}

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else 800
    set(value) {
        setTag(1123461123, value)
    }