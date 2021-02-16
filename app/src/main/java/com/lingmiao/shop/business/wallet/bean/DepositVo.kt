package com.lingmiao.shop.business.wallet.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DepositVo(

    @SerializedName("amount")
    var amount: Double? = 0.0,
    @SerializedName("createTime")
    var createTime: String? = "",
    @SerializedName("fundFlowType")
    var fundFlowType: Int? = 0,
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("isDelete")
    var isDelete: Int? = 0,
    @SerializedName("memberId")
    var memberId: String? = "",
    @SerializedName("memberName")
    var memberName: String? = "",
    @SerializedName("otherAccountId")
    var otherAccountId: String? = "",
    @SerializedName("otherMemberId")
    var otherMemberId: String? = "",
    @SerializedName("otherMemberName")
    var otherMemberName: String? = "",
    @SerializedName("serialNo")
    var serialNo: String? = "",
    @SerializedName("tradeChannel")
    var tradeChannel: Int? = 0,
    @SerializedName("tradeContent")
    var tradeContent: String? = "",
    @SerializedName("tradeStatus")
    var tradeStatus: Int? = 0,
    @SerializedName("tradeTime")
    var tradeTime: String? = "",
    @SerializedName("tradeType")
    var tradeType: Int? = 0,
    @SerializedName("tradeTypeName")
    var tradeTypeName: String? = "",
    @SerializedName("tradeVoucherNo")
    var tradeVoucherNo: String? = ""
) : Serializable