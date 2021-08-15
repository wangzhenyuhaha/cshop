package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

data class BankCard(

    // 卡号
    @SerializedName("card_no")
    var CardNo: String? = null,

    // 银行信息
    @SerializedName("bank_info")
    var BankInfo: String? = null,

    // 有效期
    @SerializedName("valid_date")
    var ValidDate: String? = null
)
