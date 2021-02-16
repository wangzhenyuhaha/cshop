package com.lingmiao.shop.business.wallet.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AccountVo(
    @SerializedName("account_type")
    var accountType: Int? = 0,
    @SerializedName("account_type_name")
    var accountTypeName: String? = "",
    @SerializedName("balance_amount")
    var balanceAmount: Double? = 0.0,
    @SerializedName("freeze_amount")
    var freezeAmount: Double? = 0.0,
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("member_id")
    var memberId: String? = "",
    @SerializedName("nature")
    var nature: Int? = 0,
    @SerializedName("status")
    var status: Int? = 0,
    @SerializedName("total_amount")
    var totalAmount: Double? = 0.0
) : Serializable