package com.lingmiao.shop.business.login.bean

data class LoginRequest(
    var login_type: Int?,//0 短信验证, 1 用户名密码
    var mobile: String?,
    var password: String?,
    var sms_code: String?,
    var user_name: String?,
    var uuid: String?=""
)