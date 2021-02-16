package com.lingmiao.shop.business.common.bean
import com.google.gson.annotations.SerializedName


/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   :
 */
data class PageVO<T>(
    @SerializedName("data")
    var `data`: List<T>?,
    @SerializedName("data_total")
    var dataTotal: Int = 0,
    @SerializedName("page_no")
    var pageNo: Int = 0,
    @SerializedName("page_size")
    var pageSize: Int = 0
)