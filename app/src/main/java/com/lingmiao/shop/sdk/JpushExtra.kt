package com.lingmiao.shop.sdk

import com.google.gson.annotations.SerializedName

//自己定义
data class JpushExtra(
    //1申请开店审核成功;  2买家订单支付成功;  3:其它
    @SerializedName("type")
    var type: String? = null,
    //订单编号
    @SerializedName("order_sn")
    var orderSN: String? = null
)