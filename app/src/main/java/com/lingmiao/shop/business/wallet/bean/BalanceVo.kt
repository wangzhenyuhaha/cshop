package com.lingmiao.shop.business.wallet.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BalanceVo(
    @SerializedName("accountId")
    var accountId: String? = "",
    @SerializedName("accountType")
    var accountType: Int? = 0,
    @SerializedName("accountTypeName")
    var accountTypeName: String? = "",
    @SerializedName("amount")
    var amount: Double? = 0.0,
    @SerializedName("applyRemark")
    var applyRemark: String? = "",
    @SerializedName("applyTime")
    var applyTime: String? = "",
    @SerializedName("bankName")
    var bankName: String? = "",
    @SerializedName("cardNo")
    var cardNo: String? = "",
    @SerializedName("createTime")
    var createTime: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("inspectRemark")
    var inspectRemark: String? = "",
    @SerializedName("inspectTime")
    var inspectTime: String? = "",
    @SerializedName("isDelete")
    var isDelete: Int? = 0,
    @SerializedName("memberId")
    var memberId: String? = "",
    @SerializedName("memberName")
    var memberName: String? = "",
    @SerializedName("openAccountName")
    var openAccountName: String? = "",
    @SerializedName("otherAccountName")
    var otherAccountName: String? = "",
    @SerializedName("otherAccountNo")
    var otherAccountNo: String? = "",
    @SerializedName("otherAccountTypeName")
    var otherAccountTypeName: String? = "",
    @SerializedName("remarks")
    var remarks: String? = "",
    @SerializedName("status")
    var status: Int? = 0,
    @SerializedName("subBankName")
    var subBankName: String? = "",
    @SerializedName("transferRemark")
    var transferRemark: String? = "",
    @SerializedName("updateTime")
    var updateTime: String? = "",
    @SerializedName("updaterId")
    var updaterId: String? = "",
    @SerializedName("withdrawChannel")
    var withdrawChannel: Int? = 0,
    @SerializedName("withdrawNo")
    var withdrawNo: String? = "",
    @SerializedName("withdrawType")
    var withdrawType: Int? = 0
) : Serializable