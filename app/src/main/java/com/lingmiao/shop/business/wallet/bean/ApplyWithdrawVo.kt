package com.lingmiao.shop.business.wallet.bean
import com.google.gson.annotations.SerializedName


data class ApplyWithdrawVo(
    @SerializedName("account_id")
    var accountId: String? = "",
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("success")
    var success: Boolean? = false
)