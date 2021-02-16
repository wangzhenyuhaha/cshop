package com.lingmiao.shop.business.main.bean
import com.google.gson.annotations.SerializedName


data class ShopStatus(
    @SerializedName("shop_staus")
    var shopStatus: String?,
    @SerializedName("status_reason")
    var statusReason: String?
)