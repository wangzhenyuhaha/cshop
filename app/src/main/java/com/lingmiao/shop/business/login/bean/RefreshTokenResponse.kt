package com.lingmiao.shop.business.login.bean
import com.google.gson.annotations.SerializedName


data class RefreshTokenResponse(
    @SerializedName("accessToken")
    var accessToken: String?,
    @SerializedName("refreshToken")
    var refreshToken: String?
)