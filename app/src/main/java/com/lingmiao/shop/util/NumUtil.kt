package com.lingmiao.shop.util

import java.lang.Exception
import java.text.DecimalFormat

/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 数量转换
 */
fun formatDouble(num: Double?): String {
    val decimalFormat = DecimalFormat("0.00")
    return decimalFormat.format(num ?: 0)
}

fun String?.parseString(): Int {
    try {
        return Integer.parseInt(this!!)
    } catch (e: Exception) {
        return 0
    }
}

fun toInt(str: String) : Int {
    return str.toInt();
}
