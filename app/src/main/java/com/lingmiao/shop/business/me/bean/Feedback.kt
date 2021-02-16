package com.lingmiao.shop.business.me.bean
import com.google.gson.annotations.SerializedName


data class Feedback(
    @SerializedName("advice_type")
    var adviceType: Int?,
    @SerializedName("content")
    var content: String?
)