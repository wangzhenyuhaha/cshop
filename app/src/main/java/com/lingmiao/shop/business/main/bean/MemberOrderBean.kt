package com.lingmiao.shop.business.main.bean


import com.google.gson.annotations.SerializedName

data class MemberOrderBean(
    @SerializedName("month_order_num")
    var monthOrderNum: Int? = 0,
    @SerializedName("month_order_price")
    var monthOrderPrice: Double? = 0.0,
    @SerializedName("total_order_num")
    var totalOrderNum: Int? = 0,
    @SerializedName("total_order_price")
    var totalOrderPrice: Double? = 0.0
)