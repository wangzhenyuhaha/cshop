package com.lingmiao.shop.util

import com.blankj.utilcode.util.TimeUtils
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

// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
// HH时mm分ss秒，
// strTime的时间格式必须要与formatType的时间格式相同
fun stringToDate(strTime: String?, formatType: String?): Date? {
    val formatter = SimpleDateFormat(formatType)
    var date: Date? = null
    date = formatter.parse(strTime)
    return date
}
