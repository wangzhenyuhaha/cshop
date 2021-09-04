package com.lingmiao.shop.business.main.bean

class TlMccListVO : ArrayList<TlMccListVOItem>()

data class TlMccListVOItem(
    var category_id: Int = 0,
    var create_time: Any = Any(),
    var creater_id: Any = Any(),
    var id: String = "",
    var is_delete: Int = 0,
    var mcc_name: String = "",
    var mccid: Int = 0,
    var merchant_id: Any = Any(),
    var org_id: Any = Any(),
    var remarks: Any = Any(),
    var update_time: Any = Any(),
    var updater_id: Any = Any()
)