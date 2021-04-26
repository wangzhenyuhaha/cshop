package com.lingmiao.shop.business.me.bean
import com.blankj.utilcode.util.StringUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ShopManage(
    @SerializedName("link_phone")
    var linkPhone: String?,//客服电话
      @SerializedName("link_name")
    var linkName: String?,
//    @SerializedName("open_end_time")
//    var openEndTime: String?,
//    @SerializedName("open_start_time")
//    var openStartTime: String?,
//    @SerializedName("open_time_type")
//    var openTimeType: Int?,
    // 店铺类型
    @SerializedName("ship_type")
    var shipType: String?,
    @SerializedName("shop_add")
    var shopAdd: String?,
//    @SerializedName("shop_banner")
//    var shopBanner: String?,
    @SerializedName("shop_desc")
    var shopDesc: String?,
    @SerializedName("shop_id")
    var shopId: Int?,
    @SerializedName("shop_lat")
    var shopLat: Double?,
    @SerializedName("shop_lng")
    var shopLng: Double?,
    @SerializedName("shop_logo")
    var shopLogo: String?,
    @SerializedName("shop_name")
    var shopName: String?,
    @SerializedName("category_names")
    var categoryNames: String?,
    @SerializedName("licence_img")
    var licenceImg: String?//店铺资质
) : Serializable {

    fun getShipTypeStr() : String {
        return if(StringUtils.equals("2", shipType)) "单店" else "连锁店";
    }
}