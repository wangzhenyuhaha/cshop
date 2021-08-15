package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

//银行卡账户
//所有字段必填
data class BindBankCardDTO(

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

    //所属银行编码
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

    //绑定银行卡时使用
    //是否默认卡  0不是结算账户 1是结算账户
    @SerializedName("is_default")
    var isDefault: Int = 0,

    //绑定银行卡时使用
    //用户ID
    @SerializedName("member_id")
    var memberId: Int? = null
)
