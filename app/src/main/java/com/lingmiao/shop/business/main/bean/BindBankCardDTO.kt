package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

//银行卡账户
//所有字段必填
data class BindBankCardDTO(

//账户id
    @SerializedName("account_id")
    var accountId: String? = null,

//账户名(账户名称）
    @SerializedName("open_account_name")
    var openAccountName: String? = null,

//卡号（账户号）
    @SerializedName("card_no")
    var cardNo: String? = null,

//账号类型  银行卡类型，0对私 1对公
    @SerializedName("bank_card_type")
    var bankCardType: Int? = null,

//所属银行省
    @SerializedName("bank_province")
    var province: String? = null,

//所属银行市
    @SerializedName("bank_city")
    var city: String? = null,

//所属银行号
    @SerializedName("bank_code")
    var bankCode: String? = null,

//所属银行名
    @SerializedName("bank_name")
    var bankName: String? = null,

//所属支行名
    @SerializedName("sub_bank_name")
    var subBankName: String? = null,

//所属支行号
    @SerializedName("sub_bank_code")
    var subBankCode: String? = null,

//银行卡照片URL
    @SerializedName("bank_urls")
    var bankUrls: String? = null,

//绑定银行卡时使用    默认为0
//是否默认卡  0不是结算账户 1是结算账户
    @SerializedName("is_default")
    var isDefault: Int = 0,

//绑定银行卡时使用
//用户ID
    @SerializedName("member_id")
    var memberId: Int? = null
)


data class CommonBack(
    var code: String = "",
    var `data`: List<Data> = listOf(),
    var message: String = "",
    var success: Boolean = false
)

data class Data(
    var account_id: Any = Any(),
    var bank_card_type: Int = 0,
    var bank_city: Any = Any(),
    var bank_city_id: Any = Any(),
    var bank_name: String = "",
    var bank_province: Any = Any(),
    var bank_province_id: Any = Any(),
    var bank_urls: String = "",
    var card_no: String = "",
    var card_status: Int = 0,
    var id: String = "",
    var is_default: Int = 0,
    var member_id: String = "",
    var mobile: String = "",
    var open_account_name: String = "",
    var sub_bank_code: String = "",
    var sub_bank_name: String = ""
)