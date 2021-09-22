package com.lingmiao.shop.business.wallet.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class DataVO<T>(
    @SerializedName("data")
    var `data`: T?,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false,

    @SerializedName("code")
    var code: String? = null
) : Serializable