package com.lingmiao.shop.business.me.bean

import com.google.gson.annotations.SerializedName

/**
Create Date : 2021/8/1011:24 下午
Auther      : Fox
Desc        :
 **/
data class HelpDocItemVo (
    @SerializedName("content")
    var content: String? = "",
    @SerializedName("create_time")
    var createTime: String? = "",
    @SerializedName("creater_id")
    var createrId: String? = "",
    @SerializedName("creater_name")
    var createrName: String? = "",
    @SerializedName("file_url")
    var fileUrl: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("is_delete")
    var isDelete: Int? = 0,
    @SerializedName("is_online")
    var isOnline: Int? = 0,
    @SerializedName("merchant_id")
    var merchantId: String? = "",
    @SerializedName("online_time")
    var onlineTime: String? = "",
    @SerializedName("org_id")
    var orgId: String? = "",
    @SerializedName("remarks")
    var remarks: String? = "",
    @SerializedName("sort")
    var sort: Int? = 0,
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("type")
    var type: Int? = 0,
    @SerializedName("update_time")
    var updateTime: String? = "",
    @SerializedName("updater_id")
    var updaterId: String? = "",
    @SerializedName("updater_name")
    var updaterName: String? = ""
)