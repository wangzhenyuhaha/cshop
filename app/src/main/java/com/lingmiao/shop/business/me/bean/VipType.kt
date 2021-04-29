package com.lingmiao.shop.business.me.bean
import com.google.gson.annotations.SerializedName


/**
Create Date : 2021/4/95:05 PM
Auther      : Fox
Desc        :
 **/
class VipType {
    var checked : Boolean = false;

    @SerializedName("create_time")
    var createTime: Any? = Any();
    @SerializedName("creater_id")
    var createrId: Any? = Any();
    @SerializedName("id")
    var id: String? = "";
    @SerializedName("is_delete")
    var isDelete: Int? = 0;
    @SerializedName("is_try")
    var isTry: Int? = 0;
    @SerializedName("merchant_id")
    var merchantId: Any? = Any();
    @SerializedName("org_id")
    var orgId: Any? = Any();
    @SerializedName("price")
    var price: Double? = 0.0;
    @SerializedName("product_name")
    var productName: String? = "";
    @SerializedName("remarks")
    var remarks: Any? = Any();
    @SerializedName("sort")
    var sort: Int? = 0;
    @SerializedName("update_time")
    var updateTime: Any? = Any();
    @SerializedName("updater_id")
    var updaterId: Any? = Any();
    @SerializedName("valid_time")
    var validTime: Int? = 0
}
