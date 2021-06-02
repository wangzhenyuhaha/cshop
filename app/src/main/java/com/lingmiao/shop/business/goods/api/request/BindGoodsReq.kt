package com.lingmiao.shop.business.goods.api.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
Create Date : 2021/5/314:20 PM
Auther      : Fox
Desc        :
 **/
class BindGoodsReq : Serializable {
    @SerializedName("goods_ids")
    var ids : List<Int?>? = mutableListOf();
    @SerializedName("shop_cat_id")
    var catId : Int? = null;

}