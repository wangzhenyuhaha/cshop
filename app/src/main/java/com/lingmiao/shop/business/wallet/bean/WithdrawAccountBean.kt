package com.lingmiao.shop.business.wallet.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class WithdrawAccountBean(
    /**
     * 账户名
     */
    @SerializedName("account_name")
    var accountName: String? = "",
    /**
     * 账号
     */
    @SerializedName("account_no")
    var accountNo: String? = "",
    /**
     * 是否对公 0否 1是
     */
    @SerializedName("of_public")
    var ofPublic: Int? = 0,
    /**
     * 账号类型, 1支付宝 2微信
     */
    @SerializedName("type")
    var type: Int? = 0,
    @SerializedName("id")
    var id: String? = ""

) : Serializable {
    companion object {
        const val TYPE_ALI_PAY = 1;
        const val TYPE_WECHAT = 2;
    }

}