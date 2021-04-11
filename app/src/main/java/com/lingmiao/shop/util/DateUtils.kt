package com.lingmiao.shop.util

import java.text.SimpleDateFormat
import java.util.*

/**
Create Date : 2021/4/1111:38 AM
Auther      : Fox
Desc        :
 **/

fun stampToDate(s: Long?): String {
    if(s == null) {
        return "";
    }
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = Date(s * 1000)
    return simpleDateFormat.format(date)
}
