package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

data class OCRInfo<T>(


    @SerializedName("success")
    var success: Boolean? = null,

    @SerializedName("code")
    var code: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: T? = null


)
