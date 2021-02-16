package com.lingmiao.shop.business.common.bean
import com.google.gson.annotations.SerializedName


data class FileResponse(
    @SerializedName("ext")
    var ext: String?,//文件后缀
    @SerializedName("name")
    var name: String?,//文件名称
    @SerializedName("url")
    var url: String?//图片地址
)