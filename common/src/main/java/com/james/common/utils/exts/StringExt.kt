package com.james.common.utils.exts

/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 字符串扩展函数
 */
/**
 * 网络地址
 */
fun String?.isNetUrl(): Boolean {
    return this != null && (this.startsWith("http", true)
            || this.startsWith("https", true))
}