package com.lingmiao.shop.sdk
import com.google.gson.annotations.SerializedName

//自己定义
data class JpushExtra(
    @SerializedName("type")
    var type: String? = null  //1申请开店审核成功;  2买家订单支付成功;  3:其它
)