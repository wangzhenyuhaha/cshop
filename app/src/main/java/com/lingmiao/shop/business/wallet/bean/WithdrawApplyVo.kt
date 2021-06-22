package com.lingmiao.shop.business.wallet.bean
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class WithdrawApplyVo(
    @SerializedName("account_id")
    var accountId: String? = "",
    @SerializedName("amount")
    var amount: Double? = 0.0,
//    @SerializedName("apply_remark")
//    var applyRemark: String? = "",
    @SerializedName("bank_name")
    var bankName: String? = "",
    @SerializedName("card_no")
    var cardNo: String? = "",
    @SerializedName("open_account_name")
    var openAccountName: String? = "",
    @SerializedName("other_account_name")
    var otherAccountName: String? = "",
    @SerializedName("other_account_no")
    var otherAccountNo: String? = "",
    @SerializedName("other_account_type_name")
    var otherAccountTypeName: String? = "",
    @SerializedName("sub_bank_name")
    var subBankName: String? = "",
    /**
     * 选择类型 1银行卡 2支付宝 3微信
     */
    @SerializedName("withdraw_channel")
    var withdrawChannel: Int? = 0
) : Serializable {

}