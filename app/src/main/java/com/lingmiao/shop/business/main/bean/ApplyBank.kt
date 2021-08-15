package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

data class ApplyBank(

    @SerializedName("body")
    var body: ApplyBankDetail? = null,

    @SerializedName("page_num")
    var pageNum: Int? = null,

    @SerializedName("page_size")
    var pageSize: Int? = null

){

    data class ApplyBankDetail(

        //所属银行码
        @SerializedName("bank_code")
        var bankCode: String? = null,

        //支行名
        @SerializedName("netbank_name")
        var netBankName:String? = null,

        //支行号
        @SerializedName("net_bank_code")
        var netBankCode:String? = null,

        //开户银行所在省
        @SerializedName("province")
        var province: String? = null,

        //开户银行所属市
        @SerializedName("city")
        var city: String? = null

    )
}