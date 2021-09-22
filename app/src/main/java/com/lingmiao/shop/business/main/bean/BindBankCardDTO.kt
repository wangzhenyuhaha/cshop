package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

//银行卡账户
//所有字段必填
data class BindBankCardDTO(

//账户id(可为空，不为空时即是修改银行卡)
    @SerializedName("id")
    var id: String? = null,

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
//是否默认卡  0不是结算账户 1是结算账户    绑定时必给
    @SerializedName("is_default")
    var isDefault: Int = 0,

//绑定银行卡时使用
//用户ID  绑定时必给
    @SerializedName("member_id")
    var memberId: Int? = null
)

data class BackBankCard(
    var code: String = "",
    var `data`: List<Data> = listOf(),
    var message: String = "",
    var success: Boolean = false
) {

    data class Data(
        var account_id: Any = Any(),
        var bank_card_type: Int = 0,
        var bank_city_id: Any = Any(),
        var bank_code: String = "",
        var bank_name: String = "",
        var bank_province_id: Any = Any(),
        var bank_urls: String = "",
        var card_no: String = "",
        var card_status: Int = 0,
        var city: String =" Any()",
        var id: String = "",
        var is_default: Int = 0,
        var member_id: String = "",
        var mobile: String = "",
        var open_account_name: String = "",
        var province: String = "",
        var sub_bank_code: String = "",
        var sub_bank_name: String = ""
    )
}

class SearchBankName : ArrayList<SearchBankNameItem>()

data class SearchBankNameItem(
    var bank_code: String = "",
    var bank_name: String = "",
    var create_time: Any = Any(),
    var creater_id: Any = Any(),
    var id: String = "",
    var is_delete: Int = 0,
    var main_account_length: Int = 0,
    var merchant_id: Any = Any(),
    var org_id: Any = Any(),
    var remarks: Any = Any(),
    var sign_length: Int = 0,
    var sign_no: String = "",
    var update_time: Any = Any(),
    var updater_id: Any = Any()
)
