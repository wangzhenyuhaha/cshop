package com.lingmiao.shop.business.me.bean
import com.google.gson.annotations.SerializedName


data class Feedback(
    @SerializedName("feedback_type")
    var feedback_type: Int?,
    @SerializedName("feedback_info")
    var feedback_info: String?,
    @SerializedName("feedback_img")
    var feedback_img: String?
)