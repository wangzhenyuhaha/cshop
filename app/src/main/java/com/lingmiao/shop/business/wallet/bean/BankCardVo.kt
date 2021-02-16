package com.lingmiao.shop.business.wallet.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class BankCardVo(
    @SerializedName("id")
    var id: String? = null,
    /**
     * 银行卡类型，0对私 1对公
     */
    @SerializedName("bank_card_type")
    var bankCardType: Int? = 0,
    /**
     * 开户行名称
     */
    @SerializedName("bank_name")
    var bankName: String? = "",
    /**
     * 银行卡号
     */
    @SerializedName("card_no")
    var cardNo: String? = "",
    /**
     * 是否默认，0不是 1是
     */
//    @SerializedName("is_default")
//    var isDefault: Int? = 0,
//    @SerializedName("merchant_id")
//    var merchantId: String? = "",
    /**
     * 开户名
     */
    @SerializedName("open_account_name")
    var openAccountName: String? = "",
//    @SerializedName("org_id")
//    var orgId: String? = "",
    /**
     * 支行名称
     */
    @SerializedName("sub_bank_name")
    var subBankName: String? = ""
) : Serializable