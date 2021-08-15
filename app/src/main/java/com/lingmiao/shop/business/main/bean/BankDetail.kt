package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

data class BankDetail(

    //所属银行
    @SerializedName("baf_name")
    var bafName: String? = null,

    //所属银行码
    @SerializedName("bank_code")
    var bankCode: String? = null,

    //开户银行所在省
    @SerializedName("province")
    var province: String? = null,

    //开户银行所属市
    @SerializedName("city")
    var city: String? = null,

    //所属支行名
    @SerializedName("bank_name")
    var bankName: String? = null,

    //所属支行码
    @SerializedName("net_bank_code")
    var netBankCode: String? = null,

    var type :Int?  = null

)