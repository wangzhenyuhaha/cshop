package com.lingmiao.shop.business.order.bean

import com.google.gson.annotations.SerializedName
import retrofit2.http.Query

/**
Create Date : 2021/6/95:48 PM
Auther      : Fox
Desc        :
 **/
class RefoundReqVo {

    var agree: Int? = null
    @SerializedName("refund_price")
    var refundMoney: String? = ""
    var remark: String? = ""
    var sn: String? = ""
}