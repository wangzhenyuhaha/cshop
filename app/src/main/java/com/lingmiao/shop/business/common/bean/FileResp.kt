package com.lingmiao.shop.business.common.bean

data class FileResp<T>(
    var code: Int = 0,
    var `data`: T? = null,
    var message: String = ""
)