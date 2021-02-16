package com.lingmiao.shop.business.wallet.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PageListVo<T>(
    @SerializedName("data")
    var `data`: List<T>?,
    @SerializedName("data_total")
    var dataTotal: Int = 0,
    @SerializedName("page_no")
    var pageNo: Int = 0,
    @SerializedName("page_size")
    var pageSize: Int = 0
) : Serializable