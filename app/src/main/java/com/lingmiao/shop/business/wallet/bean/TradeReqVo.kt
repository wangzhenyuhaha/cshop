package com.lingmiao.shop.business.wallet.bean
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class TradeReqVo(
    @SerializedName("body")
    var body: TradeReqBody? = TradeReqBody(),
    @SerializedName("page_num")
    var pageNum: Int? = 0,
    @SerializedName("page_size")
    var pageSize: Int? = 0
) : Serializable

data class TradeReqBody(
    @SerializedName("account_id")
    var accountId: String? = ""
) : Serializable
