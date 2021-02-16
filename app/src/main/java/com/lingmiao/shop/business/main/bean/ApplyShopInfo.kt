package com.lingmiao.shop.business.main.bean
import com.google.gson.annotations.SerializedName


data class ApplyShopInfo(
    @SerializedName("goods_management_category")
    var goodsManagementCategory: String?=null,//店铺经营类目    ids
    @SerializedName("category_names")
    var categoryNames: String?=null,//店铺经营类目 返回文本
    @SerializedName("hold_img")
    var holdImg: String?=null,//手持身份证照片
    @SerializedName("legal_back_img")
    var legalBackImg: String?=null,//身份证反面照片
    @SerializedName("legal_img")
    var legalImg: String?=null,//身份证正面照片
    @SerializedName("licence_img")
    var licenceImg: String?=null,//营业执照电子版
    @SerializedName("link_name")
    var linkName: String?=null,//联系人姓名
    @SerializedName("link_phone")
    var linkPhone: String?=null,//联系人电话
    @SerializedName("shop_add")
    var shopAdd: String?=null,//店铺详细地址
    @SerializedName("shop_city")
    var shopCity: String?=null,//店铺所在市
    @SerializedName("shop_county")
    var shopCounty: String?=null,//店铺所在县
    @SerializedName("shop_lat")
    var shopLat: Double?=null,//店铺纬度
    @SerializedName("shop_lng")
    var shopLng: Double?=null,//店铺经度
    @SerializedName("shop_logo")
    var shopLogo: String?=null,//店铺logo
    @SerializedName("shop_name")
    var shopName: String?=null,//店铺名称
    @SerializedName("shop_province")
    var shopProvince: String?=null,//店铺所在省
//    @SerializedName("shop_tag")
//    var shopTag: String?=null,//店铺类型
    @SerializedName("shop_town")
    var shopTown: String?=null,//店铺所在镇
    @SerializedName("shop_type")
    var shopType: String?=null//店铺类型
)