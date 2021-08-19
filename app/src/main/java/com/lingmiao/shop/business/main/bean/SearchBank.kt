package com.lingmiao.shop.business.main.bean

data class SearchBank(
    var code: String = "",
    var `data`: List<Data> = listOf(),
    var message: String = "",
    var success: Boolean = false
) {

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

}