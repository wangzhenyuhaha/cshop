package com.lingmiao.shop.business.me.bean
import com.google.gson.annotations.SerializedName


/**
Create Date : 2021/6/710:21 AM
Auther      : Fox
Desc        :
 **/
data class ShareVo(
    @SerializedName("channel_type")
    var channelType: Int = 0,
    @SerializedName("content")
    var content: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("image_url")
    var imageUrl: String = "",
    @SerializedName("path")
    var path: String = "",
    @SerializedName("share_type")
    var shareType: Int = 0,
    @SerializedName("title")
    var title: String = ""
)