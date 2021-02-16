package com.lingmiao.shop.business.wallet.bean

import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AlipayAccountVo(
    @SerializedName("accountName")
    var accountName: String? = "",
    @SerializedName("accountNo")
    var accountNo: String? = "",
    @SerializedName("createTime")
    var createTime: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("isDelete")
    var isDelete: Int? = 0,
    @SerializedName("memberId")
    var memberId: String? = "",
    @SerializedName("ofPublic")
    var ofPublic: Int? = 0,
    @SerializedName("type")
    var type: Int? = 0,
    @SerializedName("typeName")
    var typeName: String? = ""
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

    fun getPublicName() : String {
        return if(ofPublic === WalletConstants.PUBLIC_PRIVATE) WalletConstants.PUBLIC_PRIVATE_NAME else WalletConstants.PUBLIC_COMPANY_NAME
    }

}