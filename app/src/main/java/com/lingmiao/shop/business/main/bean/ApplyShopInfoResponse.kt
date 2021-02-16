package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

data class ApplyShopInfoResponse(@SerializedName(value = "shop_id", alternate = ["sellerId","seller_id"])
                                var shopId: Int? = null) {
}