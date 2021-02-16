package com.lingmiao.shop.business.login.bean

import com.google.gson.annotations.SerializedName
import com.james.common.net.BaseResponse

data class CaptchaAli(
    var mobile: String?=null,
    var nc_token: String?=null,
    var session_id: String?=null,
    var sessionid: String?=null,
    var sig: String?=null
)
