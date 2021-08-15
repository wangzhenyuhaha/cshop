package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

data class BankInfo(
    @SerializedName("records")
    var records: List<BankDetail>? = null,

    @SerializedName("total_count")
    var totalCount: Int? = null,


    @SerializedName("total_pages")
    var totalPages: Int? = null

)
