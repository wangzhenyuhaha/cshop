package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

data class OCRDiscern(

    @SerializedName("discern_type")
    var discernType: Int ? = null,

    @SerializedName("image_url")
    var imageURL: String? = null

)
