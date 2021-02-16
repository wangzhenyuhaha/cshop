package com.lingmiao.shop.business.wallet.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BankCardAccountBean(
    @SerializedName("bankCardType")
    var bankCardType: Int? = 0,
    @SerializedName("bankName")
    var bankName: String? = "",
    @SerializedName("cardNo")
    var cardNo: String? = "",
    @SerializedName("cardStatus")
    var cardStatus: Int? = 0,
    @SerializedName("createTime")
    var createTime: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("isDefault")
    var isDefault: Int? = 0,
    @SerializedName("isDelete")
    var isDelete: Int? = 0,
    @SerializedName("memberId")
    var memberId: String? = "",
    @SerializedName("mobile")
    var mobile: String? = "",
    @SerializedName("openAccountName")
    var openAccountName: String? = "",
    @SerializedName("subBankName")
    var subBankName: String? = ""
//    @SerializedName("accountId")
//    var accountId: String? = "",
//    @SerializedName("bankUrls")
//    var bankUrls: String? = "",
//    @SerializedName("createrId")
//    var createrId: String? = "",
//    @SerializedName("merchantId")
//    var merchantId: String? = "",
//    @SerializedName("orgId")
//    var orgId: String? = "",
//    @SerializedName("remarks")
//    var remarks: String? = "",
//    @SerializedName("updateTime")
//    var updateTime: String? = "",
//    @SerializedName("updaterId")
//    var updaterId: String? = ""
) : Serializable {

    fun getBankTypeName() : String{
        return if(bankCardType === 0) getPersonalAccount() else getEAccount();
    }

    fun getPersonalAccount() : String{
        return "个人账户";
    }

    fun getEAccount()  : String{
        return "企业账户";
    }

    fun getBankInfo() : String {
        return String.format("%s,%s,%s", bankName, openAccountName, cardNo);
    }
}