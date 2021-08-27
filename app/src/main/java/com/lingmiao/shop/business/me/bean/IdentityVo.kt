package com.lingmiao.shop.business.me.bean

import android.util.Log
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.util.dateTime2Date
import java.io.Serializable


/**
Create Date : 2021/4/291:28 PM
Auther      : Fox
Desc        :
 **/
data class IdentityVo(
    @SerializedName("auto_renew")
    var autoRenew: Int? = 0,
    @SerializedName("create_time")
    var createTime: String? = "",
    @SerializedName("due_date")
    var dueDate: String? = "",
    @SerializedName("expire_day")
    var expireDay: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("remarks")
    var remarks: Any? = Any(),
    @SerializedName("shop_id")
    var shopId: Int? = 0,
    @SerializedName("shop_level")
    var shopLevel: Int? = 0,
    @SerializedName("shop_title")
    var shopTitle: String? = ""
) : Serializable {

    fun get_DueDateStr(): String {
        return dueDate?.substring(0, 10).toString();
    }

    fun get_DueHint(): Int {

        val end = (dateTime2Date(dueDate)?.time ?: 0)
        val start = (dateTime2Date(TimeUtils.getNowString())?.time ?: 0)
        val time = (end - start) / (60 * 60 * 24 * 1000)



        return TimeUtils.getTimeSpan(dueDate, TimeUtils.getNowString(), TimeConstants.DAY).toInt();
    }

    fun get_VipHint(): String {
        return String.format("（有效期至%s）", get_DueDateStr());
    }

    fun get_CommonHint(): String {
        return String.format("（剩余时间%s天）", get_DueHint());
    }

    // 正式门店（有效期至2022-02-26）
    // 1试用
    // 2正式
    fun isVip(): Boolean {
        return shopLevel == 2;
    }


}
