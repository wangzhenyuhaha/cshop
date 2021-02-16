package com.lingmiao.shop.business.me.bean
import com.google.gson.annotations.SerializedName


data class AccountSetting(
    @SerializedName("app_name")
    var appName: String?,
    @SerializedName("cast_update")
    var castUpdate: Boolean = false,//是否强制更新
    @SerializedName("download_url")
    var downloadUrl: String?,
    @SerializedName("last_version")
    var lastVersion: String?,
    @SerializedName("need_upgrade")
    var needUpgrade: Boolean = false,
    @SerializedName("upgrade_content")
    var upgradeContent: String?
)