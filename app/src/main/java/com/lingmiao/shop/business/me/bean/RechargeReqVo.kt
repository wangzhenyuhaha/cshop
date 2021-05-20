package com.lingmiao.shop.business.me.bean
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
Create Date : 2021/5/2011:14 AM
Auther      : Fox
Desc        :
 **/
data class RechargeReqVo(
    @SerializedName("account_id")
    var accountId: String? = "",
    @SerializedName("amount")
    var amount: Double? = 0.0,
    @SerializedName("trade_channel")
    var tradeChannel: Int? = 0
) : Serializable {
    companion object {
        const val PAY_TRADE_CHANNEL_OF_WECHAT = 3;
    }
}