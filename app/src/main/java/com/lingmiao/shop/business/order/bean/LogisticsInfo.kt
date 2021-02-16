package com.lingmiao.shop.business.order.bean

import com.google.gson.annotations.SerializedName


data class LogisticsInfo(
    @SerializedName("courier_num")
    var courierNum: String?,
    @SerializedName("data")
    var `data`: List<LogisticsInfoData>?,
    @SerializedName("name")
    var name: String?
)

data class LogisticsInfoData(
    @SerializedName("time")
    var time: String?,
    @SerializedName("context")
    var info: String?,
    @SerializedName("sign")
    var sign: Boolean = false,
    var position:String = "middle"
)