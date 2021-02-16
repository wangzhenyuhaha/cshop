package com.lingmiao.shop.business.wallet.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Author : fox
 * Date   : 2020/8/25
 * Desc   :
 */
data class PageRecordVo<T>(
    @SerializedName("records")
    var `records`: List<T>?,
    @SerializedName("total_count")
    var dataTotal: Int = 0,
    @SerializedName("page_num")
    var pageNo: Int = 0,
    @SerializedName("page_size")
    var pageSize: Int = 0
) : Serializable