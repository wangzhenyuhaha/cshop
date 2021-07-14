package com.lingmiao.shop.util

import android.content.Context
import android.view.View
import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.lingmiao.shop.R
import java.text.SimpleDateFormat
import java.util.*

/**
Create Date : 2021/4/1111:38 AM
Auther      : Fox
Desc        :
 **/

const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

const val MINUTES_TIME_FORMAT = "yyyy-MM-dd HH:mm"

const val DATE_FORMAT = "yyyy-MM-dd"

const val DATE_FORMAT_OTHER = "yyyy/MM/dd"

const val MINUTES_TIME_FORMAT_OTHER = "yyyy/MM/dd HH:mm"

const val DATE_TIME_FORMAT_OTHER = "yyyy/MM/dd HH:mm:ss"

fun stampToDate(s: Long?, formatType: String=DATE_TIME_FORMAT): String {
    if(s == null) {
        return "";
    }
    val simpleDateFormat = SimpleDateFormat(formatType)
    val date = Date(s * 1000)
    return simpleDateFormat.format(date)
}

fun stampToDate(s: Long?): String {
    return stampToDate(s, DATE_TIME_FORMAT);
}

fun longToDate(s: Long? ): String {
    return longToDate(s, DATE_FORMAT);
}

fun longToDate(s: Long?, formatType: String=DATE_FORMAT): String {
    if(s == null) {
        return "";
    }
    val simpleDateFormat = SimpleDateFormat(formatType)
    val date = Date(s)
    return simpleDateFormat.format(date)
}

// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
// HH时mm分ss秒，
// strTime的时间格式必须要与formatType的时间格式相同
fun stringToDate(strTime: String?, formatType: String?): Date? {
    val formatter = SimpleDateFormat(formatType)
    return formatter.parse(strTime)
}

fun date2Date(strTime: String?) : Date? {
    return stringToDate(strTime, DATE_FORMAT);
}

fun dateTime2Date(strTime: String?) : Date? {
    return stringToDate(strTime, DATE_TIME_FORMAT);
}

fun formatDate(date: Date?) : String?{
    return formatString(date, DATE_FORMAT);
}

fun formatDateTime(date: Date?) : String?{
    return formatString(date, DATE_TIME_FORMAT);
}

fun formatString(date: Date?, formatType: String?) : String?{
    val format = SimpleDateFormat(formatType)//
    return format.format(date)
}

fun getDefaultDatePicker(context: Context, selectedDate: Calendar, start: Calendar, end: Calendar, click :((Date?, View?) -> Unit)?, confirm : ((View?) -> Unit)?, cancel : ((View?) -> Unit)?) : TimePickerView {
    return TimePickerBuilder(context, click)
        .setLayoutRes(
            R.layout.goods_view_time
        ) { v ->
            val tvSubmit: TextView = v.findViewById<View>(R.id.tv_finish) as TextView
            val ivCancel: TextView = v.findViewById<View>(R.id.iv_cancel) as TextView
            tvSubmit.setOnClickListener {
                confirm?.invoke(it);
            }
            ivCancel.setOnClickListener { cancel?.invoke(it) }
        }
        .setDate(selectedDate)
        .setRangDate(start, end)
        .setOutSideCancelable(true)
        .setContentTextSize(16)
        .setType(booleanArrayOf(true, true, true, false, false, false))
        .setLabel("年", "月", "日", "时", "分", "秒")
        .setLineSpacingMultiplier(2.2f)
        .setTextXOffset(0, 0, 0, 40, 0, -40)
        //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        .isCenterLabel(false)
        .setDividerColor(-0x666666)
        .build()
}


fun getDatePicker(context: Context, selectedDate: Calendar, start: Calendar, end: Calendar, click :((Date?, View?) -> Unit)?, confirm : ((View?) -> Unit)?, cancel : ((View?) -> Unit)?) : TimePickerView {
    return TimePickerBuilder(context, click)
        .setLayoutRes(
            R.layout.goods_view_time
        ) { v ->
            val tvSubmit: TextView = v.findViewById<View>(R.id.tv_finish) as TextView
            val ivCancel: TextView = v.findViewById<View>(R.id.iv_cancel) as TextView
            tvSubmit.setOnClickListener {
                confirm?.invoke(it);
            }
            ivCancel.setOnClickListener { cancel?.invoke(it) }
        }
        .setDate(selectedDate)
        .setRangDate(start, end)
        .setOutSideCancelable(true)
        .setContentTextSize(16)
        .setType(booleanArrayOf(true, true, true, false, false, false))
        .setLabel("年", "月", "日", "时", "分", "秒")
        .setLineSpacingMultiplier(2.2f)
        .setTextXOffset(0, 0, 0, 40, 0, -40)
        //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        .isCenterLabel(false)
        .setDividerColor(-0x666666)
        .build()
}



fun getDatePicker(context: Context, selectedDate: Calendar, start: Calendar, end: Calendar, type : Int, click :((Date?, View?) -> Unit)?, confirm : ((View?) -> Unit)?, cancel : ((View?) -> Unit)?) : TimePickerView {
    var values = booleanArrayOf(true, true, true, false, false, false)
    if(type == 1) {
        values = booleanArrayOf(true, false, false, false, false, false)
    } else if(type == 2) {
        values = booleanArrayOf(true, true, false, false, false, false)
    }
    return TimePickerBuilder(context, click)
        .setLayoutRes(
            R.layout.goods_view_time
        ) { v ->
            val tvSubmit: TextView = v.findViewById<View>(R.id.tv_finish) as TextView
            val ivCancel: TextView = v.findViewById<View>(R.id.iv_cancel) as TextView
            tvSubmit.setOnClickListener {
                confirm?.invoke(it);
            }
            ivCancel.setOnClickListener { cancel?.invoke(it) }
        }
        .setDate(selectedDate)
        .setRangDate(start, end)
        .setOutSideCancelable(true)
        .setContentTextSize(16)
        .setType(values)
        .setLabel("年", "月", "日", "时", "分", "秒")
        .setLineSpacingMultiplier(2.2f)
        .setTextXOffset(0, 0, 0, 40, 0, -40)
        //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        .isCenterLabel(false)
        .setDividerColor(-0x666666)
        .build()
}

fun getDefaultTimePicker(context: Context, selectedDate: Calendar, start: Calendar, end: Calendar, click :((Date?, View?) -> Unit)?, confirm : ((View?) -> Unit)?, cancel : ((View?) -> Unit)?) : TimePickerView {
    return TimePickerBuilder(context, click)
        .setLayoutRes(
            R.layout.goods_view_time
        ) { v ->
            val tvSubmit: TextView = v.findViewById<View>(R.id.tv_finish) as TextView
            val ivCancel: TextView = v.findViewById<View>(R.id.iv_cancel) as TextView
            tvSubmit.setOnClickListener {
                confirm?.invoke(it);
            }
            ivCancel.setOnClickListener { cancel?.invoke(it) }
        }
        .setDate(selectedDate)
        .setRangDate(start, end)
        .setOutSideCancelable(true)
        .setContentTextSize(16)
        .setType(booleanArrayOf(true, true, true, true, true, false))
        .setLabel("年", "月", "日", "时", "分", "秒")
        .setLineSpacingMultiplier(2.2f)
        .setTextXOffset(0, 0, 0, 40, 0, -40)
        //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        .isCenterLabel(false)
        .setDividerColor(-0x666666)
        .build()
}